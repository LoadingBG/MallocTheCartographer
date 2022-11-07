package malloc.game;

import java.util.*;

public record Piece(String id, String name, Cell[][] cells, Piece replacement, boolean hasCoin) {
    public static final Piece RIFT_LANDS_FOREST = new Piece("riftLandsForest", "Forest", new Cell[][] {
        { Cell.forest() }
    }, null, false);
    public static final Piece RIFT_LANDS_VILLAGE = new Piece("riftLandsVillage", "Village", new Cell[][] {
        { Cell.village() }
    }, null, false);
    public static final Piece RIFT_LANDS_FARM = new Piece("riftLandsFarm", "Farm", new Cell[][] {
        { Cell.farm() }
    }, null, false);
    public static final Piece RIFT_LANDS_WATER = new Piece("riftLandsWater", "Water", new Cell[][] {
        { Cell.water() }
    }, null, false);
    public static final Piece RIFT_LANDS_MONSTER = new Piece("riftLandsMonster", "Monster", new Cell[][] {
        { Cell.monster() }
    }, null, false);

    public static final Piece TREETOP_VILLAGE_FOREST = new Piece("treetopVillageForest", "Forest", new Cell[][] {
        { null,          null,          Cell.forest(), Cell.forest() },
        { Cell.forest(), Cell.forest(), Cell.forest(), null          },
    }, RIFT_LANDS_FOREST, false);
    public static final Piece TREETOP_VILLAGE_VILLAGE = new Piece("treetopVillageVillage", "Village", new Cell[][] {
        { null,           null,           Cell.village(), Cell.village() },
        { Cell.village(), Cell.village(), Cell.village(), null           },
    }, RIFT_LANDS_VILLAGE, false);

    public static final Piece FARMLAND_SMALL = new Piece("farmlandSmall", "Small", new Cell[][] {
        { Cell.farm() },
        { Cell.farm() },
    }, RIFT_LANDS_FARM, true);
    public static final Piece FARMLAND_BIG = new Piece("farmland", "Big", new Cell[][] {
        { null,        Cell.farm(), null        },
        { Cell.farm(), Cell.farm(), Cell.farm() },
        { null,        Cell.farm(), null        },
    }, RIFT_LANDS_FARM, false);

    public static final Piece FORGOTTEN_FOREST_SMALL = new Piece("forgottenForestSmall", "Small", new Cell[][] {
        { Cell.forest(), null          },
        { null,          Cell.forest() },
    }, RIFT_LANDS_FOREST, true);
    public static final Piece FORGOTTEN_FOREST_BIG = new Piece("forgottenForestBig", "Big", new Cell[][] {
        { Cell.forest(), null          },
        { Cell.forest(), Cell.forest() },
        { null,          Cell.forest() },
    }, RIFT_LANDS_FOREST, false);

    public static final Piece ORCHARD_FOREST = new Piece("orchardForest", "Forest", new Cell[][] {
        { Cell.forest(), Cell.forest(), Cell.forest() },
        { null,          null,          Cell.forest() },
    }, RIFT_LANDS_FOREST, false);
    public static final Piece ORCHARD_FARM = new Piece("orchardFarm", "Farm", new Cell[][] {
        { Cell.farm(), Cell.farm(), Cell.farm() },
        { null,        null,        Cell.farm() },
    }, RIFT_LANDS_FARM, false);

    public static final Piece HAMLET_SMALL = new Piece("hamletSmall", "Small", new Cell[][] {
        { Cell.village(), null           },
        { Cell.village(), Cell.village() },
    }, RIFT_LANDS_VILLAGE, true);
    public static final Piece HAMLET_BIG = new Piece("hamletBig", "Big", new Cell[][] {
        { Cell.village(), Cell.village(), Cell.village() },
        { Cell.village(), Cell.village(), null           },
    }, RIFT_LANDS_VILLAGE, false);

    public static final Piece HOMESTEAD_VILLAGE = new Piece("homesteadVillage", "Village", new Cell[][] {
        { Cell.village(), null           },
        { Cell.village(), Cell.village() },
        { Cell.village(), null           },
    }, RIFT_LANDS_VILLAGE, false);
    public static final Piece HOMESTEAD_FARM = new Piece("homesteadFarm", "Farm", new Cell[][] {
        { Cell.farm(), null        },
        { Cell.farm(), Cell.farm() },
        { Cell.farm(), null        },
    }, RIFT_LANDS_FARM, false);

