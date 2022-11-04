package malloc.master;

import java.util.*;

import malloc.game.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.*;

public final class Game {
    private static final List<Map.Entry<Goal, String>> FOREST_GOALS = List.of(
        Map.entry(Goal.SENTINEL_WOOD, "Sentinel Wood"),
        Map.entry(Goal.TREETOWER, "Treetower"),
        Map.entry(Goal.GREENBOUGH, "Greenbough"),
        Map.entry(Goal.STONESIDE_FOREST, "Stoneside Forest")
    );
    private static final List<Map.Entry<Goal, String>> WATER_GOALS = List.of(
        Map.entry(Goal.CANAL_LAKE, "Canal Lake"),
        Map.entry(Goal.THE_GOLDEN_GRANARY, "The Golden Granary"),
        Map.entry(Goal.MAGES_VALLEY, "Mages Valley"),
        Map.entry(Goal.SHORESIDE_EXPANSE, "Shoreside Expanse")
    );
    private static final List<Map.Entry<Goal, String>> VILLAGE_GOALS = List.of(
        Map.entry(Goal.WILDHOLDS, "Wildholds"),
        Map.entry(Goal.GREENGOLD_PLAINS, "Greengold Plains"),
        Map.entry(Goal.GREAT_CITY, "Great City"),
        Map.entry(Goal.SHIELDGATE, "Shieldgate")
    );
    private static final List<Map.Entry<Goal, String>> GENERIC_GOALS = List.of(
        Map.entry(Goal.BORDERLANDS, "Borderlands"),
        Map.entry(Goal.THE_BROKEN_ROAD, "The Broken Road"),
        Map.entry(Goal.LOST_BARONY, "Lost Barony"),
        Map.entry(Goal.THE_CAULDRONS, "The Cauldrons")
    );
    private static final List<Card> CARDS = List.of(
        new Card(
            "Rift Lands",
            0,
            Piece.RIFT_LANDS_FOREST,
            Piece.RIFT_LANDS_VILLAGE,
            Piece.RIFT_LANDS_FARM,
            Piece.RIFT_LANDS_WATER,
            Piece.RIFT_LANDS_MONSTER
        ),
        new Card(
            "Treetop Village",
            2,
            Piece.TREETOP_VILLAGE_FOREST,
            Piece.TREETOP_VILLAGE_VILLAGE
        ),
        new Card(
            "Farmland",
            1,
            Piece.FARMLAND_SMALL,
            Piece.FARMLAND_BIG
        ),
        new Card(
            "Forgotten Forest",
            1,
            Piece.FORGOTTEN_FOREST_SMALL,
            Piece.FORGOTTEN_FOREST_BIG
        ),
        new Card(
            "Orchard",
            2,
            Piece.ORCHARD_FOREST,
            Piece.ORCHARD_FARM
        ),
        new Card(
            "Hamlet",
            1,
            Piece.HAMLET_SMALL,
            Piece.HAMLET_BIG
        ),
        new Card(
            "Homestead",
            2,
            Piece.HOMESTEAD_VILLAGE,
            Piece.HOMESTEAD_FARM
        ),
        new Card(
            "Hinterland Stream",
            2,
            Piece.HINTERLAND_STREAM_FARM,
            Piece.HINTERLAND_STREAM_WATER
        ),
        new Card(
            "Great River",
            1,
            Piece.GREAT_RIVER_SMALL,
            Piece.GREAT_RIVER_BIG
        ),
        new Card(
            "Marshlands",
            2,
            Piece.MARSHLANDS_FOREST,
            Piece.MARSHLANDS_WATER
        ),
        new Card(
            "Fishing Village",
            2,
            Piece.FISHING_VILLAGE_VILLAGE,
            Piece.FISHING_VILLAGE_WATER
        )
    );
    private static final Random RNG = new Random();

    private final List<Player> players;
    private final Map.Entry<Goal, String> goalA;
    private final Map.Entry<Goal, String> goalB;
    private final Map.Entry<Goal, String> goalC;
    private final Map.Entry<Goal, String> goalD;
    // TODO: additional things from https://www.google.com/url?sa=i&url=https%3A%2F%2Fwhatsericplaying.com%2F2022%2F01%2F03%2Fcartographers-heroes%2F&psig=AOvVaw1Ax-qQ9Buc65D8_GvlspJG&ust=1667641897591000&source=images&cd=vfe&ved=0CAoQjRxqFwoTCNj7zamglPsCFQAAAAAdAAAAABAR
    private final List<Card> enemies = new ArrayList<>(List.of(
        new Card("Gorgon Gaze", 0, Piece.GORGON_GAZE),
        new Card("Giant Troll Ravage", 0, Piece.GIANT_TROLL_RAVAGE),
        new Card("Zombie Plague", 0, Piece.ZOMBIE_PLAGUE),
        new Card("Dragon Inferno", 0, Piece.DRAGON_INFERNO)
    ));
    private final List<Card> cards;
    private int season = 0;
    private int turnsLeft = 0;

    public Game(final List<Map.Entry<Member, InteractionHook>> players, final Board board) {
        this.players = players.stream().map(p -> new Player(p.getKey(), p.getValue(), board.copy())).toList();
        var goals = new ArrayList<>(List.of(
            FOREST_GOALS.get(RNG.nextInt(FOREST_GOALS.size())),
            WATER_GOALS.get(RNG.nextInt(WATER_GOALS.size())),
            VILLAGE_GOALS.get(RNG.nextInt(VILLAGE_GOALS.size())),
            GENERIC_GOALS.get(RNG.nextInt(GENERIC_GOALS.size()))
        ));
        Collections.shuffle(goals);
        goalA = goals.get(0);
        goalB = goals.get(1);
        goalC = goals.get(2);
        goalD = goals.get(3);
        cards = new ArrayList<>(CARDS);
        Collections.shuffle(cards);
    }

    public void startNextSeason() {
        ++season;
        turnsLeft = switch (season) {
            case 1, 2 -> 8;
            case 3 -> 7;
            case 4 -> 6;
            default -> throw new UnsupportedOperationException("Unreachable");
        };
    }

    private record Player(Member member, InteractionHook hook, Board board) {}

    private record Card(String name, int turns, Piece... pieces) {}
}
