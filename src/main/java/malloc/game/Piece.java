package malloc.game;

import java.util.Arrays;
import java.util.Objects;

public record Piece(Cell[][] cells, boolean hasCoin) {
    public static final Piece TREETOP_VILLAGE_FOREST = new Piece(new Cell[][] {
        { null,          null,          Cell.forest(), Cell.forest() },
        { Cell.forest(), Cell.forest(), Cell.forest(), null          },
    }, false);
    public static final Piece TREETOP_VILLAGE_VILLAGE = new Piece(new Cell[][] {
        { null,           null,           Cell.village(), Cell.village() },
        { Cell.village(), Cell.village(), Cell.village(), null           },
    }, false);

    public static final Piece FARMLAND_SMALL = new Piece(new Cell[][] {
        { Cell.farm() },
        { Cell.farm() },
    }, true);
    public static final Piece FARMLAND_BIG = new Piece(new Cell[][] {
        { null,        Cell.farm(), null        },
        { Cell.farm(), Cell.farm(), Cell.farm() },
        { null,        Cell.farm(), null        },
    }, false);

    public static final Piece FORGOTTEN_FOREST_SMALL = new Piece(new Cell[][] {
        { Cell.forest(), null          },
        { null,          Cell.forest() },
    }, true);
    public static final Piece FORGOTTEN_FOREST_BIG = new Piece(new Cell[][] {
        { Cell.forest(), null          },
        { Cell.forest(), Cell.forest() },
        { null,          Cell.forest() },
    }, false);

    public static final Piece ORCHARD_FOREST = new Piece(new Cell[][] {
        { Cell.forest(), Cell.forest(), Cell.forest() },
        { null,          null,          Cell.forest() },
    }, false);
    public static final Piece ORCHARD_FARM = new Piece(new Cell[][] {
        { Cell.farm(), Cell.farm(), Cell.farm() },
        { null,        null,        Cell.farm() },
    }, false);

    public static final Piece HAMLET_SMALL = new Piece(new Cell[][] {
        { Cell.village(), null           },
        { Cell.village(), Cell.village() },
    }, true);
    public static final Piece HAMLET_BIG = new Piece(new Cell[][] {
        { Cell.village(), Cell.village(), Cell.village() },
        { Cell.village(), Cell.village(), null           },
    }, false);

    public static final Piece RIFT_LANDS_FOREST = new Piece(new Cell[][] {
        { Cell.forest() }
    }, false);
    public static final Piece RIFT_LANDS_VILLAGE = new Piece(new Cell[][] {
        { Cell.village() }
    }, false);
    public static final Piece RIFT_LANDS_FARM = new Piece(new Cell[][] {
        { Cell.farm() }
    }, false);
    public static final Piece RIFT_LANDS_WATER = new Piece(new Cell[][] {
        { Cell.water() }
    }, false);
    public static final Piece RIFT_LANDS_MONSTER = new Piece(new Cell[][] {
        { Cell.monster() }
    }, false);

    public static final Piece HOMESTEAD_VILLAGE = new Piece(new Cell[][] {
        { Cell.village(), null           },
        { Cell.village(), Cell.village() },
        { Cell.village(), null           },
    }, false);
    public static final Piece HOMESTEAD_FARM = new Piece(new Cell[][] {
        { Cell.farm(), null        },
        { Cell.farm(), Cell.farm() },
        { Cell.farm(), null        },
    }, false);

    public static final Piece HINTERLAND_STREAM_FARM = new Piece(new Cell[][] {
        { Cell.farm(), Cell.farm(), Cell.farm() },
        { Cell.farm(), null,        null        },
        { Cell.farm(), null,        null        },
    }, false);
    public static final Piece HINTERLAND_STREAM_WATER = new Piece(new Cell[][] {
        { Cell.water(), Cell.water(), Cell.water() },
        { Cell.water(), null,         null         },
        { Cell.water(), null,         null         },
    }, false);

    public static final Piece GREAT_RIVER_SMALL = new Piece(new Cell[][] {
        { Cell.water() },
        { Cell.water() },
        { Cell.water() },
    }, true);
    public static final Piece GREAT_RIVER_BIG = new Piece(new Cell[][] {
        { null,         null,         Cell.water() },
        { null,         Cell.water(), Cell.water() },
        { Cell.water(), Cell.water(), null         },
    }, false);

    public static final Piece MARSHLANDS_FOREST = new Piece(new Cell[][] {
        { Cell.forest(), null,          null          },
        { Cell.forest(), Cell.forest(), Cell.forest() },
        { Cell.forest(), null,          null          },
    }, false);
    public static final Piece MARSHLANDS_WATER = new Piece(new Cell[][] {
        { Cell.water(), null,         null         },
        { Cell.water(), Cell.water(), Cell.water() },
        { Cell.water(), null,         null         },
    }, false);

    public static final Piece FISHING_VILLAGE_VILLAGE = new Piece(new Cell[][] {
        { Cell.village(), Cell.village(), Cell.village(), Cell.village() }
    }, false);
    public static final Piece FISHING_VILLAGE_WATER = new Piece(new Cell[][] {
        { Cell.water(), Cell.water(), Cell.water(), Cell.water() }
    }, false);

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
