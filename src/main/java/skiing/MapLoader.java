package skiing;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MapLoader {
    public static final String datum = "./src/main/scala/mountain1000x1000.txt";

    public static int[][] loadMap() throws Exception {
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
}
