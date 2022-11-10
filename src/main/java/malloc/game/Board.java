package malloc.game;

import java.awt.image.*;
import java.io.*;

import javax.imageio.*;

public final class Board {
    public static Board plain() {
        return new Board("Plain", new Cell[][] {
            { Cell.empty(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(), Cell.empty(),    Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(), Cell.empty() },
            { Cell.empty(), Cell.empty(), Cell.empty(),    Cell.mountain(), Cell.empty(), Cell.ruins(),    Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(), Cell.empty() },
            { Cell.empty(), Cell.ruins(), Cell.empty(),    Cell.empty(),    Cell.empty(), Cell.empty(),    Cell.empty(), Cell.empty(),    Cell.mountain(), Cell.ruins(), Cell.empty() },
            { Cell.empty(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(), Cell.empty(),    Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(), Cell.empty() },
            { Cell.empty(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(), Cell.empty(),    Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(), Cell.empty() },
            { Cell.empty(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(), Cell.mountain(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(), Cell.empty() },
            { Cell.empty(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(), Cell.empty(),    Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(), Cell.empty() },
            { Cell.empty(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(), Cell.empty(),    Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(), Cell.empty() },
            { Cell.empty(), Cell.ruins(), Cell.mountain(), Cell.empty(),    Cell.empty(), Cell.empty(),    Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.ruins(), Cell.empty() },
            { Cell.empty(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(), Cell.ruins(),    Cell.empty(), Cell.mountain(), Cell.empty(),    Cell.empty(), Cell.empty() },
            { Cell.empty(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(), Cell.empty(),    Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(), Cell.empty() },
        });
    }
    public static Board ravineMiddle() {
        return new Board("Ravine in Middle", new Cell[][] {

            { Cell.empty(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(),  Cell.empty(),    Cell.empty(),  Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty() },
            { Cell.empty(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(),  Cell.empty(),    Cell.ruins(),  Cell.empty(), Cell.mountain(), Cell.empty(),    Cell.empty() },
            { Cell.empty(), Cell.empty(), Cell.ruins(),    Cell.mountain(), Cell.empty(),  Cell.empty(),    Cell.empty(),  Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty() },
            { Cell.empty(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(),  Cell.ravine(),   Cell.empty(),  Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty() },
            { Cell.empty(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.ravine(), Cell.ravine(),   Cell.ruins(),  Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty() },
            { Cell.empty(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.ravine(), Cell.ravine(),   Cell.ravine(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty() },
            { Cell.empty(), Cell.ruins(), Cell.empty(),    Cell.empty(),    Cell.empty(),  Cell.ravine(),   Cell.empty(),  Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty() },
            { Cell.empty(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(),  Cell.mountain(), Cell.empty(),  Cell.empty(), Cell.ruins(),    Cell.empty(),    Cell.empty() },
            { Cell.empty(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(),  Cell.empty(),    Cell.empty(),  Cell.empty(), Cell.empty(),    Cell.mountain(), Cell.empty() },
            { Cell.empty(), Cell.empty(), Cell.mountain(), Cell.ruins(),    Cell.empty(),  Cell.empty(),    Cell.empty(),  Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty() },
            { Cell.empty(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(),  Cell.empty(),    Cell.empty(),  Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty() },
        });
    }

    private final String name;
    private final Cell[][] cells;
    private int coins;

    public Board(Cell[][] cells) {
        this("", cells, 0);
    }

    public Board(String name, Cell[][] cells) {
        this(name, cells, 0);
    }

    private Board(String name, Cell[][] cells, int coins) {
        this.name = name;
        this.cells = cells;
        this.coins = coins;
    }

    private void addCoin() {
        this.coins = Math.min(this.coins + 1, 10);
    }

    public int coins() {
        return coins;
    }

    public String name() {
        return name;
    }

    public boolean canFitPiece(Piece piece, int x, int y, boolean ruins) {
        boolean foundRuins = false;
        for (var i = 0; i < piece.height(); ++i) {
            if (i + y >= cells.length) {
                return false;
            }
            for (var j = 0; j < piece.width(); ++j) {
                if (j + x >= cells[i + y].length) {
                    return false;
                }
                if (piece.get(i, j) == null) {
                    continue;
                }
                if (!cells[i + y][j + x].isReplaceable) {
                    return false;
                }
                if (cells[i + y][j + x].hasRuins) {
                    foundRuins = true;
                }
            }
        }
        return !ruins || foundRuins;
    }

    public void placePiece(Piece piece, int x, int y) {
        for (var i = 0; i < piece.height() && i + y < cells.length; ++i) {
            for (var j = 0; j < piece.width() && j + x < cells[i + y].length; ++j) {
                if (piece.get(i, j) != null) {
                    cells[i + y][j + x] = piece.get(i, j).withRuins(cells[i + y][j + x].hasRuins);
                }
            }
        }

        if (piece.hasCoin()) {
            addCoin();
        }

        for (var i = 0; i < cells.length; ++i) {
            for (var j = 0; j < cells[i].length; ++j) {
                if (cells[i][j] instanceof Cell.Mountain m && m.hasCoin() && malloc.game.Utils.isSurrounded(this, i, j)) {
                    m.collectCoin();
                    addCoin();
                }
            }
        }
    }

    public Board copy() {
        var newCells = new Cell[cells.length][];
        for (var i = 0; i < cells.length; ++i) {
            newCells[i] = new Cell[cells[i].length];
            for (var j = 0; j < cells[i].length; ++j) {
                newCells[i][j] = cells[i][j].copy();
            }
        }
        return new Board(name, newCells, coins);
    }

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
    public String toString() {
        var res = new StringBuilder("Coins: ")
            .append(coins)
            .append("/10\n┏")
            .append("━┯".repeat(Math.max(0, cells.length - 1)))
            .append("━┓\n");

        for (var row : cells) {
            res.append("┃");
            for (var cell : row) {
                res.append(cell).append("│");
            }
            res.deleteCharAt(res.length() - 1)
                .append("┃\n┠")
                .append("─┼".repeat(Math.max(0, cells.length - 1)))
                .append("─┨\n");
        }

        res.append("┗")
            .append("━┷".repeat(Math.max(0, cells.length - 1)))
            .append("━┛");
        return res.toString();
    }

    public byte[] toImageBytes() {
        var bytes = new ByteArrayOutputStream();
        try {
            ImageIO.write(toImage(), "png", bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytes.toByteArray();
    }

    public BufferedImage toImage() {
        var image = new BufferedImage(width() * malloc.Utils.IMAGE_SIZE, height() * malloc.Utils.IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
        var graphics = image.createGraphics();

        for (var i = 0; i < height(); ++i) {
            for (var j = 0; j < width(); ++j) {
                var tileImage = malloc.Utils.BORDER;
                if (cells[i][j] instanceof Cell.Empty c) {
                    tileImage = c.hasRuins ? malloc.Utils.EMPTY_RUINS : malloc.Utils.EMPTY;
                } else if (cells[i][j] instanceof Cell.Forest c) {
                    tileImage = c.hasRuins ? malloc.Utils.FOREST_RUINS : malloc.Utils.FOREST;
                } else if (cells[i][j] instanceof Cell.Village c) {
                    tileImage = c.hasRuins ? malloc.Utils.VILLAGE_RUINS : malloc.Utils.VILLAGE;
                } else if (cells[i][j] instanceof Cell.Farm c) {
                    tileImage = c.hasRuins ? malloc.Utils.FARM_RUINS : malloc.Utils.FARM;
                } else if (cells[i][j] instanceof Cell.Water c) {
                    tileImage = c.hasRuins ? malloc.Utils.WATER_RUINS : malloc.Utils.WATER;
                } else if (cells[i][j] instanceof Cell.Monster c) {
                    tileImage = c.hasRuins ? malloc.Utils.MONSTER_RUINS : malloc.Utils.MONSTER;
                } else if (cells[i][j] instanceof Cell.Mountain c) {
                    tileImage = c.hasCoin() ? malloc.Utils.MOUNTAIN_COIN : malloc.Utils.MOUNTAIN;
                }
//                var tileImage = switch (cells[i][j]) {
//                    case Cell.Empty c -> c.hasRuins ? malloc.Utils.EMPTY_RUINS : malloc.Utils.EMPTY;
//                    case Cell.Forest c -> c.hasRuins ? malloc.Utils.FOREST_RUINS : malloc.Utils.FOREST;
//                    case Cell.Village c -> c.hasRuins ? malloc.Utils.VILLAGE_RUINS : malloc.Utils.VILLAGE;
//                    case Cell.Farm c -> c.hasRuins ? malloc.Utils.FARM_RUINS : malloc.Utils.FARM;
//                    case Cell.Water c -> c.hasRuins ? malloc.Utils.WATER_RUINS : malloc.Utils.WATER;
//                    case Cell.Monster c -> c.hasRuins ? malloc.Utils.MONSTER_RUINS : malloc.Utils.MONSTER;
//                    case Cell.Mountain c -> c.hasCoin() ? malloc.Utils.MOUNTAIN_COIN : malloc.Utils.MOUNTAIN;
//                    case Cell.Ravine c -> malloc.Utils.BORDER;
//                };
                graphics.drawImage(tileImage, j * malloc.Utils.IMAGE_SIZE, i * malloc.Utils.IMAGE_SIZE, null);
            }
        }

        return image;
    }
}
