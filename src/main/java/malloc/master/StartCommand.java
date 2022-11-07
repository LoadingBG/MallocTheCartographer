package malloc.master;

import malloc.*;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.api.utils.*;

public final class StartCommand extends DiscordCommand {

    @Override
    public CommandData data() {
        return Commands.slash("start", "Creates a game room");
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        if (Malloc.ROOM_MANAGER.hasPlayer(event.getMember()) || Malloc.GAME_MANAGER.hasPlayer(event.getMember())) {
            event.getInteraction()
                .replyEmbeds(Embeds.alreadyInAGame())
                .setEphemeral(true)
                .queue();

            return;
        }

        var room = Malloc.ROOM_MANAGER.createRoom(event.getMember(), event.getHook());
        event.getInteraction()
            .replyFiles(FileUpload.fromData(room.getBoardImage(), "board.png"))
            .addEmbeds(Embeds.settings(room))
            .setComponents(Embeds.settingsComponents(room))
            .setEphemeral(true)
            .queue();
    }
}
