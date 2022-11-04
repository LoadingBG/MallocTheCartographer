package malloc.master;

import java.util.Objects;

import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.interactions.commands.*;
import net.dv8tion.jda.api.interactions.commands.build.*;

public final class JoinCommand extends DiscordCommand {
    @Override
    public CommandData data() {
        return Commands.slash("join", "Join a game room")
            .addOption(OptionType.STRING, "code", "The code of the game room", true);
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        RoomSettingsListener.joinPlayer(event.getMember(), Objects.requireNonNull(event.getOption("code")).getAsString(), event);
    }
}
