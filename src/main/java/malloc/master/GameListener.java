package malloc.master;

import java.util.*;
import java.util.concurrent.*;

import malloc.game.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.hooks.*;
import net.dv8tion.jda.api.interactions.*;

public final class GameListener extends ListenerAdapter {
    private static final Map<String, Game> GAMES = new ConcurrentHashMap<>();

    public static void setupGame(String code, List<Map.Entry<Member, InteractionHook>> players, Board board) {
        var game = new Game(players, board);
        GAMES.put(code, game);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        game.startNextSeason();
    }
}
