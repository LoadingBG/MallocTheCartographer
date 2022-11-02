package malloc.game;

import java.util.*;

import malloc.game.*;

public final class Bot {
    private final Board board;
    private final Goal goalA;
    private final Goal goalB;
    private final Goal goalC;
    private final Goal goalD;

    private int activeSeason;
    private Goal[] activeGoals;
    private int score;

    public Bot(final Board board, final Goal goalA, final Goal goalB, final Goal goalC, final Goal goalD) {
        this.board = board;
        this.goalA = goalA;
        this.goalB = goalB;
        this.goalC = goalC;
        this.goalD = goalD;

        activeSeason = 0;
        activeGoals = new Goal[] {};
        switchSeason();
        score = 0;
    }

    public void map(Piece[] pieces) {
        // TODO: rotate and flip pieces
        Piece best = null;
        int bestX = -1;
        int bestY = -1;
        int bestScore = 0;

        for (var piece : pieces) {
            for (var i = 0; i < board.height(); ++i) {
                for (var j = 0; j < board.width(); ++j) {
                    if (board.canFitPiece(piece, i, j)) {
                        var copy = board.copy();
                        copy.placePiece(piece, i, j);
                        var placementScore = Arrays.stream(activeGoals).mapToInt(g -> g.score(copy)).sum();
                        if (bestScore < placementScore) {
                            bestScore = placementScore;
                            best = piece;
                            bestX = i;
                            bestY = j;
                        }
                    }
                }
            }
        }

        if (best == null) {
            map(Arrays.stream(pieces).map(Piece::replacement).toArray(Piece[]::new));
        } else {
            board.placePiece(best, bestX, bestY);
        }
    }

    public void placeEnemy(Board board, Piece enemy) {
        int bestX = -1;
        int bestY = -1;
        int bestScore = 0;

        for (var i = 0; i < board.height(); ++i) {
            for (var j = 0; j < board.width(); ++j) {
                if (board.canFitPiece(enemy, i, j)) {
                    var copy = board.copy();
                    copy.placePiece(enemy, i, j);
                    var placementScore = Arrays.stream(activeGoals).mapToInt(g -> -g.score(copy)).sum();
                    if (bestScore < placementScore) {
                        bestScore = placementScore;
                        bestX = i;
                        bestY = j;
                    }
                }
            }
        }

        if (bestX == -1) {
            placeEnemy(board, enemy.replacement());
        } else {
            board.placePiece(enemy, bestX, bestY);
        }
    }

    public void placeEnemyOnSelf(Piece enemy, int x, int y) {
        board.placePiece(enemy, x, y);
    }

    public void switchSeason() {
        score += Arrays.stream(activeGoals).mapToInt(g -> g.score(board)).sum();

        activeGoals = switch (++activeSeason) {
            // TODO: add goals for early game
            case 1 -> new Goal[] { goalA, goalB, Goal.COINS, Goal.ENEMIES };
            case 2 -> new Goal[] { goalB, goalC, Goal.COINS, Goal.ENEMIES };
            case 3 -> new Goal[] { goalC, goalD, Goal.COINS, Goal.ENEMIES };
            case 4 -> new Goal[] { goalD, goalA, Goal.COINS, Goal.ENEMIES };
            default -> throw new UnsupportedOperationException("Unreachable");
        };
    }

    public Board board() {
        return board;
    }

    public int score() {
        return score;
    }
}
