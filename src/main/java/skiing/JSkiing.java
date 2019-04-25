package skiing;

import java.util.*;
import java.util.stream.Collectors;

public class JSkiing {
    private Stack<Coordinate> bestPath = null;

    public void visit(final int[][] mountain) {
        final int w = mountain.length;
        final int h = mountain[0].length;

        Coordinate from = new Coordinate(0, 1);
        visit(from, mountain, new Stack<>(), new HashSet<>());
        System.out.println(from + " " + bestPath);

//        bestPath = null;
//        from = new Coordinate(0, 3);
//        visit(from, mountain, new Stack<>(), new HashSet<>());
//        System.out.println("0, 3: " + bestPath);
    }

    private void visit(final Coordinate from,
                       final int[][] mountain,
                       final Stack<Coordinate> path,
                       final Set<Coordinate> visited) {
        path.push(from);
        visited.add(from);

        List<Coordinate> children = children(from, mountain);

        if (children.isEmpty()) {
            System.out.println("Path " + processPath(path, mountain));
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

    private void isBestPath(final Stack<Coordinate> path) {
        //System.out.println("> isBestPath: " + path);
        if (bestPath == null || path.size() > bestPath.size()) {
            bestPath = path;
        }
        System.out.println("< isBestPath: " + bestPath);
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
        System.out.println("Loaded map in " + tf + " millis");

        jSkiing.visit(mountain);
    }
}
