package gh.edu.ug.dsaoptimizer.algorithms;

import gh.edu.ug.dsaoptimizer.structures.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimTest {

    /** Adds the edge in both directions, simulating an undirected road. */
    private static void addUndirected(Graph<String> g, String a, String b, double weight) {
        g.addEdge(a, b, weight);
        g.addEdge(b, a, weight);
    }

    @Test
    void classicFourNodeExampleMatchesKruskalTotalCost() {
        // Same graph as KruskalTest -- both algorithms must agree on the
        // minimum total cost (4), even though the specific edge set can
        // differ when multiple MSTs of equal cost exist.
        Graph<String> g = new Graph<>();
        addUndirected(g, "A", "B", 1);
        addUndirected(g, "A", "C", 4);
        addUndirected(g, "B", "C", 2);
        addUndirected(g, "B", "D", 5);
        addUndirected(g, "C", "D", 1);

        MSTResult<String> result = Prim.mst(g, "A");

        assertEquals(3, result.edges.length);
        assertEquals(4.0, result.totalCost);
    }

    @Test
    void disconnectedGraphOnlySpansReachableComponent() {
        Graph<String> g = new Graph<>();
        addUndirected(g, "A", "B", 1);
        g.addNode("Z"); // isolated -- unreachable from A

        MSTResult<String> result = Prim.mst(g, "A");

        assertEquals(1, result.edges.length);
        assertEquals(1.0, result.totalCost);
    }

    @Test
    void singleNodeBoundary() {
        Graph<String> g = new Graph<>();
        g.addNode("A");
        MSTResult<String> result = Prim.mst(g, "A");
        assertEquals(0, result.edges.length);
        assertEquals(0.0, result.totalCost);
    }

    @Test
    void nullAndInvalidInputThrows() {
        Graph<String> g = new Graph<>();
        g.addNode("A");
        assertThrows(IllegalArgumentException.class, () -> Prim.mst(null, "A"));
        assertThrows(IllegalArgumentException.class, () -> Prim.mst(g, null));
        assertThrows(IllegalArgumentException.class, () -> Prim.mst(g, "not-in-graph"));
    }
}
