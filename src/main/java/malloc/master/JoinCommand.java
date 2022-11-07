package malloc.master;

import java.awt.Color;
import java.util.*;

import malloc.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.interactions.commands.*;
import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.api.utils.FileUpload;

public final class JoinCommand extends DiscordCommand {
    @Override
    public CommandData data() {
        return Commands.slash("join", "Join a game room")
            .addOption(OptionType.STRING, "code", "The code of the game room", true);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        var code = Objects.requireNonNull(event.getOption("code")).getAsString();
        var result = Malloc.ROOM_MANAGER.joinPlayer(code, event.getMember(), event.getHook());

        if (result.getValue().isEmpty()) {
            event.getInteraction()
                .replyEmbeds(Embeds.gameNotFound(code))
                .setEphemeral(true)
                .queue();
            return;
        }

        if (!result.getKey()) {
            event.getInteraction()
                .replyEmbeds(Embeds.alreadyInAGame())
                .setEphemeral(true)
                .queue();
            return;
        }

        var room = result.getValue().get();
        event.getInteraction()
            .replyFiles(FileUpload.fromData(room.getBoardImage(), "board.png"))
            .setEmbeds(Embeds.player(room))
            .setComponents(Embeds.playerComponents(room))
            .setEphemeral(true)
            .queue();

        room.hostHook()
            .editOriginalAttachments(FileUpload.fromData(room.board().toImageBytes(), "board.png"))
            .setEmbeds(Embeds.settings(room))
            .setComponents(Embeds.settingsComponents(room))
            .queue();
    }
}
