package malloc;

import java.io.*;
import java.util.*;

import malloc.master.*;
import malloc.master.logic.GameManager;
import malloc.master.logic.RoomManager;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.utils.cache.*;

// https://discord.com/api/oauth2/authorize?client_id=1035622851899830352&permissions=2147483648&scope=bot%20applications.commands
public class Malloc {
    public static final RoomManager ROOM_MANAGER = new RoomManager();
    public static final GameManager GAME_MANAGER = new GameManager();

    public static final DiscordCommand[] DISCORD_COMMANDS = {
        new HelpCommand(),
        new StartCommand(),
        new JoinCommand(),
        //new StopCommand(),
    };

    public static void main(String[] args) {
        try (var reader = new BufferedReader(new InputStreamReader(Malloc.class.getResourceAsStream("/token.txt")))) {
            JDA jda = JDABuilder.createDefault(reader.lines().findFirst().get())
                .disableCache(Arrays.asList(CacheFlag.values()))
                .setBulkDeleteSplittingEnabled(false)
                .setActivity(Activity.competing("the mapping of the northern lands"))
                .addEventListeners(DISCORD_COMMANDS)
                .addEventListeners(new GuildJoinListener(), new RoomListener(), new GameListener())
                //.addEventListeners(new GuildJoinListener(), new GameHandler())
                .build();
            jda.awaitReady();
            Runtime.getRuntime().addShutdownHook(new Thread(jda::shutdownNow));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}