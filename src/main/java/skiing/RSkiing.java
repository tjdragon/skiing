package skiing;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Skiing: a coding diversion
 *
 * http://geeks.redmart.com/2015/01/07/skiing-in-singapore-a-coding-diversion/
 *
 * CIO Solution (https://github.com/mgorriz) with Real World Physics
 *
 * - The data map remains the same, a 1000x1000 grid of heights in meters
 * - We will consider that each "square" is a 5x5, therefore the distance travelled between two peaks is 10 meters
 * - You can only move down if the angle of the slope is greater than the minimum angle from using the kinetic
 *   friction coeffecient
 *  - The final speed is the initial speed for the next square
 *  - You loose 20% of the initial speed on a turn
 *
 * @author TJ
 * @see Physics
 */
public class RSkiing {
    // Min slope angle to go down considering the static kinetic friction factor
    private final double minSlopeAngle = Physics.minSlopeAngle(Physics.SKF);
    private List<Coordinate> bestPath = null;

    public void visit(final int[][] mountain) {
        final int w = mountain.length;
        final int h = mountain[0].length;

        for(int i = 0 ; i < w; i++) {
            for(int j = 0; j < h; j++) {
                final long t0 = System.currentTimeMillis();
                final Coordinate from = new Coordinate(i, j);
                visit(from, mountain, new Stack<>(), new HashSet<>());
                final long tf = (System.currentTimeMillis() - t0);
                System.out.println("Visited " + i + " / " + j + " in " + tf + " millis");
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

        final List<Coordinate> children = children(from, mountain);

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

    // v1: don't consider straight lines or turns for 20% redux - to do later
    private List<Coordinate> children(final Coordinate from, final int[][] mountain) {
        // Let's get coordinates within the mountain
        final List<Coordinate> validCoordinates = validCoordinates(from, mountain);
        final List<Coordinate> validChildren = new LinkedList<>();
        from.value = mountain[from.x][from.y];

        for(Coordinate to : validCoordinates) {
            to.value = mountain[to.x][to.y];
            final double slopeAngle = Math.abs(Physics.slopeAngle(from.value - to.value));
            final double slopeLength = Physics.slopeLength(from.value - to.value, Physics.DEFAULT_LENGTH);
//            System.out.println(to + " angle: " + slopeAngle + ", length: " + slopeLength);
            if (to.value <= from.value) {
                // Going downhill
                if (slopeAngle >= minSlopeAngle) {
                    final double acceleration = Physics.acceleration(slopeAngle, Physics.SKF, Orientation.DOWN);
                    final double finalSpeed = Physics.finalSpeed(from.speed, acceleration, slopeLength);
                    to.speed = finalSpeed;
                    validChildren.add(to);
                }
            } else {
                // Going uphill
                final double acceleration = Physics.acceleration(slopeAngle, Physics.SKF, Orientation.UP);
                final double finalSpeed = Physics.finalSpeed(from.speed, acceleration, slopeLength);
                if (!Double.isNaN(finalSpeed)) {
                    to.speed = finalSpeed;
                    validChildren.add(to);
//                    System.out.println("UP: " + to + " angle: " + slopeAngle + ", length: " + slopeLength);
                }
            }
        }

        return validChildren;
    }

    private List<Coordinate> validCoordinates(final Coordinate from, final int[][] mountain) {
        final List<Coordinate> coords = new LinkedList<>();
        final int s = mountain.length;

        coords.add(new Coordinate(from.x + 1, from.y));
        coords.add(new Coordinate(from.x - 1, from.y));
        coords.add(new Coordinate(from.x, from.y + 1));
        coords.add(new Coordinate(from.x, from.y - 1 ));

        final List<Coordinate> validCoords = coords.stream()
                                                   .filter(e -> e.x >= 0 && e.y >= 0 && e.x < s && e.y < s)
                                                   .collect(Collectors.toList());
        return validCoords;
    }

    public static void main(String[] args) throws Exception {
        final RSkiing jSkiing = new RSkiing();

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
