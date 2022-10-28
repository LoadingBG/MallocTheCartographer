package malloc.game;

import org.junit.jupiter.api.*;

final class GoalTest {
    @Test
    void testSentinelWood() {
        Assertions.assertEquals(0, Goal.SENTINEL_WOOD.score(Board.plain()));
        Assertions.assertEquals(0,
            Goal.SENTINEL_WOOD.score(new Board(new Cell[][] {
                { Cell.empty(), Cell.village(), Cell.ravine(),  Cell.mountain() },
                { Cell.empty(), Cell.forest(),  Cell.forest(),  Cell.empty()    },
                { Cell.water(), Cell.farm(),    Cell.monster(), Cell.ruins()    },
            }))
        );
        Assertions.assertEquals(3,
            Goal.SENTINEL_WOOD.score(new Board(new Cell[][] {
                { Cell.forest(), Cell.village(), Cell.forest(),  Cell.mountain() },
                { Cell.forest(), Cell.forest(),  Cell.forest(),  Cell.empty()    },
                { Cell.water(),  Cell.farm(),    Cell.monster(), Cell.ruins()    },
            }))
        );
    }

    @Test
    void testTreetower() {
        Assertions.assertEquals(0, Goal.TREETOWER.score(Board.plain()));
        Assertions.assertEquals(0,
            Goal.SENTINEL_WOOD.score(new Board(new Cell[][] {
                { Cell.empty(), Cell.village(), Cell.ravine(),  Cell.mountain() },
                { Cell.empty(), Cell.forest(),  Cell.forest(),  Cell.empty()    },
                { Cell.water(), Cell.farm(),    Cell.monster(), Cell.ruins()    },
            }))
        );
        Assertions.assertEquals(3,
            Goal.TREETOWER.score(new Board(new Cell[][] {
                { Cell.forest(), Cell.village(), Cell.forest(),  Cell.mountain() },
                { Cell.forest(), Cell.forest(),  Cell.forest(),  Cell.empty()    },
                { Cell.water(),  Cell.empty(),   Cell.monster(), Cell.ruins()    },
            }))
        );
    }

    @Test
    void testGreenbough() {
        Assertions.assertEquals(0, Goal.GREENBOUGH.score(Board.plain()));
        Assertions.assertEquals(2,
            Goal.GREENBOUGH.score(new Board(new Cell[][] {
                { Cell.empty(), Cell.village(), Cell.ravine(),  Cell.mountain() },
                { Cell.empty(), Cell.forest(),  Cell.empty(),   Cell.empty()    },
                { Cell.water(), Cell.farm(),    Cell.monster(), Cell.ruins()    },
            }))
        );
        Assertions.assertEquals(5,
            Goal.GREENBOUGH.score(new Board(new Cell[][] {
                { Cell.forest(), Cell.village(), Cell.forest(),  Cell.mountain() },
                { Cell.forest(), Cell.forest(),  Cell.forest(),  Cell.empty()    },
                { Cell.water(),  Cell.farm(),    Cell.monster(), Cell.ruins()    },
            }))
        );
    }

    @Test
    void testStonesideForest() {
        Assertions.assertEquals(0, Goal.TREETOWER.score(Board.plain()));
        Assertions.assertEquals(6,
            Goal.STONESIDE_FOREST.score(new Board(new Cell[][] {
                { Cell.mountain(), Cell.forest(), Cell.forest(), Cell.mountain() },
                { Cell.mountain(), Cell.empty(),  Cell.forest(), Cell.empty()    },
                { Cell.empty(),    Cell.empty(),  Cell.forest(), Cell.forest()   },
            }))
        );
        Assertions.assertEquals(9,
            Goal.STONESIDE_FOREST.score(new Board(new Cell[][] {
                { Cell.mountain(), Cell.forest(), Cell.forest(), Cell.mountain() },
                { Cell.mountain(), Cell.forest(), Cell.forest(), Cell.empty()    },
                { Cell.empty(),    Cell.empty(),  Cell.forest(), Cell.forest()   },
            }))
        );
    }

    @Test
    void testCanalLake() {
        Assertions.assertEquals(0, Goal.CANAL_LAKE.score(Board.plain()));
        Assertions.assertEquals(0,
            Goal.CANAL_LAKE.score(new Board(new Cell[][] {
                { Cell.farm(), Cell.monster(), Cell.water(), Cell.water() },
                { Cell.farm(), Cell.village(), Cell.empty(), Cell.empty() },
                { Cell.farm(), Cell.village(), Cell.empty(), Cell.water() },
            }))
        );
        Assertions.assertEquals(7,
            Goal.CANAL_LAKE.score(new Board(new Cell[][] {
                { Cell.farm(), Cell.water(),   Cell.water(), Cell.water() },
                { Cell.farm(), Cell.village(), Cell.empty(), Cell.farm()  },
                { Cell.farm(), Cell.water(),   Cell.empty(), Cell.water() },
            }))
        );
    }

