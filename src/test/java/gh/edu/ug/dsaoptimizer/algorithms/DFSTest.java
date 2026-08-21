package gh.edu.ug.dsaoptimizer.algorithms;

import gh.edu.ug.dsaoptimizer.structures.Graph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class DFSTest {

    @Test
    void visitsAllReachableNodesFromSource() {
        Graph<String> g = new Graph<>();
        g.addEdge("A", "B", 1);
        g.addEdge("A", "C", 1);
        g.addEdge("B", "D", 1);
        g.addEdge("C", "D", 1);

        Object[] order = DFS.traverse(g, "A");
        assertEquals(4, order.length);
        assertEquals("A", order[0]);
        assertEquals(new HashSet<>(Arrays.asList("A", "B", "C", "D")),
                new HashSet<>(Arrays.asList(order)));
    }

    @Test
    void disconnectedGraphOnlyVisitsReachableComponent() {
        Graph<String> g = new Graph<>();
        g.addEdge("A", "B", 1);
        g.addNode("Z"); // isolated, disconnected from A/B

        Object[] order = DFS.traverse(g, "A");
        assertEquals(2, order.length);
        assertFalse(new HashSet<>(Arrays.asList(order)).contains("Z"));
    }

    @Test
    void unreachablePathReturnsFalse() {
        Graph<String> g = new Graph<>();
        g.addEdge("A", "B", 1);
        g.addNode("Z");

        assertFalse(DFS.isReachable(g, "A", "Z"));
        assertTrue(DFS.isReachable(g, "A", "B"));
        assertTrue(DFS.isReachable(g, "A", "A"));
    }

    @Test
    void singleNodeGraphBoundary() {
        Graph<String> g = new Graph<>();
        g.addNode("A");
        Object[] order = DFS.traverse(g, "A");
        assertArrayEquals(new String[]{"A"}, order);
    }

    @Test
    void handlesCycleWithoutInfiniteLoop() {
        Graph<String> g = new Graph<>();
        g.addEdge("A", "B", 1);
        g.addEdge("B", "A", 1); // cycle
        g.addEdge("B", "C", 1);

        Object[] order = DFS.traverse(g, "A");
        assertEquals(3, order.length);
    }

    @Test
    void nullAndInvalidInputThrows() {
        Graph<String> g = new Graph<>();
        g.addNode("A");
        assertThrows(IllegalArgumentException.class, () -> DFS.traverse(null, "A"));
        assertThrows(IllegalArgumentException.class, () -> DFS.traverse(g, null));
        assertThrows(IllegalArgumentException.class, () -> DFS.traverse(g, "not-in-graph"));
    }
}
