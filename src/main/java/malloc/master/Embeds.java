package malloc.master;

import java.awt.Color;
import java.util.*;
import java.util.stream.*;

import malloc.master.logic.*;
import malloc.master.logic.decks.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.*;
import net.dv8tion.jda.api.interactions.components.buttons.*;
import net.dv8tion.jda.api.interactions.components.selections.*;

public final class Embeds {
    private Embeds() {}

    public static MessageEmbed gameNotFound(final String code) {
        return new EmbedBuilder()
            .setTitle("Game room not found")
            .setColor(Color.RED)
            .setDescription("No game with code " + code + " found.")
            .build();
    }

    public static MessageEmbed alreadyInAGame() {
        return new EmbedBuilder()
            .setTitle("Already in a game")
            .setColor(Color.RED)
            .setDescription("You are already in a game.")
            .build();
    }

    public static MessageEmbed gameInitialized() {
        return new EmbedBuilder()
            .setTitle("Game Initialized")
            .setColor(Color.GREEN)
            .setDescription("Game is starting shortly")
            .build();
    }

    public static MessageEmbed gameStopped(final Color color, final String code) {
        return new EmbedBuilder()
            .setTitle("Game stopped")
            .setColor(color)
            .setDescription("Game room " + code + " has been stopped by the host.")
            .setFooter("Join a new one using /join or create one using /start")
            .build();
    }

    public static MessageEmbed gameLeft(final String code) {
        return new EmbedBuilder()
            .setTitle("Game left")
            .setColor(Color.GREEN)
            .setDescription("You left game room " + code + ".")
            .setFooter("Join a new one using /join or create one using /start")
            .build();
    }

    public static MessageEmbed settings(final Room room) {
        return new EmbedBuilder()
            .setTitle("Game Room " + room.code())
            .setColor(Color.CYAN)
            .setDescription("General Settings")
            .addField("Players", room.players().stream().map(IMentionable::getAsMention).collect(Collectors.joining("\n:black_large_square: ", ":crown: ", "")), false)
            .addField("Board", "Current board is: " + room.board().name(), false)
            .setImage("attachment://board.png")
            .build();
    }

    public static List<LayoutComponent> settingsComponents(final Room room) {
        return List.of(
            ActionRow.of(
                StringSelectMenu.create(room.makeCommand("changeBoard"))
                    .addOptions(room.boardNames().entrySet().stream().map(e -> SelectOption.of(e.getValue(), e.getKey())).toList())
                    .setDefaultValues(room.boardId())
                    .build()
            ),
            ActionRow.of(
                Button.danger(room.makeCommand("stop"), "Stop"),
                Button.success(room.makeCommand("start"), "Start").withDisabled(room.players().size() == 1)
            )
        );
    }

    public static MessageEmbed player(final Room room) {
        return new EmbedBuilder()
            .setTitle("Game Room " + room.code())
            .setColor(Color.CYAN)
            .setDescription("Prepare your ink and paper.")
            .addField("Players", room.players().stream().map(IMentionable::getAsMention).collect(Collectors.joining("\n:black_large_square: ", ":crown: ", "")), false)
            .addField("Board", "Current board is: " + room.board().name(), false)
            .setImage("attachment://board.png")
            .build();
    }

    public static List<LayoutComponent> playerComponents(final Room room) {
        return List.of(
            ActionRow.of(
                Button.danger(room.makeCommand("leave"), "Leave")
            )
        );
    }

    public static MessageEmbed move(final Game game) {
        var color = switch (game.season()) {
            case 1 -> Color.PINK;
            case 2 -> Color.GREEN;
            case 3 -> Color.ORANGE;
            case 4 -> Color.WHITE;
            default -> throw new UnsupportedOperationException("Unreachable");
        };
        var goals = game.activeGoalNames();

        return new EmbedBuilder()
            .setTitle("Current cards: " + game.currentCards().stream().map(Deck.Card::name).collect(Collectors.joining(" + ")) + "(" + game.currentCard().turns() + " turns)")
            .setColor(color)
            .setDescription("Active goals: " + goals.get(0) + " and " + goals.get(1))
            .setImage("attachment://board.png")
            .build();
    }