    @Test
    void testTheGoldenGranary() {
        Assertions.assertEquals(0, Goal.THE_GOLDEN_GRANARY.score(Board.plain()));
        Assertions.assertEquals(0,
            Goal.THE_GOLDEN_GRANARY.score(new Board(new Cell[][] {
                { Cell.empty(), Cell.ruins(), Cell.farm() },
                { Cell.water(), Cell.empty(), Cell.farm() },
            }))
        );
        Assertions.assertEquals(3,
            Goal.THE_GOLDEN_GRANARY.score(new Board(new Cell[][] {
                { Cell.water(), Cell.ruins(), Cell.water() },
                { Cell.farm(),  Cell.water(), Cell.farm()  },
            }))
        );
        Assertions.assertEquals(3,
            Goal.THE_GOLDEN_GRANARY.score(new Board(new Cell[][] {
                { Cell.empty(), Cell.farm().withRuins(true), Cell.farm() },
                { Cell.water(), Cell.empty(), Cell.farm() },
            }))
        );
        Assertions.assertEquals(6,
            Goal.THE_GOLDEN_GRANARY.score(new Board(new Cell[][] {
                { Cell.water(), Cell.farm().withRuins(true), Cell.water() },
                { Cell.farm(),  Cell.water(), Cell.farm() },
            }))
        );
    }

    @Test
    void testMagesValey() {
        Assertions.assertEquals(0, Goal.MAGES_VALLEY.score(Board.plain()));
        Assertions.assertEquals(0,
            Goal.MAGES_VALLEY.score(new Board(new Cell[][] {
                { Cell.empty(), Cell.mountain(), Cell.village() },
                { Cell.water(), Cell.empty(),    Cell.farm()    },
            }))
        );
        Assertions.assertEquals(1,
            Goal.MAGES_VALLEY.score(new Board(new Cell[][] {
                { Cell.empty(), Cell.mountain(), Cell.farm() },
                { Cell.water(), Cell.empty(),    Cell.farm() },
            }))
        );
        Assertions.assertEquals(2,
            Goal.MAGES_VALLEY.score(new Board(new Cell[][] {
                { Cell.water(), Cell.mountain(), Cell.village() },
                { Cell.water(), Cell.empty(),    Cell.farm()    },
            }))
        );
        Assertions.assertEquals(4,
            Goal.MAGES_VALLEY.score(new Board(new Cell[][] {
                { Cell.water(), Cell.mountain(), Cell.farm() },
                { Cell.water(), Cell.farm(),     Cell.farm() },
            }))
        );
    }

    @Test
    void testShoresideExpanse() {
        Assertions.assertEquals(0, Goal.SHORESIDE_EXPANSE.score(Board.plain()));
        Assertions.assertEquals(0,
            Goal.SHORESIDE_EXPANSE.score(new Board(new Cell[][] {
                { Cell.water(), Cell.mountain(), Cell.farm() },
                { Cell.water(), Cell.empty(),     Cell.farm() },
            }))
        );
        Assertions.assertEquals(0,
            Goal.SHORESIDE_EXPANSE.score(new Board(new Cell[][] {
                { Cell.empty(), Cell.mountain(), Cell.empty(), Cell.empty() },
                { Cell.empty(), Cell.water(),    Cell.farm(),  Cell.empty() },
                { Cell.empty(), Cell.mountain(), Cell.empty(), Cell.empty() },
            }))
        );
        Assertions.assertEquals(9,
            Goal.SHORESIDE_EXPANSE.score(new Board(new Cell[][] {
                { Cell.empty(), Cell.mountain(), Cell.empty(), Cell.empty(), Cell.empty() },
                { Cell.empty(), Cell.water(),    Cell.empty(), Cell.farm(),  Cell.empty() },
                { Cell.empty(), Cell.water(),    Cell.water(), Cell.empty(), Cell.empty() },
                { Cell.empty(), Cell.mountain(), Cell.empty(), Cell.farm(),  Cell.empty() },
                { Cell.empty(), Cell.mountain(), Cell.empty(), Cell.empty(), Cell.empty() },
            }))
        );
    }

