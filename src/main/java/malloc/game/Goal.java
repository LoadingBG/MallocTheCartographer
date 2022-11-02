package malloc.game;

import java.awt.*;
import java.util.*;
import java.util.List;

public interface Goal {
    int score(Board board);

    Goal SENTINEL_WOOD = board -> {
        var stars = 0;

        for (var i = 0; i < board.width(); ++i) {
            if (board.get(0, i) instanceof Cell.Forest) {
                ++stars;
            }
            if (board.get(board.height() - 1, i) instanceof Cell.Forest) {
                ++stars;
            }
        }

        for (var i = 1; i < board.height() - 1; ++i) { // skip corners
            if (board.get(i, 0) instanceof Cell.Forest) {
                ++stars;
            }
            if (board.get(i, board.width() - 1) instanceof Cell.Forest) {
                ++stars;
            }
        }

        return stars;
    };

    Goal TREETOWER = board -> {
        var stars = 0;

        for (var i = 0; i < board.height(); ++i) {
            for (var j = 0; j < board.width(); ++j) {
                if (board.get(i, j) instanceof Cell.Forest && Utils.isSurrounded(board, i, j)) {
                    ++stars;
                }
            }
        }

        return stars;
    };

    Goal GREENBOUGH = board -> {
        var stars = 0;

        for (var i = 0; i < board.height(); ++i) {
            for (var j = 0; j < board.width(); ++j) {
                if (board.get(i, j) instanceof Cell.Forest) {
                    ++stars;
                    break;
                }
            }
        }

        for (var j = 0; j < board.width(); ++j) {
            for (var i = 0; i < board.height(); ++i) {
                if (board.get(i, j) instanceof Cell.Forest) {
                    ++stars;
                    break;
                }
            }
        }

        return stars;
    };

    Goal STONESIDE_FOREST = board -> {
        var mountains = new HashSet<Point>();
        for (var cluster : Utils.findCoordinatesOfClusters(board, cell -> cell instanceof Cell.Forest)) {
            var found = Utils.findAdjacent(board, cluster, cell -> cell instanceof Cell.Mountain);
            if (found.size() > 1) {
                mountains.addAll(Utils.findAdjacent(board, cluster, cell -> cell instanceof Cell.Mountain));
            }
        }
        return mountains.size() * 3;
    };

    Goal CANAL_LAKE = board -> {
        var stars = 0;

        for (var i = 0; i < board.height(); ++i) {
            for (var j = 0; j < board.width(); ++j) {
                if ((board.get(i, j) instanceof Cell.Water && Utils.countAdjacent(board, i, j, cell -> cell instanceof Cell.Farm) > 0)
                    || (board.get(i, j) instanceof Cell.Farm && Utils.countAdjacent(board, i, j, cell -> cell instanceof Cell.Water) > 0)) {
                    ++stars;
                }
            }
        }

        return stars;
    };

    Goal THE_GOLDEN_GRANARY = board -> {
        var stars = 0;

        for (var i = 0; i < board.height(); ++i) {
            for (var j = 0; j < board.width(); ++j) {
                if (board.get(i, j) instanceof Cell.Water && Utils.countAdjacent(board, i, j, cell -> cell.hasRuins) > 0) {
                    ++stars;
                } else if (board.get(i, j) instanceof Cell.Farm cell && cell.hasRuins) {
                    stars += 3;
                }
            }
        }

        return stars;
    };

    Goal MAGES_VALLEY = board -> {
        var stars = 0;

        for (var i = 0; i < board.height(); ++i) {
            for (var j = 0; j < board.width(); ++j) {
                var cell = board.get(i, j);
                if (cell instanceof Cell.Water && Utils.countAdjacent(board, i, j, c -> c instanceof Cell.Mountain) > 0) {
                    stars += 2;
                } else if (cell instanceof Cell.Farm && Utils.countAdjacent(board, i, j, c -> c instanceof Cell.Mountain) > 0) {
                    ++stars;
                }
            }
        }

        return stars;
    };

    Goal SHORESIDE_EXPANSE = board -> {
        var waterCount = Utils.findCoordinatesOfClusters(board, cell -> cell instanceof Cell.Water)
            .stream()
            .filter(cluster -> cluster.stream().noneMatch(cell -> cell.x == 0 || cell.x == board.height() - 1 || cell.y == 0 || cell.y == board.width() - 1))
            .filter(cluster -> Utils.countAdjacent(board, cluster, cell -> cell instanceof Cell.Farm) == 0)
            .count();
        var farmCount = Utils.findCoordinatesOfClusters(board, cell -> cell instanceof Cell.Farm)
            .stream()
            .filter(cluster -> cluster.stream().noneMatch(cell -> cell.x == 0 || cell.x == board.height() - 1 || cell.y == 0 || cell.y == board.width() - 1))
            .filter(cluster -> Utils.countAdjacent(board, cluster, cell -> cell instanceof Cell.Water) == 0)
            .count();

        return (int) (waterCount + farmCount) * 3;
    };

