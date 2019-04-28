package skiing;

import java.util.*;
import java.util.stream.Collectors;

public class JSkiing {
    private List<Coordinate> bestPath = null;

    public void visit(final int[][] mountain) {
        final int w = mountain.length;
        final int h = mountain[0].length;

        for(int i = 0 ; i < w; i++) {
            for(int j = 0; j < h; j++) {
                final Coordinate from = new Coordinate(i, j);
                visit(from, mountain, new Stack<>(), new HashSet<>());
            }
        }
        System.out.println("Best path: " + bestPath);
        System.out.println(" Size: " + bestPath.size()); // 15
        System.out.println(" Drop: " + drop(bestPath)); // 1422
    }

    private void visit(final Coordinate from,
                       final int[][] mountain,
                       final Stack<Coordinate> path,
                       final Set<Coordinate> visited) {
        path.push(from);
        visited.add(from);

        List<Coordinate> children = children(from, mountain);

        if (children.isEmpty()) {
            isBestPath(processPath(path, mountain));
        } else {
            for(Coordinate child : children) {
                if (!visited.contains(child)) {
                    visit(child, mountain, path, visited);
                }
            }
        }

        path.pop();
        visited.remove(from);
    } // visit

    private void isBestPath(final List<Coordinate> path) {
        if (bestPath == null) {
            bestPath = new LinkedList<>(path);
        } else if (path.size() > bestPath.size()) {
            bestPath = new LinkedList<>(path);
        } else if (path.size() == bestPath.size() && drop(path) > drop(bestPath)) {
            bestPath = new LinkedList<>(path);
        }
    }

    private int drop(final List<Coordinate> path) {
        if (path.isEmpty())
            return -1;
        else {
            final int top = path.get(0).value;
            final int bottom = path.get(path.size() - 1).value;
            return top - bottom;
        }
    }

    private Stack<Coordinate> processPath(final Stack<Coordinate> path, final int[][] mountain) {
        path.forEach(e -> e.value = mountain[e.x][e.y]);
        return path;
    }

    private List<Coordinate> children(final Coordinate from, final int[][] mountain) {
        final List<Coordinate> ns = new LinkedList<>();
        final int fromVal = mountain[from.x][from.y];
        final int s = mountain.length;

        ns.add(new Coordinate(from.x + 1, from.y));
        ns.add(new Coordinate(from.x - 1, from.y));
        ns.add(new Coordinate(from.x, from.y + 1));
        ns.add(new Coordinate(from.x, from.y - 1 ));

        final List<Coordinate> nc = ns.stream()
                                      .filter(e -> e.x >= 0 && e.y >= 0 && e.x < s && e.y < s && mountain[e.x][e.y] < fromVal)
                                      .collect(Collectors.toList());
        return nc;
    }

    public static void main(String[] args) throws Exception {
        final JSkiing jSkiing = new JSkiing();

        long t0 = System.currentTimeMillis();
        final int[][] mountain = MapLoader.loadMap();
        long tf = System.currentTimeMillis() - t0;
        System.out.println("Loaded map in " + tf + " millis"); // 300 ms

        t0 = System.currentTimeMillis();
        jSkiing.visit(mountain);
        tf = System.currentTimeMillis() - t0;
        System.out.println("Solved in " + tf+ " millis"); // 2.5 secs
    }
}
