import java.util.ArrayList;
import java.util.Iterator;

public class Node {
    private ArrayList<Coordinate> adjacencyList = new ArrayList<>();

    public void addEdge(int x, int y) {
        Coordinate coord = new Coordinate(x, y);
        if (!contains(adjacencyList, coord))
            adjacencyList.add(coord);
    }

    private boolean contains(ArrayList<Coordinate> adjacencyList, Coordinate coord) {
        for (int i = 0; i < adjacencyList.size(); i++) {
            Coordinate current = adjacencyList.get(i);
            if (current.getX() == coord.getX() && current.getY() == coord.getY())
                return true;
        }

        return false;
    }

    public Iterator<Coordinate> getAdjacencyList() {
        return adjacencyList.iterator();
    }

    public boolean isConnectedTo(int x, int y) {
        return adjacencyList.contains(new Coordinate(x, y));
    }
}
