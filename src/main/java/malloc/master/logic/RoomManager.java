package malloc.master.logic;

import java.util.*;
import java.util.concurrent.*;

import malloc.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.component.*;
import net.dv8tion.jda.api.interactions.*;

public final class RoomManager {
    private static final Random RNG = new Random();

    private final Map<String, Room> rooms = new ConcurrentHashMap<>();

    private String generateCode() {
        while (true) {
            var codeBuilder = new StringBuilder();
            for (var i = 0; i < 6; ++i) {
                codeBuilder.append(Character.toString(RNG.nextInt('a', 'z' + 1)));
            }
            var code = codeBuilder.toString();
            if (!rooms.containsKey(code) && !Malloc.GAME_MANAGER.hasGame(code)) {
                return code;
            }
        }
    }

    public Room createRoom(final Member member, final InteractionHook hook) {
        var code = generateCode();
        var room = new Room(code, member, hook);
        rooms.put(code, room);
        return room;
    }

    public Map.Entry<Boolean, Optional<Room>> joinPlayer(String code, Member member, InteractionHook hook) {
        var room = rooms.get(code);
        if (room == null) {
            return Map.entry(false, Optional.empty());
        }
        if (rooms.values().stream().anyMatch(r -> r.hasPlayer(member))) {
            return Map.entry(false, Optional.of(room));
        }

        room.addPlayer(member, hook);

        return Map.entry(true, Optional.of(room));
    }

    public Room execute(final String code, final String command, final GenericComponentInteractionCreateEvent event) {
        var room = rooms.get(code);

        switch (command) {
            case "changeBoard" -> room.changeBoard(((StringSelectInteractionEvent) event).getValues().get(0));
            case "stop" -> rooms.remove(code);
            case "leave" -> room.removePlayer(event.getMember());
            case "start" -> Malloc.GAME_MANAGER.setupGame(room);
        }

        return room;
    }

    public boolean hasPlayer(final Member member) {
        return rooms.values().stream().anyMatch(r -> r.hasPlayer(member));
    }
}
