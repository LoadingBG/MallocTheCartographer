package malloc;

import malloc.game.*;

public class Main {
    public static void main(String[] args) {
        var bot = new Bot(Board.plain(), Goal.GREENBOUGH, Goal.THE_GOLDEN_GRANARY, Goal.WILDHOLDS, Goal.LOST_BARONY);

        System.out.println(bot.board());
        bot.map(new Piece[] { Piece.TREETOP_VILLAGE_FOREST, Piece.FISHING_VILLAGE_VILLAGE });
        System.out.println(bot.board());
        bot.map(new Piece[] { Piece.FARMLAND_SMALL, Piece.FARMLAND_BIG });
        System.out.println(bot.board());
    }
}