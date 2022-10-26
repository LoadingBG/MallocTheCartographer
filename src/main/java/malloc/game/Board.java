package malloc.game;

public class Board {
    public static Board plain() {
        return new Board(new Cell[][] {
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
        return new Board(new Cell[][] {
            { Cell.empty(), Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty(),  Cell.empty(),    Cell.empty(),  Cell.empty(), Cell.empty(),    Cell.empty(),    Cell.empty() },
            { Cell.empty(), Cell.empty(), Cell.ruins(),    Cell.mountain(), Cell.empty(),  Cell.empty(),    Cell.ruins(),  Cell.empty(), Cell.mountain(), Cell.empty(),    Cell.empty() },
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

    private Cell[][] cells;
    private int coins = 0;

    public Board(Cell[][] cells) {
        this.cells = cells;
    }

    private Board(Cell[][] cells, int coins) {
        this.cells = cells;
        this.coins = coins;
    }

    private void addCoin() {
        if (coins < 10) {
            ++coins;
        }
    }

    public boolean canFitPiece(Piece piece, int x, int y) {
        for (var i = 0; i < piece.height(); ++i) {
            for (var j = 0; j < piece.width(); ++j) {
                if (piece.get(i, j) != null && !cells[i + y][j + x].isReplaceable) {
                    return false;
                }
            }
        }
        return true;
    }

    public void placePiece(Piece piece, int x, int y) {
        for (var i = 0; i < piece.height(); ++i) {
            for (var j = 0; j < piece.width(); ++j) {
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
                if (cells[i][j] instanceof Cell.Mountain m && m.hasCoin() && Utils.isSurrounded(this, i, j)) {
                    m.collectCoin();
                    addCoin();
                }
            }
        }
    }

    public Board copy() {
        var newCells = new Cell[cells.length][];
        for (int i = 0; i < cells.length; ++i) {
            newCells[i] = new Cell[cells[i].length];
            System.arraycopy(cells, 0, newCells, 0, newCells[i].length);
        }
        return new Board(newCells, coins);
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
}