    public static final Piece HINTERLAND_STREAM_FARM = new Piece("hinterlandStreamFarm", "Farm", new Cell[][] {
        { Cell.farm(), Cell.farm(), Cell.farm() },
        { Cell.farm(), null,        null        },
        { Cell.farm(), null,        null        },
    }, RIFT_LANDS_FARM, false);
    public static final Piece HINTERLAND_STREAM_WATER = new Piece("hinterlandStreamWater", "Water", new Cell[][] {
        { Cell.water(), Cell.water(), Cell.water() },
        { Cell.water(), null,         null         },
        { Cell.water(), null,         null         },
    }, RIFT_LANDS_WATER, false);

    public static final Piece GREAT_RIVER_SMALL = new Piece("greatRiverSmall", "Small", new Cell[][] {
        { Cell.water() },
        { Cell.water() },
        { Cell.water() },
    }, RIFT_LANDS_WATER, true);
    public static final Piece GREAT_RIVER_BIG = new Piece("greatRiverBig", "Big", new Cell[][] {
        { null,         null,         Cell.water() },
        { null,         Cell.water(), Cell.water() },
        { Cell.water(), Cell.water(), null         },
    }, RIFT_LANDS_WATER, false);

    public static final Piece MARSHLANDS_FOREST = new Piece("marshlandsForest", "Forest", new Cell[][] {
        { Cell.forest(), null,          null          },
        { Cell.forest(), Cell.forest(), Cell.forest() },
        { Cell.forest(), null,          null          },
    }, RIFT_LANDS_FOREST, false);
    public static final Piece MARSHLANDS_WATER = new Piece("marshlandsWater", "Water", new Cell[][] {
        { Cell.water(), null,         null         },
        { Cell.water(), Cell.water(), Cell.water() },
        { Cell.water(), null,         null         },
    }, RIFT_LANDS_WATER, false);

    public static final Piece FISHING_VILLAGE_VILLAGE = new Piece("fishingVillageVillage", "Village", new Cell[][] {
        { Cell.village(), Cell.village(), Cell.village(), Cell.village() }
    }, RIFT_LANDS_VILLAGE, false);
    public static final Piece FISHING_VILLAGE_WATER = new Piece("fishingVillageWater", "Water", new Cell[][] {
        { Cell.water(), Cell.water(), Cell.water(), Cell.water() }
    }, RIFT_LANDS_WATER, false);

    public static final Piece GORGON_GAZE = new Piece("gorgonGaze", null, new Cell[][] {
        { Cell.monster(), null,           Cell.monster() },
        { null,           Cell.monster(), null           },
    }, RIFT_LANDS_MONSTER, false);
    public static final Piece ZOMBIE_PLAGUE = new Piece("zombiePlague", null, new Cell[][] {
        { Cell.monster() }
    }, RIFT_LANDS_MONSTER, false);
    public static final Piece GIANT_TROLL_RAVAGE = new Piece("giantTrollRavage", null, new Cell[][] {
        { Cell.monster(), Cell.monster(), Cell.monster() },
        { null,           Cell.monster(), null           },
    }, RIFT_LANDS_MONSTER, false);
    public static final Piece DRAGON_INFERNO = new Piece("dragonInferno", null, new Cell[][] {
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

    public Piece flipHorizontal() {
        var newCells = new Cell[height()][width()];
        for (var i = 0; i < height(); ++i) {
            for (var j = 0; j < width(); ++j) {
                newCells[i][width() - 1 - j] = cells[i][j];
            }
        }
        return new Piece(id, name, newCells, replacement, hasCoin);
    }

    public Piece flipVertical() {
        var newCells = new Cell[height()][width()];
        for (var i = 0; i < height(); ++i) {
            System.arraycopy(cells[i], 0, newCells[height() - 1 - i], 0, width());
        }
        return new Piece(id, name, newCells, replacement, hasCoin);
    }

    public Piece rotateCounterclockwise() {
        var newCells = new Cell[width()][height()];
        for (var i = 0; i < height(); ++i) {
            for (var j = 0; j < width(); ++j) {
                newCells[width() - 1 - j][i] = cells[i][j];
            }
        }
        return new Piece(id, name, newCells, replacement, hasCoin);
    }

    public Piece rotateClockwise() {
        var newCells = new Cell[width()][height()];
        for (var i = 0; i < height(); ++i) {
            for (var j = 0; j < width(); ++j) {
                newCells[j][height() - 1 - i] = cells[i][j];
            }
        }
        return new Piece(id, name, newCells, replacement, hasCoin);
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