    @Test
    void testWildholds() {
        Assertions.assertEquals(0, Goal.WILDHOLDS.score(Board.plain()));
        Assertions.assertEquals(0,
            Goal.WILDHOLDS.score(new Board(new Cell[][] {
                { Cell.village(), Cell.empty(),   Cell.village() },
                { Cell.village(), Cell.village(), Cell.village() },
                { Cell.water(),   Cell.water(),   Cell.water()   },
                { Cell.village(), Cell.village(), Cell.village() },
            }))
        );
        Assertions.assertEquals(8,
            Goal.WILDHOLDS.score(new Board(new Cell[][] {
                { Cell.village(), Cell.empty(),   Cell.village() },
                { Cell.village(), Cell.village(), Cell.village() },
                { Cell.water(),   Cell.village(), Cell.water()   },
                { Cell.village(), Cell.village(), Cell.village() },
            }))
        );
        Assertions.assertEquals(16,
            Goal.WILDHOLDS.score(new Board(new Cell[][] {
                { Cell.village(), Cell.village(), Cell.village() },
                { Cell.village(), Cell.village(), Cell.village() },
                { Cell.water(),   Cell.water(),   Cell.water()   },
                { Cell.village(), Cell.village(), Cell.village() },
                { Cell.village(), Cell.village(), Cell.village() },
            }))
        );
    }

    @Test
    void testGreengoldPlains() {
        Assertions.assertEquals(0, Goal.GREENGOLD_PLAINS.score(Board.plain()));
        Assertions.assertEquals(0,
            Goal.GREENGOLD_PLAINS.score(new Board(new Cell[][] {
                { Cell.village(), Cell.village(), Cell.village() },
                { Cell.monster(), Cell.village(), Cell.farm()    },
                { Cell.monster(), Cell.monster(), Cell.monster() },
            }))
        );
        Assertions.assertEquals(3,
            Goal.GREENGOLD_PLAINS.score(new Board(new Cell[][] {
                { Cell.village(), Cell.village(), Cell.village() },
                { Cell.monster(), Cell.village(), Cell.farm()    },
                { Cell.monster(), Cell.water(),   Cell.monster() },
            }))
        );
        Assertions.assertEquals(6,
            Goal.GREENGOLD_PLAINS.score(new Board(new Cell[][] {
                { Cell.village(), Cell.village(), Cell.village() },
                { Cell.monster(), Cell.water(),   Cell.farm()    },
                { Cell.village(), Cell.village(), Cell.village() },
            }))
        );
    }

    @Test
    void testGreatCity() {
        Assertions.assertEquals(0, Goal.GREAT_CITY.score(Board.plain()));
        Assertions.assertEquals(0,
            Goal.GREAT_CITY.score(new Board(new Cell[][] {
                { Cell.village(), Cell.village(),  Cell.village() },
                { Cell.village(), Cell.empty(),    Cell.village() },
                { Cell.water(),   Cell.mountain(), Cell.village() },
            }))
        );
        Assertions.assertEquals(6,
            Goal.GREAT_CITY.score(new Board(new Cell[][] {
                { Cell.village(), Cell.village(),  Cell.village() },
                { Cell.village(), Cell.empty(),    Cell.village() },
                { Cell.water(),   Cell.empty(),    Cell.village() },
            }))
        );
        Assertions.assertEquals(3,
            Goal.GREAT_CITY.score(new Board(new Cell[][] {
                { Cell.village(), Cell.village(),  Cell.village() },
                { Cell.village(), Cell.empty(),    Cell.village() },
                { Cell.water(),   Cell.mountain(), Cell.village() },
                { Cell.water(),   Cell.water(),    Cell.water()   },
                { Cell.village(), Cell.village(),  Cell.village() },
            }))
        );
    }

    @Test
    void testShieldgate() {
        Assertions.assertEquals(0, Goal.SHIELDGATE.score(Board.plain()));
        Assertions.assertEquals(0,
            Goal.SHIELDGATE.score(new Board(new Cell[][] {
                { Cell.village(), Cell.village(), Cell.village() },
                { Cell.monster(), Cell.monster(), Cell.village() },
                { Cell.monster(), Cell.monster(), Cell.village() },
            }))
        );
        Assertions.assertEquals(2,
            Goal.SHIELDGATE.score(new Board(new Cell[][] {
                { Cell.village(), Cell.village(), Cell.village() },
                { Cell.monster(), Cell.monster(), Cell.village() },
                { Cell.village(), Cell.monster(), Cell.village() },
            }))
        );
        Assertions.assertEquals(6,
            Goal.SHIELDGATE.score(new Board(new Cell[][] {
                { Cell.village(), Cell.village(), Cell.village() },
                { Cell.monster(), Cell.monster(), Cell.monster() },
                { Cell.village(), Cell.village(), Cell.village() },
            }))
        );
        Assertions.assertEquals(6,
            Goal.SHIELDGATE.score(new Board(new Cell[][] {
                { Cell.village(), Cell.village(), Cell.village() },
                { Cell.monster(), Cell.monster(), Cell.monster() },
                { Cell.village(), Cell.village(), Cell.village() },
                { Cell.monster(), Cell.monster(), Cell.monster() },
                { Cell.village(), Cell.monster(), Cell.village() },
            }))
        );
    }

