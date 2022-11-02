package malloc.discord;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.interactions.commands.build.*;

public final class HelpCommand extends DiscordCommand {
    @Override
    public CommandData data() {
        return Commands.slash("help", "Sends help");
    }

    @Override
    public void handle(final SlashCommandInteractionEvent event) {
        event.getInteraction().replyEmbeds(
            new EmbedBuilder()
                .addField("Help", "This is a sample help", false)
                .build())
            .setEphemeral(true)
            .queue();
    }
}
