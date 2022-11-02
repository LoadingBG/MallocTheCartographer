package malloc.discord;

import net.dv8tion.jda.api.events.interaction.command.*;
import net.dv8tion.jda.api.hooks.*;
import net.dv8tion.jda.api.interactions.commands.build.*;

public abstract class DiscordCommand extends ListenerAdapter {
    public abstract CommandData data();

    public abstract void handle(final SlashCommandInteractionEvent event);

    @Override
    public void onSlashCommandInteraction(final SlashCommandInteractionEvent event) {
        if (event.getName().equals(data().getName())) {
            handle(event);
        }
    }
}
