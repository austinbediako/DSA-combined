package gh.edu.ug.dsaoptimizer.structures;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphMatrixTest {

    @Test
    void testDijkstraShortestPathMatrix() {
        GraphMatrix<String> g = new GraphMatrix<>();
        g.addEdge("A", "B", 1);
        g.addEdge("A", "C", 4);
        g.addEdge("B", "C", 2);
        g.addEdge("B", "D", 5);
        g.addEdge("C", "D", 1);

        HashTable<String, Double> dist = g.dijkstra("A");
        assertEquals(0.0, dist.get("A"));
        assertEquals(1.0, dist.get("B"));
        assertEquals(3.0, dist.get("C"));
        assertEquals(4.0, dist.get("D"));

        List<String> path = g.shortestPath("A", "D");
        assertNotNull(path);
        assertArrayEquals(new String[]{"A", "B", "C", "D"}, path.toArray());
    }

    @Test
    void testHasEdgeAndRemoveMatrix() {
        GraphMatrix<String> g = new GraphMatrix<>();
        g.addEdge("X", "Y", 2.5);
        assertTrue(g.hasNode("X"));
        assertTrue(g.hasNode("Y"));
        assertTrue(g.hasEdge("X", "Y"));
        assertFalse(g.hasEdge("Y", "X"));
        g.removeEdge("X", "Y");
        assertFalse(g.hasEdge("X", "Y"));
    }
}
