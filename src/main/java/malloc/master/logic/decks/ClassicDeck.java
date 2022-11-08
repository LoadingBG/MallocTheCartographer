package malloc.master.logic.decks;

import java.util.*;

import malloc.game.*;

public class ClassicDeck implements Deck {
    private static final List<NormalCard> CARDS = List.of(
        new NormalCard("Rift Lands", 0, List.of(
            Piece.RIFT_LANDS_FOREST,
            Piece.RIFT_LANDS_VILLAGE,
            Piece.RIFT_LANDS_FARM,
            Piece.RIFT_LANDS_WATER,
            Piece.RIFT_LANDS_MONSTER
        )),
        new NormalCard("Treetop Village", 2, List.of(
            Piece.TREETOP_VILLAGE_FOREST,
            Piece.TREETOP_VILLAGE_VILLAGE
        )),
        new NormalCard("Farmland", 1, List.of(
            Piece.FARMLAND_SMALL,
            Piece.FARMLAND_BIG
        )),
        new NormalCard("Forgotten Forest", 1, List.of(
            Piece.FORGOTTEN_FOREST_SMALL,
            Piece.FORGOTTEN_FOREST_BIG
        )),
        new NormalCard("Orchard", 2, List.of(
            Piece.ORCHARD_FOREST,
            Piece.ORCHARD_FARM
        )),
        new NormalCard("Hamlet", 1, List.of(
            Piece.HAMLET_SMALL,
            Piece.HAMLET_BIG
        )),
        new NormalCard("Homestead", 2, List.of(
            Piece.HOMESTEAD_VILLAGE,
            Piece.HOMESTEAD_FARM
        )),
        new NormalCard("Hinterland Stream", 2, List.of(
            Piece.HINTERLAND_STREAM_FARM,
            Piece.HINTERLAND_STREAM_WATER
        )),
        new NormalCard("Great River", 1, List.of(
            Piece.GREAT_RIVER_SMALL,
            Piece.GREAT_RIVER_BIG
        )),
        new NormalCard("Marshlands", 2, List.of(
            Piece.MARSHLANDS_FOREST,
            Piece.MARSHLANDS_WATER
        )),
        new NormalCard("Fishing Village", 2, List.of(
            Piece.FISHING_VILLAGE_VILLAGE,
            Piece.FISHING_VILLAGE_WATER
        ))
    );

    // TODO: merge with normal cards
    private static final List<ModifierCard> RUINS = List.of(
        new ModifierCard("Temple Ruins"),
        new ModifierCard("Outpost Ruins")
    );

    // TODO: additional things from https://www.google.com/url?sa=i&url=https%3A%2F%2Fwhatsericplaying.com%2F2022%2F01%2F03%2Fcartographers-heroes%2F&psig=AOvVaw1Ax-qQ9Buc65D8_GvlspJG&ust=1667641897591000&source=images&cd=vfe&ved=0CAoQjRxqFwoTCNj7zamglPsCFQAAAAAdAAAAABAR
    private final List<EnemyCard> enemies = new ArrayList<>(List.of(
        new EnemyCard("Gorgon Gaze", Piece.GORGON_GAZE, true),
        new EnemyCard("Giant Troll Ravage", Piece.GIANT_TROLL_RAVAGE, true),
        new EnemyCard("Zombie Plague", Piece.ZOMBIE_PLAGUE, false),
        new EnemyCard("Dragon Inferno", Piece.DRAGON_INFERNO, false)
    ));

    private final List<Card> cards = new ArrayList<>();

    private final List<Card> dealtModifiers = new ArrayList<>();
    private final List<Card> dealtEnemies = new ArrayList<>();
    private Card dealtCard;

    @Override
    public void reset(boolean addEnemy) {
        if (addEnemy) {
            Collections.shuffle(enemies);
            dealtEnemies.add(enemies.remove(0));
        }
        cards.clear();
        cards.addAll(CARDS);
        cards.addAll(RUINS);
        cards.addAll(dealtEnemies);
        Collections.shuffle(cards);
    }

    @Override
    public void deal() {
        if (!(currentCard() instanceof EnemyCard)) {
            dealtModifiers.clear();
        }

        var card = cards.remove(0);
        while (card instanceof ModifierCard) {
            dealtModifiers.add(card);
            card = cards.remove(0);
        }
        dealtCard = card;
    }

    public List<Card> dealtCards() {
        var dealtCards = new ArrayList<Card>();
        dealtCards.add(dealtCard);
        if (!(dealtCard instanceof EnemyCard)) {
            dealtCards.addAll(dealtModifiers);
        }
        return dealtCards;
    }

    public Card currentCard() {
        return dealtCard;
    }

    public boolean withRuins() {
        return !(dealtCard instanceof EnemyCard) && !dealtModifiers.isEmpty();
    }
}
