package malloc.master;

import java.util.*;

import malloc.*;
import malloc.master.logic.*;
import net.dv8tion.jda.api.events.interaction.component.*;
import net.dv8tion.jda.api.hooks.*;
import net.dv8tion.jda.api.utils.FileUpload;

public final class GameListener extends ListenerAdapter {
    @Override
    public void onGenericComponentInteractionCreate(GenericComponentInteractionCreateEvent event) {
        if (!event.getComponentId().startsWith("game")) {
            return;
        }

        var parts = event.getComponentId().split(":");
        var game = Malloc.GAME_MANAGER.execute(parts[1], parts[2], event);

        game.updateHook(event.getMember(), event.getHook());

        switch (parts[2]) {
            case "selectPiece", "flipHorizontal", "flipVertical", "rotateClockwise", "rotateCounterclockwise",
                "moveLeft", "moveRight", "moveUp", "moveDown" -> {
                var player = game.player(event.getMember());
                event.editMessageAttachments(FileUpload.fromData(player.boardImageBytes(), "board.png"))
                    .setEmbeds(Embeds.move(game))
                    .setComponents(Embeds.moveComponents(game, player))
                    .queue();
            }
            case "placePiece" -> {
                var player = game.player(event.getMember());
                event.editMessageAttachments(FileUpload.fromData(player.boardImageBytes(), "board.png"))
                    .setEmbeds(Embeds.piecePlaced(game, player))
                    .setComponents()
                    .queue();

                game.players()
                    .stream()
                    .filter(p -> !p.isThinking() && !p.member().equals(event.getMember()))
                    .forEach(p -> p.hook()
                        .editOriginalAttachments(FileUpload.fromData(p.boardImageBytes(), "board.png"))
                        .setEmbeds(Embeds.piecePlaced(game, p))
                        .setComponents()
                        .queue());

                if (game.countPlayersThinking() == 0) {
                    Utils.givePlayersTime();

                    if (game.hasSeasonEnded()) {
                        if (!game.hasNextSeason()) {
                            endGame(game);
                            return;
                        }
                        startNextSeason(game);

                        Utils.givePlayersTime();
                    }
                    makeMove(game);
                }
            }
        }
    }

    public static void makeMove(final Game game) {
        // TODO: finish

        game.startMove();

        game.players().forEach(player -> player.hook()
            .editOriginalAttachments(FileUpload.fromData(player.boardImageBytes(), "board.png"))
            .setEmbeds(Embeds.move(game))
            .setComponents(Embeds.moveComponents(game, player))
            .queue());
    }

    public static void startGame(final String code) {
        var game = Malloc.GAME_MANAGER.execute(code, "start", null);

        game.players().forEach(player -> player.hook()
            .editOriginalAttachments(FileUpload.fromData(player.boardImageBytes(), "board.png"))
            .setEmbeds(Embeds.move(game))
            .setComponents(Embeds.moveComponents(game, player))
            .queue());
    }

    public static void startNextSeason(final Game game) {
        game.players().forEach(p -> {
            var score = game.calculateScore(p);
            p.hook()
                .editOriginalAttachments(FileUpload.fromData(p.boardImageBytes(), "board.png"))
                .setEmbeds(Embeds.seasonEnded(game, p, score))
                .setComponents()
                .queue();
        });

        // TODO: additional stuff at end of season
        game.startNextSeason();
    }

    private static void endGame(final Game game) {
        var scoreboard = game.players().stream().sorted(Comparator.comparingInt(Game.Player::score).reversed()).toList();

        var list = new StringBuilder();
        for (var i = 0; i < scoreboard.size(); ++i) {
            list.append(switch (i + 1) {
                case 1 -> ":first_place:";
                case 2 -> ":second_place:";
                case 3 -> ":third_place:";
                default -> ":black_large_square:";
            });
            list.append(" ").append(scoreboard.get(i).member().getAsMention()).append(" (").append(scoreboard.get(i).score()).append(" points)\n");
        }

        for (var i = 0; i < scoreboard.size(); ++i) {
            var curr = scoreboard.get(i);
            curr.hook()
                .editOriginalAttachments(FileUpload.fromData(curr.boardImageBytes(), "board.png"), FileUpload.fromData(scoreboard.get(0).boardImageBytes(), "winner_board.png"))
                .setEmbeds(Embeds.end(i + 1, list.toString()))
                .setComponents()
                .queue();
        }
    }
}
