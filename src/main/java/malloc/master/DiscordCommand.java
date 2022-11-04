package malloc.master;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

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