    Goal WILDHOLDS = board -> (int) Utils.findCoordinatesOfClusters(board, cell -> cell instanceof Cell.Village)
        .stream()
        .mapToInt(List::size)
        .filter(s -> s >= 6)
        .count() * 8;

    Goal GREENGOLD_PLAINS = board -> {
        var stars = 0;

        var clusters = Utils.findCoordinatesOfClusters(board, cell -> cell instanceof Cell.Village);
        for (var cluster : clusters) {
            var surroundings = Utils.findSurroundings(board, cluster, cell -> !(cell instanceof Cell.Village));
            if (surroundings.size() >= 3) {
                stars += 3;
            }
        }

        return stars;
    };

    Goal GREAT_CITY = board -> Utils.findCoordinatesOfClusters(board, cell -> cell instanceof Cell.Village)
        .stream()
        .filter(cluster -> !Utils.findSurroundings(board, cluster, cell -> !(cell instanceof Cell.Village)).contains(Cell.mountain()))
        .mapToInt(List::size)
        .max()
        .orElse(0);

    Goal SHIELDGATE = board -> Utils.findCoordinatesOfClusters(board, cell -> cell instanceof Cell.Village)
        .stream()
        .map(List::size)
        .sorted(Comparator.<Integer>naturalOrder().reversed())
        .skip(1)
        .findFirst()
        .orElse(0) * 2;

    Goal BORDERLANDS = board -> {
        var stars = 0;

        outer1:
        for (var i = 0; i < board.height(); ++i) {
            for (var j = 0; j < board.width(); ++j) {
                if (board.get(i, j) instanceof Cell.Empty) {
                    continue outer1;
                }
            }
            stars += 6;
        }

        outer2:
        for (var j = 0; j < board.width(); ++j) {
            for (var i = 0; i < board.height(); ++i) {
                if (board.get(i, j) instanceof Cell.Empty) {
                    continue outer2;
                }
            }
            stars += 6;
        }

        return stars;
    };

    Goal THE_BROKEN_ROAD = board -> {
        var stars = 0;
        
        var offset = Math.max(0, board.height() - board.width());
        outer:
        for (var i = offset; i < board.height(); ++i) {
            for (var delta = 0; delta < board.height() - i; ++delta) {
                if (board.get(i + delta, delta) instanceof Cell.Empty) {
                    continue outer;
                }
            }
            stars += 3;
        }
        
        return stars;
    };

    Goal LOST_BARONY = board -> {
        var squareSizes = new int[board.height()][board.width()];

        var max = 0;

        for (var i = 0; i < board.height(); ++i) {
            for (var j = 0; j < board.width(); ++j) {
                if (board.get(i, j) instanceof Cell.Empty || board.get(i, j) instanceof Cell.Ravine) {
                    squareSizes[i][j] = 0;
                    continue;
                }
                var up = i == 0 ? 0 : squareSizes[i - 1][j];
                var left = j == 0 ? 0 : squareSizes[i][j - 1];
                var diag = i == 0 || j == 0 ? 0 : squareSizes[i - 1][j - 1];
                squareSizes[i][j] = Math.min(up, Math.min(left, diag)) + 1;
                max = Math.max(squareSizes[i][j], max);
            }
        }

        return max * 3;
    };

    Goal THE_CAULDRONS = board -> {
        var stars = 0;

        for (var i = 0; i < board.height(); ++i) {
            for (var j = 0; j < board.width(); ++j) {
                if (board.get(i, j) instanceof Cell.Empty && Utils.isSurrounded(board, i, j)) {
                    ++stars;
                }
            }
        }

        return stars;
    };

    Goal ENEMIES = board -> {
        var spaces = new HashSet<Point>();

        for (var i = 0; i < board.height(); ++i) {
            for (var j = 0; j < board.width(); ++j) {
                if (board.get(i, j) instanceof Cell.Monster) {
                    spaces.addAll(Utils.findAdjacent(board, i, j, cell -> cell instanceof Cell.Empty));
                }
            }
        }

        return -spaces.size();
    };

    Goal COINS = Board::coins;
}
