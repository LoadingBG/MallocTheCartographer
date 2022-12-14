package malloc.game;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.*;

public final class Utils {
    private Utils() {}

    public static boolean isSurrounded(Board board, int x, int y) {
        return (x == 0 || !board.get(x - 1, y).isReplaceable)
            && (x == board.height() - 1 || !board.get(x + 1, y).isReplaceable)
            && (y == 0 || !board.get(x, y - 1).isReplaceable)
            && (y == board.width() - 1 || !board.get(x, y + 1).isReplaceable);
    }

    public static int countAdjacent(Board board, int x, int y, Predicate<Cell> predicate) {
        return (x != 0 && predicate.test(board.get(x - 1, y)) ? 1 : 0)
            + (x != board.height() - 1 && predicate.test(board.get(x + 1, y)) ? 1 : 0)
            + (y != 0 && predicate.test(board.get(x, y - 1)) ? 1 : 0)
            + (y != board.width() - 1 && predicate.test(board.get(x, y + 1)) ? 1 : 0);
    }

    public static List<Point> findAdjacent(Board board, int x, int y, Predicate<Cell> predicate) {
        var points = new ArrayList<Point>();
        if (x != 0 && predicate.test(board.get(x - 1, y))) {
            points.add(new Point(x - 1, y));
        }
        if (x != board.height() - 1 && predicate.test(board.get(x + 1, y))) {
            points.add(new Point(x + 1, y));
        }
        if (y != 0 && predicate.test(board.get(x, y - 1))) {
            points.add(new Point(x, y - 1));
        }
        if (y != board.width() - 1 && predicate.test(board.get(x, y + 1))) {
            points.add(new Point(x, y + 1));
        }
        return points;
    }

    public static Set<Cell> findSurroundings(Board board, int x, int y, Predicate<Cell> predicate) {
        var cells = new HashSet<Cell>();
        if (x > 0 && predicate.test(board.get(x - 1, y))) {
            cells.add(board.get(x - 1, y));
        }
        if (x < board.height() - 1 && predicate.test(board.get(x + 1, y))) {
            cells.add(board.get(x + 1, y));
        }
        if (y > 0 && predicate.test(board.get(x, y - 1))) {
            cells.add(board.get(x, y - 1));
        }
        if (y < board.width() - 1 && predicate.test(board.get(x, y + 1))) {
            cells.add(board.get(x, y + 1));
        }
        return cells;
    }

    public static Set<List<Point>> findCoordinatesOfClusters(Board board, Predicate<Cell> predicate) {
        var clusters = new HashSet<List<Point>>();

        for (var i = 0; i < board.height(); ++i) {
            for (var j = 0; j < board.width(); ++j) {
                if (predicate.test(board.get(i, j))) {
                    clusters.add(findCluster(board, i, j, predicate));
                }
            }
        }

        return clusters;
    }

    private static List<Point> findCluster(Board board, int x, int y, Predicate<Cell> predicate) {
        var visited = new HashSet<Point>();

        var stack = new ArrayDeque<Point>();
        stack.push(new Point(x, y));

        while (!stack.isEmpty()) {
            var p = stack.pop();
            visited.add(p);

            var up = new Point(p.x - 1, p.y);
            if (p.x > 0 && predicate.test(board.get(up.x, up.y)) && !visited.contains(up)) {
                stack.push(up);
            }

            var down = new Point(p.x + 1, p.y);
            if (p.x < board.height() - 1 && predicate.test(board.get(down.x, down.y)) && !visited.contains(down)) {
                stack.push(down);
            }

            var left = new Point(p.x, p.y - 1);
            if (p.y > 0 && predicate.test(board.get(left.x, left.y)) && !visited.contains(left)) {
                stack.push(left);
            }

            var right = new Point(p.x, p.y + 1);
            if (p.y < board.width() - 1 && predicate.test(board.get(right.x, right.y)) && !visited.contains(right)) {
                stack.push(right);
            }
        }

        return visited.stream().sorted(Comparator.<Point>comparingInt(p -> p.x).thenComparingInt(p -> p.y)).toList();
    }

    public static int countAdjacent(Board board, List<Point> cluster, Predicate<Cell> predicate) {
        return cluster.stream().mapToInt(coords -> countAdjacent(board, coords.x, coords.y, predicate)).sum();
    }

    public static List<Point> findAdjacent(Board board, List<Point> cluster, Predicate<Cell> predicate) {
        return cluster.stream()
            .flatMap(p -> findAdjacent(board, p.x, p.y, predicate).stream())
            .distinct()
            .toList();
    }

    public static Set<Cell> findSurroundings(Board board, List<Point> cluster, Predicate<Cell> predicate) {
        var cells = new HashSet<Cell>();
        for (var pos : cluster) {
            cells.addAll(findSurroundings(board, pos.x, pos.y, predicate.and(cell -> !(cell instanceof Cell.Ravine))));
        }
        return cells;
    }
}
