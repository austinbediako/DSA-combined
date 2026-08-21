package gh.edu.ug.dsaoptimizer.algorithms;

import gh.edu.ug.dsaoptimizer.structures.Graph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class BFSTest {

    @Test
    void visitsAllReachableNodesFromSource() {
        Graph<String> g = new Graph<>();
        g.addEdge("A", "B", 1);
        g.addEdge("A", "C", 1);
        g.addEdge("B", "D", 1);
        g.addEdge("C", "D", 1);

        Object[] order = BFS.traverse(g, "A");
        assertEquals(4, order.length);
        assertEquals("A", order[0]); // source visited first
        // B and C are both at distance 1, D is at distance 2 -- must come last
        assertEquals("D", order[3]);
    }

    @Test
    void disconnectedGraphOnlyVisitsReachableComponent() {
        Graph<String> g = new Graph<>();
        g.addEdge("A", "B", 1);
        g.addNode("Z"); // isolated, disconnected from A/B

        Object[] order = BFS.traverse(g, "A");
        assertEquals(2, order.length);
        assertFalse(new HashSet<>(Arrays.asList(order)).contains("Z"));
    }

    @Test
    void unreachablePathReturnsFalse() {
        Graph<String> g = new Graph<>();
        g.addEdge("A", "B", 1);
        g.addNode("Z");

        assertFalse(BFS.isReachable(g, "A", "Z"));
        assertTrue(BFS.isReachable(g, "A", "B"));
        assertTrue(BFS.isReachable(g, "A", "A"));
    }

    @Test
    void singleNodeGraphBoundary() {
        Graph<String> g = new Graph<>();
        g.addNode("A");
        Object[] order = BFS.traverse(g, "A");
        assertArrayEquals(new String[]{"A"}, order);
    }

    @Test
    void nullAndInvalidInputThrows() {
        Graph<String> g = new Graph<>();
        g.addNode("A");
        assertThrows(IllegalArgumentException.class, () -> BFS.traverse(null, "A"));
        assertThrows(IllegalArgumentException.class, () -> BFS.traverse(g, null));
        assertThrows(IllegalArgumentException.class, () -> BFS.traverse(g, "not-in-graph"));
    }
}
