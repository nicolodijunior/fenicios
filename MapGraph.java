import grafos_cohen.Graph;
import grafos_cohen.Queue;

public class MapGraph {
    private final Graph graph; // Grafo usando a classe Graph fornecida
    private final char[][] map; // Mapa marítimo
    private final int rows; // Número de linhas do mapa
    private final int cols; // Número de colunas do mapa
    private final int[] postPositions; // Posições dos postos

    public MapGraph(char[][] map) {
        this.rows = map.length;
        this.cols = map[0].length;
        this.graph = new Graph(rows * cols);
        this.map = map;
        this.postPositions = new int[9];
        setPostPositions();
        buildGraph();
    }

    private void buildGraph() {
        int rows = map.length;
        int cols = map[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (map[i][j] != '*') {
                    int v = i * cols + j;

                    // Verificar posições vizinhas (vertical e horizontal)
                    if (i > 0 && map[i - 1][j] != '*') {
                        int w = (i - 1) * cols + j;
                        graph.addEdge(v, w);
                    }
                    if (i < rows - 1 && map[i + 1][j] != '*') {
                        int w = (i + 1) * cols + j;
                        graph.addEdge(v, w);
                    }
                    if (j > 0 && map[i][j - 1] != '*') {
                        int w = i * cols + (j - 1);
                        graph.addEdge(v, w);
                    }
                    if (j < cols - 1 && map[i][j + 1] != '*') {
                        int w = i * cols + (j + 1);
                        graph.addEdge(v, w);
                    }
                }
            }
        }
    }

    private void setPostPositions() {
        int postCount = 0;

        // Percorrer o mapa e armazenar as posições dos postos
        // no vetor postPositions
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char c = map[i][j];
                if (c >= '1' && c <= '9') {
                    postPositions[c - '1'] = i * cols + j;
                    postCount++;
                }
            }
        }

        // Verificar se todos os postos foram encontrados
        if (postCount != 9) {
            throw new RuntimeException("Número de postos inválido!");
        }
    }
    public int[] getPostPositions() {
        return postPositions;
    }

    public Graph getGraph() {
        return graph;
    }

    public char[][] getMap() {
        return map;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }


    public int getFuel(int i, int k) {
        int[] distTo = new int[graph.V()];
        int[] postPositions = getPostPositions();
        boolean[] marked = new boolean[graph.V()];
        int source = postPositions[i-1];
        int target = postPositions[k-1];

        distTo[source] = 0;
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(source);
        marked[source] = true;
        while (!queue.isEmpty()) {
            int v = queue.dequeue();

            if (v == target) {
                // Encontrou o ponto de destino, retorna a menor distância
                return distTo[v];
            }

            boolean differentPost = false;
            for (int j = 0; j < 9; j++) {
                if (v!=source && v == postPositions[j]) {
                    differentPost = true;
                    break;
                }
            }
            if (differentPost)
                continue;

            for (int w : graph.adj(v)) {

                if (!marked[w]) {
                    marked[w] = true;
                    distTo[w] = distTo[v] + 1;
                    queue.enqueue(w);
                }
            }
        }
       return -1;
    }
}