

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {

        String folderPath = "casos-cohen";

        File folder = new File(folderPath);

        if (!folder.isDirectory()) {
            System.out.println("A pasta " + folderPath + " não existe!");
            return;
        }

        File[] files = folder.listFiles();
        assert files != null;
        Arrays.sort(files);

        for (File file : files) {
            if (file.isFile()) {
                List<String> lines = readFile(file.getPath());

                String[] info = lines.get(0).split(" ");
                int rows = Integer.parseInt(info[0]);
                int columns = Integer.parseInt(info[1]);

                char[][] map = new char[rows][columns];

                for (int i = 0; i < rows; i++) {
                    String line = lines.get(i + 1);
                    for (int j = 0; j < columns; j++) {
                        map[i][j] = line.charAt(j);
                    }
                }
                MapGraph mapGraph = new MapGraph(map);

                final int PRIMEIRO_PORTO = 1;
                final int ULTIMO_PORTO = 9;

                int combustivel = 0;
                int i = PRIMEIRO_PORTO;
                for (int k = PRIMEIRO_PORTO+1; k <= ULTIMO_PORTO; k++) {
                    int custo = mapGraph.getFuel(i, k);
                    if (custo >= 0) {
                        i = k;
                        combustivel += custo;
                    }
                }
                combustivel += mapGraph.getFuel(i, PRIMEIRO_PORTO);
                System.out.println(file.getName());
                System.out.println("Combustível necessário: " + combustivel);
            }
        }
    }


    public static List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
}