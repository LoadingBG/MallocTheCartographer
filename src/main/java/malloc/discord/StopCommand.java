package malloc.discord;

import java.awt.*;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.interactions.commands.build.*;

public class StopCommand extends DiscordCommand {
    @Override
    public CommandData data() {
        return Commands.slash("stop", "Stops the current game");
    }

    @Override
    public void handle(SlashCommandInteractionEvent event) {
        GameHandler.reset();
        event.getInteraction().replyEmbeds(
            new EmbedBuilder()
                .setTitle("Game stopped")
                .setColor(Color.RED)
                .setDescription("Start a new game using /start")
                .build()
        ).queue();
    }
}
