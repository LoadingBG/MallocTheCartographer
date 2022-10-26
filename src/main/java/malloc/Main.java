package malloc;

import malloc.game.Board;
import malloc.game.Piece;

public class Main {
    public static void main(String[] args) {
        var board = Board.plain();
        System.out.println(board);
        board.placePiece(Piece.GREAT_RIVER_BIG, 3, 0);
        System.out.println(board);
        board.placePiece(Piece.HAMLET_BIG, 1, 0);
        System.out.println(board);
    }
}