    @Test
    void testBorderlands() {
        Assertions.assertEquals(0, Goal.BORDERLANDS.score(Board.plain()));
        Assertions.assertEquals(6,
            Goal.BORDERLANDS.score(new Board(new Cell[][] {
                { Cell.empty(), Cell.water(), Cell.empty() },
                { Cell.empty(), Cell.water(), Cell.water() },
                { Cell.empty(), Cell.water(), Cell.empty() },
            }))
        );
        Assertions.assertEquals(12,
            Goal.BORDERLANDS.score(new Board(new Cell[][] {
                { Cell.empty(), Cell.water(), Cell.empty() },
                { Cell.water(), Cell.water(), Cell.water() },
                { Cell.empty(), Cell.water(), Cell.empty() },
            }))
        );
        Assertions.assertEquals(24,
            Goal.BORDERLANDS.score(new Board(new Cell[][] {
                { Cell.water(), Cell.water(), Cell.water() },
                { Cell.water(), Cell.empty(), Cell.water() },
                { Cell.water(), Cell.water(), Cell.water() },
            }))
        );
    }

    @Test
    void testTheBrokenRoad() {
        Assertions.assertEquals(0, Goal.THE_BROKEN_ROAD.score(Board.plain()));
        Assertions.assertEquals(0,
            Goal.THE_BROKEN_ROAD.score(new Board(new Cell[][] {
                { Cell.empty(), Cell.empty(), Cell.farm()  },
                { Cell.empty(), Cell.water(), Cell.empty() },
            }))
        );
        Assertions.assertEquals(6,
            Goal.THE_BROKEN_ROAD.score(new Board(new Cell[][] {
                { Cell.water(), Cell.empty(), Cell.farm()  },
                { Cell.water(), Cell.water(), Cell.empty() },
            }))
        );
    }

    @Test
    void testLostBarony() {
        Assertions.assertEquals(3, Goal.LOST_BARONY.score(Board.plain()));
        Assertions.assertEquals(6,
            Goal.LOST_BARONY.score(new Board(new Cell[][] {
                { Cell.mountain(), Cell.village(), Cell.village() },
                { Cell.village(),  Cell.village(), Cell.ravine()  },
                { Cell.forest(),   Cell.monster(), Cell.village() },
            }))
        );
    }

    @Test
    void testTheCauldrons() {
        Assertions.assertEquals(0, Goal.THE_CAULDRONS.score(Board.plain()));
        Assertions.assertEquals(0,
            Goal.THE_CAULDRONS.score(new Board(new Cell[][] {
                { Cell.empty(), Cell.village(), Cell.mountain() },
                { Cell.empty(), Cell.empty(),   Cell.mountain() },
                { Cell.water(), Cell.village(), Cell.mountain() },
            }))
        );
        Assertions.assertEquals(2,
            Goal.THE_CAULDRONS.score(new Board(new Cell[][] {
                { Cell.empty(), Cell.village(), Cell.mountain() },
                { Cell.water(), Cell.empty(),   Cell.mountain() },
                { Cell.water(), Cell.village(), Cell.mountain() },
            }))
        );
    }

    @Test
    void testEnemies() {
        Assertions.assertEquals(0, Goal.ENEMIES.score(Board.plain()));
        Assertions.assertEquals(0,
            Goal.ENEMIES.score(new Board(new Cell[][] {
                { Cell.monster(), Cell.water(), Cell.empty()   },
                { Cell.village(), Cell.water(), Cell.water()   },
                { Cell.village(), Cell.water(), Cell.monster() },
            }))
        );
        Assertions.assertEquals(-2,
            Goal.ENEMIES.score(new Board(new Cell[][] {
                { Cell.monster(), Cell.empty(), Cell.empty()   },
                { Cell.village(), Cell.water(), Cell.empty()   },
                { Cell.village(), Cell.water(), Cell.monster() },
            }))
        );
        Assertions.assertEquals(-1,
            Goal.ENEMIES.score(new Board(new Cell[][] {
                { Cell.monster(), Cell.empty(), Cell.monster() },
                { Cell.village(), Cell.water(), Cell.water()   },
                { Cell.village(), Cell.water(), Cell.empty()   },
            }))
        );
    }
}
