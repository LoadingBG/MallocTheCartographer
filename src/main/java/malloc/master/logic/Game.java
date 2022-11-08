package malloc.master.logic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.*;
import java.util.*;
import java.util.function.*;

import javax.imageio.*;

import malloc.Utils;
import malloc.game.*;
import malloc.master.logic.decks.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.*;

public final class Game {
    private static final List<Map.Entry<Goal, String>> FOREST_GOALS = List.of(
        Map.entry(Goal.SENTINEL_WOOD, "Sentinel Wood"),
        Map.entry(Goal.TREETOWER, "Treetower"),
        Map.entry(Goal.GREENBOUGH, "Greenbough"),
        Map.entry(Goal.STONESIDE_FOREST, "Stoneside Forest")
    );
    private static final List<Map.Entry<Goal, String>> WATER_GOALS = List.of(
        Map.entry(Goal.CANAL_LAKE, "Canal Lake"),
        Map.entry(Goal.THE_GOLDEN_GRANARY, "The Golden Granary"),
        Map.entry(Goal.MAGES_VALLEY, "Mages Valley"),
        Map.entry(Goal.SHORESIDE_EXPANSE, "Shoreside Expanse")
    );
    private static final List<Map.Entry<Goal, String>> VILLAGE_GOALS = List.of(
        Map.entry(Goal.WILDHOLDS, "Wildholds"),
        Map.entry(Goal.GREENGOLD_PLAINS, "Greengold Plains"),
        Map.entry(Goal.GREAT_CITY, "Great City"),
        Map.entry(Goal.SHIELDGATE, "Shieldgate")
    );
    private static final List<Map.Entry<Goal, String>> GENERIC_GOALS = List.of(
        Map.entry(Goal.BORDERLANDS, "Borderlands"),
        Map.entry(Goal.THE_BROKEN_ROAD, "The Broken Road"),
        Map.entry(Goal.LOST_BARONY, "Lost Barony"),
        Map.entry(Goal.THE_CAULDRONS, "The Cauldrons")
    );

    private static final Random RNG = new Random();

    private final List<Player> players;
    private final Map.Entry<Goal, String> goalA;
    private final Map.Entry<Goal, String> goalB;
    private final Map.Entry<Goal, String> goalC;
    private final Map.Entry<Goal, String> goalD;

    private final String code;
    private final Deck deck;
    private int season = 0;
    private int turnsLeft = 0;

    public Game(final String code, final List<Map.Entry<Member, InteractionHook>> players, final Board board) {
        this.code = code;
        this.players = players.stream().map(p -> new Player(p.getKey(), p.getValue(), board.copy())).toList();
        var goals = new ArrayList<>(List.of(
            FOREST_GOALS.get(RNG.nextInt(FOREST_GOALS.size())),
            WATER_GOALS.get(RNG.nextInt(WATER_GOALS.size())),
            VILLAGE_GOALS.get(RNG.nextInt(VILLAGE_GOALS.size())),
            GENERIC_GOALS.get(RNG.nextInt(GENERIC_GOALS.size()))
        ));
        Collections.shuffle(goals);
        goalA = goals.get(0);
        goalB = goals.get(1);
        goalC = goals.get(2);
        goalD = goals.get(3);
        deck = new ClassicDeck();
        deck.reset(false);
    }

    public List<Map.Entry<Goal, String>> activeGoals() {
        return switch (season) {
            case 1 -> List.of(goalA, goalB);
            case 2 -> List.of(goalB, goalC);
            case 3 -> List.of(goalC, goalD);
            case 4 -> List.of(goalD, goalA);
            default -> throw new UnsupportedOperationException("Unreachable");
        };
    }

    public List<String> goalNames() {
        return List.of(goalA.getValue(), goalB.getValue(), goalC.getValue(), goalD.getValue());
    }

    public int turnsLeft() {
        return turnsLeft;
    }

    public List<Deck.Card> currentCards() {
        return deck.dealtCards();
    }

    public Deck.Card currentCard() {
        return deck.currentCard();
    }

    public void startMove() {
        deck.deal();
        if (deck.currentCard() instanceof Deck.EnemyCard enemyCard) {
            var rotated = new ArrayList<>(players);
            Collections.rotate(rotated, enemyCard.rotateBoardsClockwise() ? 1 : -1);
            for (var i = 0; i < players.size(); ++i) {
                players.get(i).giveBoard(rotated.get(i).originalBoard());
            }
        }
        players.forEach(p -> p.setCard(deck.currentCard(), deck.withRuins()));
        turnsLeft -= deck.currentCard().turns();
    }

    public boolean hasNextSeason() {
        return season < 4;
    }

    public void startNextSeason() {
        ++season;
        turnsLeft = switch (season) {
            case 1, 2 -> 8;
            case 3 -> 7;
            case 4 -> 6;
            default -> throw new UnsupportedOperationException("Unreachable");
        };
        deck.reset(season > 1);
    }

    public boolean hasSeasonEnded() {
        return turnsLeft <= 0;
    }

