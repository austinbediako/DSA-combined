package gh.edu.ug.dsaoptimizer.structures;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Test
    void testDijkstraShortestPath() {
        Graph<String> g = new Graph<>();
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
    void testUnreachableAndNulls() {
        Graph<Integer> g = new Graph<>();
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addEdge(1, 2, 1);
        // 3 is isolated
        HashTable<Integer, Double> dist = g.dijkstra(1);
        assertEquals(Double.POSITIVE_INFINITY, dist.get(3));
        assertNull(g.shortestPath(1, 3));

        assertThrows(NullPointerException.class, () -> g.addNode(null));
        assertThrows(NullPointerException.class, () -> g.addEdge(null, 2, 1));
        assertThrows(NullPointerException.class, () -> g.addEdge(1, null, 1));
        assertThrows(IllegalArgumentException.class, () -> g.addEdge(1, 2, -1));
    }

    @Test
    void testHasEdgeAndNeighborsAndRemove() {
        Graph<String> g = new Graph<>();
        g.addEdge("X", "Y", 2.5);
        assertTrue(g.hasNode("X"));
        assertTrue(g.hasNode("Y"));
        assertTrue(g.hasEdge("X", "Y"));
        assertFalse(g.hasEdge("Y", "X"));
        HashTable<String, Double> nbrs = g.neighbors("X");
        assertEquals(1, nbrs.size());
        assertEquals(2.5, nbrs.get("Y"));
        g.removeEdge("X", "Y");
        assertFalse(g.hasEdge("X", "Y"));
    }
}
