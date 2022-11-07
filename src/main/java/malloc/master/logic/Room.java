package malloc.master.logic;

import java.util.*;
import java.util.function.*;

import malloc.game.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.*;

public final class Room {
    private final String code;
    private final List<Map.Entry<Member, InteractionHook>> players;

    public static final Map<String, Board> BOARDS = Map.of(
        "plain", Board.plain(),
        "ravineInMiddle", Board.ravineMiddle()
    );

    private String boardId = "plain";
    private Board board = BOARDS.get(boardId);

    public byte[] getBoardImage() {
        return board.toImageBytes();
    }

    public Room(final String code, final Member host, final InteractionHook hook) {
        this.code = code;
        players = new ArrayList<>();
        players.add(Map.entry(host, hook));
    }

    public String code() {
        return code;
    }

    public List<Member> players() {
        return players.stream().map(Map.Entry::getKey).toList();
    }

    public Board board() {
        return board;
    }

    public String boardId() {
        return boardId;
    }

    public InteractionHook hostHook() {
        return players.get(0).getValue();
    }

    public List<InteractionHook> playerHooks() {
        return players.stream().skip(1).map(Map.Entry::getValue).toList();
    }

    public Map<String, String> boardNames() {
        return Map.of(
            "plain", "Plain",
            "ravineInMiddle", "Ravine in Middle"
        );
    }

    public List<Map.Entry<Member, InteractionHook>> playerInfo() {
        return players;
    }

    public boolean hasPlayer(final Member member) {
        return players.stream().map(Map.Entry::getKey).anyMatch(Predicate.isEqual(member));
    }

    public void addPlayer(final Member member, final InteractionHook hook) {
        players.add(Map.entry(member, hook));
    }

    public String makeCommand(final String command) {
        return "room:" + code + ":" + command;
    }

    public void removePlayer(final Member member) {
        players.removeIf(e -> e.getKey().equals(member));
    }

    public void changeBoard(final String boardId) {
        this.boardId = boardId;
        board = BOARDS.get(boardId);
    }
}
