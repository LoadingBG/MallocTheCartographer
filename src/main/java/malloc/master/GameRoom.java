package malloc.master;

import java.awt.Color;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import malloc.game.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.*;
import net.dv8tion.jda.api.interactions.*;
import net.dv8tion.jda.api.interactions.components.*;
import net.dv8tion.jda.api.interactions.components.buttons.*;
import net.dv8tion.jda.api.interactions.components.selections.*;
import net.dv8tion.jda.api.utils.*;

public final class GameRoom {
    private final String code;
    private final List<Map.Entry<Member, InteractionHook>> players;

    private Board board = Board.plain();
    private String boardId = "plain";

    public byte[] getBoardImage() {
        return board.toImage();
    }

    public GameRoom(final String code, final Member host, final InteractionHook hook) {
        this.code = code;
        players = new ArrayList<>();
        players.add(Map.entry(host, hook));
    }

    public boolean hasPlayer(final Member member) {
        return players.stream().map(Map.Entry::getKey).anyMatch(Predicate.isEqual(member));
    }

    public void addPlayer(final Member member, final InteractionHook hook) {
        players.add(Map.entry(member, hook));
    }

    public MessageEmbed generalSettingsEmbed() {
        return new EmbedBuilder()
            .setTitle("Game Room " + code)
            .setColor(Color.CYAN)
            .setDescription("General Settings")
            .addField("Players", players.stream().map(e -> e.getKey().getAsMention()).collect(Collectors.joining("\n:black_large_square: ", ":crown: ", "")), false)
            .addField("Board", "Current board is: " + board.name(), false)
            .setImage("attachment://board.png")
            .build();
    }

    public List<LayoutComponent> generalSettingsComponents() {
        return List.of(
            ActionRow.of(
                StringSelectMenu.create(code + ":changeBoard")
                    .addOption("Plain", "plain")
                    .addOption("Ravine in Middle", "ravineInMiddle")
                    .setDefaultValues(boardId)
                    .build()
            ),
            ActionRow.of(
                Button.danger(code + ":stop", "Stop"),
                Button.success(code + ":start", "Start")
            )
        );
    }

    public MessageEmbed playerEmbed() {
        return new EmbedBuilder()
            .setTitle("Game Room " + code)
            .setColor(Color.CYAN)
            .setDescription("Prepare your ink and paper.")
            .addField("Players", players.stream().map(e -> e.getKey().getAsMention()).collect(Collectors.joining("\n:black_large_square: ", ":crown: ", "")), false)
            .addField("Board", "Current board is: " + board.name(), false)
            .setImage("attachment://board.png")
            .build();
    }

    public List<LayoutComponent> playerComponents() {
        return List.of(
            ActionRow.of(
                Button.danger(code + ":leave", "Leave")
            )
        );
    }

    public void execute(final String command, final GenericComponentInteractionCreateEvent event) {
        switch (command) {
            case "changeBoard" -> {
                boardId = ((StringSelectInteractionEvent) event).getValues().get(0);
                board = switch (boardId) {
                    case "plain" -> Board.plain();
                    case "ravineInMiddle" -> Board.ravineMiddle();
                    default -> throw new UnsupportedOperationException("unreachable");
                };

                event.getInteraction()
                    .editMessageAttachments(FileUpload.fromData(board.toImage(), "board.png"))
                    .setEmbeds(generalSettingsEmbed())
                    .setComponents(generalSettingsComponents())
                    .queue();

                updatePlayerMenus();
            }
            case "leave" -> {
                players.removeIf(e -> e.getKey().equals(event.getMember()));
                ((ButtonInteractionEvent) event).getInteraction().editMessageAttachments().setEmbeds(
                    new EmbedBuilder()
                        .setTitle("Game left")
                        .setColor(Color.GREEN)
                        .addField("You left game room " + code + ".", "Join a new one using /join or create one using /start", false)
                        .build()
                ).setComponents().queue();

                updateSettingsMenu();
            }
            case "stop" -> {
                RoomSettingsListener.removeRoom(code);

                players.get(0)
                    .getValue()
                    .editOriginalAttachments()
                    .setEmbeds(
                        new EmbedBuilder()
                            .setTitle("Game stopped")
                            .setColor(Color.GREEN)
                            .addField("Game room " + code + " is stopped.", "Join a new one using /join or create one using /start", false)
                            .build()
                    )
                    .setComponents()
                    .queue();

                players.stream()
                    .skip(1)
                    .map(Map.Entry::getValue)
                    .forEach(hook -> hook.editOriginalAttachments()
                        .setEmbeds(
                            new EmbedBuilder()
                                .setTitle("Game stopped")
                                .setColor(Color.RED)
                                .addField("Game room " + code + " has been stopped by the host.", "Join a new one using /join or create one using /start", false)
                                .build()
                        )
                        .setComponents()
                        .queue());
            }
            case "start" -> {
                GameListener.setupGame(code, players, board);

                event.getInteraction()
                    .editMessageAttachments()
                    .setEmbeds(
                        new EmbedBuilder()
                            .setTitle("Game Initialized")
                            .setColor(Color.GREEN)
                            .setDescription("Game is starting shortly")
                            .build()
                    )
                    .setComponents()
                    .queue();

                players.stream()
                    .skip(1)
                    .map(Map.Entry::getValue)
                    .forEach(hook -> hook.editOriginalAttachments()
                        .setEmbeds(
                            new EmbedBuilder()
                                .setTitle("Game Initialized")
                                .setColor(Color.GREEN)
                                .setDescription("Game is starting shortly")
                                .build()
                        )
                        .setComponents()
                        .queue());
            }
        }
    }

    public void updateSettingsMenu() {
        players.get(0)
            .getValue()
            .editOriginalAttachments(FileUpload.fromData(board.toImage(), "board.png"))
            .setEmbeds(generalSettingsEmbed())
            .setComponents(generalSettingsComponents())
            .queue();
    }

    public void updatePlayerMenus() {
        players.stream()
            .skip(1)
            .map(Map.Entry::getValue)
            .forEach(hook -> hook.editOriginalAttachments(FileUpload.fromData(board.toImage(), "board.png"))
                .setEmbeds(playerEmbed())
                .setComponents(playerComponents())
                .queue());
    }
}
