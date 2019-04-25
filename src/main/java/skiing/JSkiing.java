package skiing;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class JSkiing {
    public static final String datum = "./src/main/scala/mountain1000x1000.txt";

    public void visit(final int[][] mountain) {
        final int w = mountain.length;
        final int h = mountain[0].length;

        System.out.println("0, 1");
        Coordinate from = new Coordinate(0, 1);
        visit(from, mountain, new Stack<>(), new HashSet<>());

        System.out.println("0, 3");
        from = new Coordinate(0, 3);
        visit(from, mountain, new Stack<>(), new HashSet<>());
    }

    private void visit(final Coordinate from,
                       final int[][] mountain,
                       final Stack<Coordinate> path,
                       Set<Coordinate> visited) {
        int nbPath = 0;

        path.push(from);
        visited.add(from);

        List<Coordinate> children = children(from, mountain);

        if (children.isEmpty()) {
            System.out.println((++nbPath) + ". Path " + processPath(path, mountain));
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

    private List<Coordinate> processPath(final List<Coordinate> path, final int[][] mountain) {
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

    private int[][] loadMap() throws Exception {
        final List<String> lines = Files.readAllLines(Paths.get(datum));
        int count = -1;
        int[][] mountain = null;

        for(String l : lines) {
            if (count == -1) {
                final String[] dim = l.split("\\s+");
                final int x = Integer.parseInt(dim[0]);
                final int y = Integer.parseInt(dim[1]);
                mountain = new int[x][y];
            } else {
                final String[] ys = l.split("\\s+");
                for(int i = 0; i < ys.length; i++) {
                    final int y = Integer.parseInt(ys[i]);
                    mountain[count][i] = y;
                }
            }
            count++;
        }
        System.out.println("Read " + count + " lines");
        return mountain;
    }

    public static void main(String[] args) throws Exception {
        final JSkiing jSkiing = new JSkiing();

        long t0 = System.currentTimeMillis();
        final int[][] mountain = jSkiing.loadMap();
        long tf = System.currentTimeMillis() - t0;
        System.out.println("Loaded map in " + tf + " millis");

        jSkiing.visit(mountain);
    }
}