    public Player player(final Member member) {
        return players.stream().filter(p -> p.member.equals(member)).findFirst().orElseThrow();
    }

    public List<Player> players() {
        return players;
    }

    public int season() {
        return season;
    }

    public String makeCommand(final String command) {
        return "game:" + code + ":" + command;
    }

    public boolean hasPlayer(final Member member) {
        return players.stream().map(Player::member).anyMatch(Predicate.isEqual(member));
    }

    public long countPlayersThinking() {
        return players.stream().filter(Player::isThinking).count();
    }

    public int calculateScore(final Player p) {
        var activeGoals = activeGoals();
        return p.calculateScore(List.of(activeGoals.get(0).getKey(), activeGoals.get(1).getKey(), Goal.COINS, Goal.ENEMIES));
    }

    public void updateHook(final Member member, final InteractionHook hook) {
        players.stream().filter(p -> p.member.equals(member)).findFirst().orElseThrow().updateHook(hook);
    }

    public static final class Player {
        private final Member member;
        private InteractionHook hook;
        private final Board board;
        private int score;

        private Board currentBoard;
        private Deck.Card card;
        private boolean withRuins;
        private Piece currentPiece;
        private int pieceX;
        private int pieceY;

        public Player(final Member member, final InteractionHook hook, final Board board) {
            this.member = member;
            this.hook = hook;
            this.board = board;
            currentBoard = this.board;
            score = 0;
        }

        public Member member() {
            return member;
        }

        public InteractionHook hook() {
            return hook;
        }

        public void updateHook(final InteractionHook hook) {
            this.hook = hook;
        }

        public Board originalBoard() {
            return board;
        }

        public Board currentBoard() {
            return currentBoard;
        }

        public int score() {
            return score;
        }

        public Piece currentPiece() {
            return currentPiece;
        }

        public boolean isPiecePlaceable() {
            return currentBoard.canFitPiece(currentPiece, pieceX, pieceY, withRuins);
        }

        public void giveBoard(final Board board) {
            currentBoard = board;
        }

        public void setCard(final Deck.Card card, final boolean withRuins) {
            this.card = card.hasPlaceablePiece(currentBoard, withRuins) ? card : card.withReplacedPieces();
            this.withRuins = withRuins;
            currentPiece = card.pieces().get(0);
            pieceX = 0;
            pieceY = 0;
        }

        public void selectPiece(final String pieceId) {
            currentPiece = card.pieces().stream().filter(p -> p.id().equals(pieceId)).findFirst().orElseThrow();
            pieceX = 0;
            pieceY = 0;
        }

        public void movePieceRight() {
            if (pieceX + currentPiece.width() < currentBoard.width()) {
                ++pieceX;
            }
        }

        public void movePieceLeft() {
            if (pieceX > 0) {
                --pieceX;
            }
        }

        public void movePieceDown() {
            if (pieceY + currentPiece.height() < currentBoard.height()) {
                ++pieceY;
            }
        }

        public void movePieceUp() {
            if (pieceY > 0) {
                --pieceY;
            }
        }

        public void flipPieceHorizontally() {
            currentPiece = currentPiece.flipHorizontal();
        }

        public void flipPieceVertically() {
            currentPiece = currentPiece.flipVertical();
        }

        public void rotatePieceClockwise() {
            currentPiece = currentPiece.rotateClockwise();
            putPieceInBounds();
        }

        public void rotatePieceCounterclockwise() {
            currentPiece = currentPiece.rotateCounterclockwise();
            putPieceInBounds();
        }

        private void putPieceInBounds() {
            pieceX = Math.min(pieceX, currentBoard.width() - currentPiece.width());
            pieceY = Math.min(pieceY, currentBoard.height() - currentPiece.height());
        }

        public void placePiece() {
            currentBoard.placePiece(currentPiece, pieceX, pieceY);
            currentPiece = null;
            currentBoard = board;
        }

        public boolean isThinking() {
            return currentPiece != null;
        }

        public int calculateScore(List<Goal> goals) {
            var seasonScore = goals.stream().mapToInt(g -> g.score(board)).sum();
            score += seasonScore;
            return seasonScore;
        }

        public byte[] boardImageBytes() {
            if (currentPiece == null) {
                return currentBoard.toImageBytes();
            }

            var image = currentBoard.toImage();
            var graphics = image.createGraphics();
            graphics.setColor(currentBoard.canFitPiece(currentPiece, pieceX, pieceY, withRuins) ? Color.GREEN : Color.RED);
            graphics.setStroke(new BasicStroke(Utils.IMAGE_SIZE / 10f));

            for (var j = 0; j < currentPiece.height(); ++j) {
                for (var i = 0; i < currentPiece.width(); ++i) {
                    if (currentPiece.get(j, i) != null) {
                        graphics.drawRect((i + pieceX) * Utils.IMAGE_SIZE, (j + pieceY) * Utils.IMAGE_SIZE, Utils.IMAGE_SIZE, Utils.IMAGE_SIZE);
                    }
                }
            }

            var bytes = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "png", bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return bytes.toByteArray();
        }
    }
}
