package malloc.master.logic.decks;

import java.util.*;

import malloc.game.*;

public interface Deck {
    void reset(final boolean addEnemy);

    void deal();

    List<Card> dealtCards();

    Card currentCard();

    boolean withRuins();

    interface Card {
        String name();

        List<Piece> pieces();

        int turns();

        Map.Entry<Card, Boolean> placeableVariant(final Board board, final boolean withRuins);
    }

    record NormalCard(String name, int turns, List<Piece> pieces) implements Card {
        @Override
        public Map.Entry<Card, Boolean> placeableVariant(final Board board, final boolean withRuins) {
            var placeablePieces = new ArrayList<Piece>();
            outer:
            for (var piece : pieces) {
                var orientations = new HashSet<>(List.of(
                    piece,
                    piece.rotateClockwise(),
                    piece.rotateClockwise().rotateClockwise(),
                    piece.rotateCounterclockwise(),
                    piece.flipHorizontal(),
                    piece.flipHorizontal().rotateClockwise(),
                    piece.flipHorizontal().rotateClockwise().rotateClockwise(),
                    piece.flipHorizontal().rotateCounterclockwise()
                ));
                for (var orientation : orientations) {
                    for (var i = 0; i < board.height(); ++i) {
                        for (var j = 0; j < board.width(); ++j) {
                            if (board.canFitPiece(orientation, i, j, withRuins)) {
                                placeablePieces.add(piece);
                                continue outer;
                            }
                        }
                    }
                }
            }

            if (placeablePieces.isEmpty()) {
                // TODO: if with ruins, can place any terrain type??
                return Map.entry(new NormalCard(name, turns, pieces.stream().map(Piece::replacement).distinct().toList()), false);
            }
            return Map.entry(new NormalCard(name, turns, placeablePieces), withRuins);
        }
    }

    record ModifierCard(String name) implements Card {
        @Override
        public List<Piece> pieces() {
            return List.of();
        }

        @Override
        public int turns() {
            throw new UnsupportedOperationException("Unreachable");
        }

        @Override
        public Map.Entry<Card, Boolean> placeableVariant(final Board board, final boolean withRuins) {
            throw new UnsupportedOperationException("Unreachable");
        }
    }

    record EnemyCard(String name, Piece piece, boolean rotateBoardsClockwise) implements Card {
        @Override
        public List<Piece> pieces() {
            return List.of(piece);
        }

        @Override
        public int turns() {
            return 0;
        }

        @Override
        public Map.Entry<Card, Boolean> placeableVariant(Board board, boolean withRuins) {
            var orientations = new HashSet<>(List.of(
                piece,
                piece.rotateClockwise(),
                piece.rotateClockwise().rotateClockwise(),
                piece.rotateCounterclockwise(),
                piece.flipHorizontal(),
                piece.flipHorizontal().rotateClockwise(),
                piece.flipHorizontal().rotateClockwise().rotateClockwise(),
                piece.flipHorizontal().rotateCounterclockwise()
            ));
            for (var orientation : orientations) {
                for (var i = 0; i < board.height(); ++i) {
                    for (var j = 0; j < board.width(); ++j) {
                        if (board.canFitPiece(orientation, i, j, false)) {
                            return Map.entry(this, false);
                        }
                    }
                }
            }
            return Map.entry(new EnemyCard(name, piece.replacement(), rotateBoardsClockwise), false);
        }
    }
}
