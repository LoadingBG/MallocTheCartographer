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

        boolean hasPlaceablePiece(final Board board, final boolean withRuins);

        Card withReplacedPieces();
    }

    record NormalCard(String name, int turns, List<Piece> pieces) implements Card {
        @Override
        public boolean hasPlaceablePiece(Board board, boolean withRuins) {
            for (var piece : pieces) {
                for (var i = 0; i < board.height(); ++i) {
                    for (var j = 0; j < board.width(); ++j) {
                        if (board.canFitPiece(piece, i, j, withRuins)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public Card withReplacedPieces() {
            return new NormalCard(name, turns, pieces.stream().map(Piece::replacement).toList());
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
        public boolean hasPlaceablePiece(Board board, boolean withRuins) {
            throw new UnsupportedOperationException("Unreachable");
        }

        @Override
        public Card withReplacedPieces() {
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
        public boolean hasPlaceablePiece(final Board board, final boolean withRuins) {
            for (var i = 0; i < board.height(); ++i) {
                for (var j = 0; j < board.width(); ++j) {
                    if (board.canFitPiece(piece, i, j, withRuins)) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public Card withReplacedPieces() {
            return new EnemyCard(name, piece.replacement(), rotateBoardsClockwise);
        }
    }
}
