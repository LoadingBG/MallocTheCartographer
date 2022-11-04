package malloc.master;

import java.util.*;

import malloc.*;
import net.dv8tion.jda.api.events.guild.*;
import net.dv8tion.jda.api.hooks.*;

public final class GuildJoinListener extends ListenerAdapter {
    @Override
    public void onGuildJoin(final GuildJoinEvent event) {
        event.getGuild()
            .updateCommands()
            .addCommands(Arrays.stream(Malloc.DISCORD_COMMANDS).map(DiscordCommand::data).toList())
            .complete();
    }
}
