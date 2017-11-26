import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class Maze {
    private final int width, height;
    private final Node[][] nodes;
    private final int startX, endX;

    public Maze(int width, int height, int startX, int endX) {
        this.width = width;
        this.height = height;
        this.startX = startX;   // entry node is nodes[startX, 0]
        this.endX = endX;       // exit node is nodes[endX, height-1]

        nodes = new Node[width][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                nodes[x][y] = new Node();

        // Generate maze by connecting adjacent nodes randomly.
        Random gen = new Random();
        while (!isConnected()) {
            // Pick a node not on the outer wall. (Between 1 and length-2, inclusive)
            int nodeX = gen.nextInt(width-2) + 1;
            int nodeY = gen.nextInt(height-2) + 1;

            // Connect randomly with node above, below, left, or right.
            int offsetX, offsetY;
            do {
                // Generate x and y offset between -1 and 1, inclusive.
                offsetX = gen.nextInt(3)-1;
                offsetY = gen.nextInt(3)-1;
            } while (offsetX == 0 && offsetY == 0);

            connect(nodeX, nodeY, nodeX+offsetX, nodeY+offsetY);
        }
    }

    /**
     * Perform DFS to determine if the maze is connected.
     */
    private boolean isConnected() {
        // Set all nodes to unvisited.
        boolean visited[][] = new boolean[width][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                visited[x][y] = false;

        // Mark each connected node as visited.
        visitConnected(startX, 0, visited);

        // Check if each node was visited in DFS.
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                if (!visited[x][y])
                    return false;

        return true;
    }

    private void visitConnected(int x, int y, boolean[][] visited) {
        visited[x][y] = true;   // Mark this node as visited.

        Iterator<Coordinate> adjacencyList = nodes[x][y].getAdjacencyList();
        while (adjacencyList.hasNext()) {
            System.out.println("Visiting adjacent to [" + x + "][" + y + "]...");
            Coordinate next = adjacencyList.next();

            if (!visited[next.getX()][next.getY()]) {
                System.out.println("   [" + next.getX() + "][" + next.getY() + "]");
                visitConnected(next.getX(), next.getY(), visited);
            }
        }
    }

    private void connect(int node1X, int node1Y, int node2X, int node2Y) {
        nodes[node1X][node1Y].addEdge(node2X, node2Y);
        nodes[node2X][node2Y].addEdge(node1X, node1Y);
    }

    public String toString() {
        StringBuilder string = new StringBuilder();

        /*// Draw top row.
        string.append('#');  // Add left border.
        for (int x = 0; x < width; x++)
            string.append((x == startX)? "+#" : "##");
        string.append('\n');
        */

        // Draw middle rows.
        for (int y = 0; y < height; y++) {
            string.append(' ');  // Add left border.
            for (int x = 0; x < width; x++) {
                if (x < width-1 && nodes[x][y].isConnectedTo(x+1, y))
                    string.append("+-");
                else
                    string.append("+ ");
            }
            string.append('\n'); // Move onto next row.

            string.append(' ');  // Add left border.
            for (int x = 0; x < width; x++) {
                if (y < height-1 && nodes[x][y].isConnectedTo(x, y+1))
                    string.append("| ");
                else
                    string.append("  ");
            }
            string.append('\n'); // Move onto next row.
        }


        return string.toString();
    }

    public static void main(String[] args) {
        Maze maze = new Maze(10, 10, 5, 5);

        try {
            PrintWriter writer = new PrintWriter(new File("output.txt"));
            writer.println(maze.toString());
            writer.flush();
            writer.close();
        }
        catch (Exception e) { e.printStackTrace(); }
    }
}
