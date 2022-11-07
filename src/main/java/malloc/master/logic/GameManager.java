package malloc.master.logic;

import java.util.*;
import java.util.concurrent.*;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.*;

public final class GameManager {
    private final Map<String, Game> games = new ConcurrentHashMap<>();

    public Game setupGame(final Room room) {
        var game = new Game(room.code(), room.playerInfo(), room.board());
        games.put(room.code(), game);
        return game;
    }

    public boolean hasGame(final String code) {
        return games.containsKey(code);
    }

    public Game getGame(String code) {
        return games.get(code);
    }

    public Game execute(final String code, final String command, final GenericComponentInteractionCreateEvent event) {
        var game = games.get(code);
        switch (command) {
            case "start" -> {
                game.startNextSeason();
                game.startMove();
            }
            case "selectPiece" -> game.player(event.getMember()).selectPiece(((StringSelectInteractionEvent) event).getValues().get(0));
            case "moveUp" -> game.player(event.getMember()).movePieceUp();
            case "moveDown" -> game.player(event.getMember()).movePieceDown();
            case "moveLeft" -> game.player(event.getMember()).movePieceLeft();
            case "moveRight" -> game.player(event.getMember()).movePieceRight();
            case "flipHorizontal" -> game.player(event.getMember()).flipPieceHorizontally();
            case "flipVertical" -> game.player(event.getMember()).flipPieceVertically();
            case "rotateClockwise" -> game.player(event.getMember()).rotatePieceClockwise();
            case "rotateCounterclockwise" -> game.player(event.getMember()).rotatePieceCounterclockwise();
            case "placePiece" -> game.player(event.getMember()).placePiece();
        }
        return game;
    }

    public boolean hasPlayer(final Member member) {
        return games.values().stream().anyMatch(g -> g.hasPlayer(member));
    }
}