    public static List<LayoutComponent> moveComponents(final Game game, final Game.Player player) {
        var components = new ArrayList<LayoutComponent>();
        if (game.currentCard().pieces().size() > 1) {
            components.add(ActionRow.of(
                StringSelectMenu.create(game.makeCommand("selectPiece"))
                    .addOptions(game.currentCard().pieces().stream().map(p -> SelectOption.of(p.name(), p.id())).toList())
                    .setDefaultValues(player.currentPiece().id())
                    .build()
            ));
        }
        components.addAll(List.of(
            ActionRow.of(
                Button.secondary(game.makeCommand("flipHorizontal"), Emoji.fromFormatted("↔")),
                Button.secondary(game.makeCommand("moveUp"), Emoji.fromFormatted("⬆")),
                Button.secondary(game.makeCommand("rotateClockwise"), Emoji.fromFormatted("↩"))
            ),
            ActionRow.of(
                Button.secondary(game.makeCommand("moveLeft"), Emoji.fromFormatted("⬅")),
                Button.success(game.makeCommand("placePiece"), Emoji.fromFormatted("✅")).withDisabled(!player.isPiecePlaceable()),
                Button.secondary(game.makeCommand("moveRight"), Emoji.fromFormatted("➡"))
            ),
            ActionRow.of(
                Button.secondary(game.makeCommand("flipVertical"), Emoji.fromFormatted("↕")),
                Button.secondary(game.makeCommand("moveDown"), Emoji.fromFormatted("⬇")),
                Button.secondary(game.makeCommand("rotateCounterclockwise"), Emoji.fromFormatted("↪"))
            )
        ));
        return components;
    }

    public static MessageEmbed piecePlaced(final Game game, final Game.Player player) {
        return new EmbedBuilder()
            .setTitle("Piece placed")
            .setColor(Color.GREEN)
            .setDescription("Waiting for " + game.countPlayersThinking() + " players to finish.")
            .addField("Your board", "Coins: " + player.currentBoard().coins() + "/10", false)
            .setImage("attachment://board.png")
            .build();
    }

    public static MessageEmbed end(final int place, final String leaderboard) {
        var placeString = switch (place) {
            case 1 -> "first";
            case 2 -> "second";
            case 3 -> "third";
            case 4 -> "fourth";
            case 5 -> "fifth";
            case 6 -> "sixth";
            case 7 -> "seventh";
            case 8 -> "eight";
            case 9 -> "ninth";
            case 10 -> "tenth";
            default -> "outside the top 10";
        };

        return new EmbedBuilder()
            .setTitle("Game has ended")
            .setColor(Color.GREEN)
            .setDescription("You finished " + placeString + "!")
            .setThumbnail("attachment://board.png")
            .addField("Leaderboard", leaderboard, false)
            .addField("Winner's board", "", false)
            .setImage("attachment://winner_board.png")
            .build();
    }

    public static MessageEmbed seasonEnded(final Game game, final Game.Player player, final int seasonScore) {
        var color = switch (game.season()) {
            case 1 -> Color.PINK;
            case 2 -> Color.GREEN;
            case 3 -> Color.ORANGE;
            case 4 -> Color.WHITE;
            default -> throw new UnsupportedOperationException("Unreachable");
        };
        return new EmbedBuilder()
            .setTitle("Season has ended")
            .setColor(color)
            .addField("You scored " + seasonScore + " points this season!", "Your total score is " + player.score() + " points.", false)
            .addField("Your board", "Coins: " + player.currentBoard().coins() + "/10", false)
            .setImage("attachment://board.png")
            .build();
    }
}
