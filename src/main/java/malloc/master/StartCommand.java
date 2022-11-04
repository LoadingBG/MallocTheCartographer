package malloc.master;

import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.interactions.commands.build.*;

public final class StartCommand extends DiscordCommand {

    @Override
    public CommandData data() {
        return Commands.slash("start", "Creates a game room");
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        RoomSettingsListener.setupRoom(event);
    }
}
