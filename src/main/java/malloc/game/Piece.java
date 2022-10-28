package malloc.game;

import java.util.*;

public record Piece(Cell[][] cells, Piece replacement, boolean hasCoin) {
    public static final Piece RIFT_LANDS_FOREST = new Piece(new Cell[][] {
    { Cell.forest() }
}, null, false);
    public static final Piece RIFT_LANDS_VILLAGE = new Piece(new Cell[][] {
        { Cell.village() }
    }, null, false);
    public static final Piece RIFT_LANDS_FARM = new Piece(new Cell[][] {
        { Cell.farm() }
    }, null, false);
    public static final Piece RIFT_LANDS_WATER = new Piece(new Cell[][] {
        { Cell.water() }
    }, null, false);
    public static final Piece RIFT_LANDS_MONSTER = new Piece(new Cell[][] {
        { Cell.monster() }
    }, null, false);

    public static final Piece TREETOP_VILLAGE_FOREST = new Piece(new Cell[][] {
        { null,          null,          Cell.forest(), Cell.forest() },
        { Cell.forest(), Cell.forest(), Cell.forest(), null          },
    }, RIFT_LANDS_FOREST, false);
    public static final Piece TREETOP_VILLAGE_VILLAGE = new Piece(new Cell[][] {
        { null,           null,           Cell.village(), Cell.village() },
        { Cell.village(), Cell.village(), Cell.village(), null           },
    }, RIFT_LANDS_VILLAGE, false);

    public static final Piece FARMLAND_SMALL = new Piece(new Cell[][] {
        { Cell.farm() },
        { Cell.farm() },
    }, RIFT_LANDS_FARM, true);
    public static final Piece FARMLAND_BIG = new Piece(new Cell[][] {
        { null,        Cell.farm(), null        },
        { Cell.farm(), Cell.farm(), Cell.farm() },
        { null,        Cell.farm(), null        },
    }, RIFT_LANDS_FARM, false);

    public static final Piece FORGOTTEN_FOREST_SMALL = new Piece(new Cell[][] {
        { Cell.forest(), null          },
        { null,          Cell.forest() },
    }, RIFT_LANDS_FOREST, true);
    public static final Piece FORGOTTEN_FOREST_BIG = new Piece(new Cell[][] {
        { Cell.forest(), null          },
        { Cell.forest(), Cell.forest() },
        { null,          Cell.forest() },
    }, RIFT_LANDS_FOREST, false);

    public static final Piece ORCHARD_FOREST = new Piece(new Cell[][] {
        { Cell.forest(), Cell.forest(), Cell.forest() },
        { null,          null,          Cell.forest() },
    }, RIFT_LANDS_FOREST, false);
    public static final Piece ORCHARD_FARM = new Piece(new Cell[][] {
        { Cell.farm(), Cell.farm(), Cell.farm() },
        { null,        null,        Cell.farm() },
    }, RIFT_LANDS_FARM, false);

    public static final Piece HAMLET_SMALL = new Piece(new Cell[][] {
        { Cell.village(), null           },
        { Cell.village(), Cell.village() },
    }, RIFT_LANDS_VILLAGE, true);
    public static final Piece HAMLET_BIG = new Piece(new Cell[][] {
        { Cell.village(), Cell.village(), Cell.village() },
        { Cell.village(), Cell.village(), null           },
    }, RIFT_LANDS_VILLAGE, false);

    public static final Piece HOMESTEAD_VILLAGE = new Piece(new Cell[][] {
        { Cell.village(), null           },
        { Cell.village(), Cell.village() },
        { Cell.village(), null           },
    }, RIFT_LANDS_VILLAGE, false);
    public static final Piece HOMESTEAD_FARM = new Piece(new Cell[][] {
        { Cell.farm(), null        },
        { Cell.farm(), Cell.farm() },
        { Cell.farm(), null        },
    }, RIFT_LANDS_FARM, false);

    public static final Piece HINTERLAND_STREAM_FARM = new Piece(new Cell[][] {
        { Cell.farm(), Cell.farm(), Cell.farm() },
        { Cell.farm(), null,        null        },
        { Cell.farm(), null,        null        },
    }, RIFT_LANDS_FARM, false);
    public static final Piece HINTERLAND_STREAM_WATER = new Piece(new Cell[][] {
        { Cell.water(), Cell.water(), Cell.water() },
        { Cell.water(), null,         null         },
        { Cell.water(), null,         null         },
    }, RIFT_LANDS_WATER, false);

    public static final Piece GREAT_RIVER_SMALL = new Piece(new Cell[][] {
        { Cell.water() },
        { Cell.water() },
        { Cell.water() },
    }, RIFT_LANDS_WATER, true);
    public static final Piece GREAT_RIVER_BIG = new Piece(new Cell[][] {
        { null,         null,         Cell.water() },
        { null,         Cell.water(), Cell.water() },
        { Cell.water(), Cell.water(), null         },
    }, RIFT_LANDS_WATER, false);

    public static final Piece MARSHLANDS_FOREST = new Piece(new Cell[][] {
        { Cell.forest(), null,          null          },
        { Cell.forest(), Cell.forest(), Cell.forest() },
        { Cell.forest(), null,          null          },
    }, RIFT_LANDS_FOREST, false);
    public static final Piece MARSHLANDS_WATER = new Piece(new Cell[][] {
        { Cell.water(), null,         null         },
        { Cell.water(), Cell.water(), Cell.water() },
        { Cell.water(), null,         null         },
    }, RIFT_LANDS_WATER, false);

    public static final Piece FISHING_VILLAGE_VILLAGE = new Piece(new Cell[][] {
        { Cell.village(), Cell.village(), Cell.village(), Cell.village() }
    }, RIFT_LANDS_VILLAGE, false);
    public static final Piece FISHING_VILLAGE_WATER = new Piece(new Cell[][] {
        { Cell.water(), Cell.water(), Cell.water(), Cell.water() }
    }, RIFT_LANDS_WATER, false);

    public static final Piece GORGON_GAZE = new Piece(new Cell[][] {
        { Cell.monster(), null,           Cell.monster() },
        { null,           Cell.monster(), null           },
    }, RIFT_LANDS_MONSTER, false);
    public static final Piece ZOMBIE_PLAGUE = new Piece(new Cell[][] {
        { Cell.monster() }
    }, RIFT_LANDS_MONSTER, false);
    public static final Piece GIANT_TROLL_RAVAGE = new Piece(new Cell[][] {
        { Cell.monster(), Cell.monster(), Cell.monster() },
        { null,           Cell.monster(), null           },
    }, RIFT_LANDS_MONSTER, false);
    private static final Piece DRAGON_INFERNO = new Piece(new Cell[][] {
        { null,           null,           Cell.monster() },
        { Cell.monster(), Cell.monster(), Cell.monster() },
        { Cell.monster(), null,           null           },
    }, RIFT_LANDS_MONSTER, false);

    public int width() {
        return cells[0].length;
    }

    public int height() {
        return cells.length;
    }

    public Cell get(int x, int y) {
        return cells[x][y];
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Piece p && Arrays.deepEquals(cells, p.cells) && hasCoin == p.hasCoin;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hasCoin) * 31 + Arrays.deepHashCode(cells);
    }

    @Override
    public String toString() {
        var res = new StringBuilder();
        for (var row : cells) {
            for (var cell : row) {
                res.append(cell == null ? " " : cell);
            }
        }
        return res.toString();
    }
}
