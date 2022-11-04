package malloc.master;

import java.awt.Color;
import java.util.*;
import java.util.concurrent.*;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.events.interaction.component.*;
import net.dv8tion.jda.api.hooks.*;
import net.dv8tion.jda.api.utils.*;

public final class RoomSettingsListener extends ListenerAdapter {
    private static final Map<String, GameRoom> ROOMS = new ConcurrentHashMap<>();

    private static final Random RNG = new Random();

    private static String generateCode() {
        while (true) {
            var codeBuilder = new StringBuilder();
            for (var i = 0; i < 6; ++i) {
                codeBuilder.append(Character.toString(RNG.nextInt('a', 'z' + 1)));
            }
            var code = codeBuilder.toString();
            if (!ROOMS.containsKey(code)) {
                return code;
            }
        }
    }

    public static void setupRoom(SlashCommandInteractionEvent event) {
        var code = generateCode();
        var room = new GameRoom(code, event.getMember(), event.getHook());
        ROOMS.put(code, room);

        event.getInteraction()
            .replyFiles(FileUpload.fromData(room.getBoardImage(), "board.png"))
            .addEmbeds(room.generalSettingsEmbed())
            .setComponents(room.generalSettingsComponents())
            .setEphemeral(true)
            .queue();
    }

    public static void joinPlayer(Member member, String code, SlashCommandInteractionEvent event) {
        var room = ROOMS.get(code);
        if (room == null) {
            event.getInteraction().replyEmbeds(
                new EmbedBuilder()
                    .setTitle("Game room not found")
                    .setColor(Color.RED)
                    .setDescription("No game with code " + code + " found.")
                    .build()
            ).setEphemeral(true).queue();
            return;
        }

        if (ROOMS.values().stream().anyMatch(r -> r.hasPlayer(member))) {
            event.getInteraction().replyEmbeds(
                new EmbedBuilder()
                    .setTitle("Already in a game")
                    .setColor(Color.RED)
                    .setDescription("You are already in a game.")
                    .build()
            ).setEphemeral(true).queue();
            return;
        }

        room.addPlayer(member, event.getHook());

        event.getInteraction()
            .replyFiles(FileUpload.fromData(room.getBoardImage(), "board.png"))
            .setEmbeds(room.playerEmbed())
            .setComponents(room.playerComponents())
            .setEphemeral(true)
            .queue();

        room.updateSettingsMenu();
    }

    public static void removeRoom(String code) {
        ROOMS.remove(code);
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        var parts = event.getComponentId().split(":");
        ROOMS.get(parts[0]).execute(parts[1], event);
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        var parts = event.getComponentId().split(":");
        ROOMS.get(parts[0]).execute(parts[1], event);
    }
}
