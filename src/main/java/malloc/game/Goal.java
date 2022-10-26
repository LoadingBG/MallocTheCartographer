package malloc.game;

import java.awt.Point;
import java.util.HashSet;

public interface Goal {
    int score(Board board);

    Goal SENTINEL_WOOD = board -> {
        var stars = 0;

        for (var i = 0; i < board.width(); ++i) {
            if (board.get(0, i) instanceof Cell.Forest) {
                ++stars;
            }
            if (board.get(board.height() - 1, board.width()) instanceof Cell.Forest) {
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

        return (waterCount + farmCount) * 3;
    };

    Goal WILDHOLDS = board -> Utils.findCoordinatesOfClusters(board, cell -> cell instanceof Cell.Village)
        .stream()
        .mapToInt(List::size)
        .filter(s -> s >= 6)
        .count() * 8;
}
