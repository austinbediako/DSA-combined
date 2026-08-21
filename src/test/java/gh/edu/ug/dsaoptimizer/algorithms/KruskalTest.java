package gh.edu.ug.dsaoptimizer.algorithms;

import gh.edu.ug.dsaoptimizer.structures.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KruskalTest {

    /** Adds the edge in both directions, simulating an undirected road. */
    private static void addUndirected(Graph<String> g, String a, String b, double weight) {
        g.addEdge(a, b, weight);
        g.addEdge(b, a, weight);
    }

    @Test
    void classicFourNodeExampleProducesMinimumSpanningTree() {
        // A-B:1, A-C:4, B-C:2, B-D:5, C-D:1 -- known MST cost = 4
        // (edges A-B, C-D, B-C), matches evidence/trace-tables/kruskal-trace.md
        Graph<String> g = new Graph<>();
        addUndirected(g, "A", "B", 1);
        addUndirected(g, "A", "C", 4);
        addUndirected(g, "B", "C", 2);
        addUndirected(g, "B", "D", 5);
        addUndirected(g, "C", "D", 1);

        MSTResult<String> result = Kruskal.mst(g);

        assertEquals(3, result.edges.length); // V-1 = 4-1 = 3
        assertEquals(4.0, result.totalCost);
    }

    @Test
    void disconnectedGraphProducesSpanningForestNotSingleTree() {
        Graph<String> g = new Graph<>();
        addUndirected(g, "A", "B", 1);
        g.addNode("Z"); // isolated -- disconnected from A/B

        MSTResult<String> result = Kruskal.mst(g);

        // Only the A-B edge can be included; Z stays its own component.
        assertEquals(1, result.edges.length);
        assertEquals(1.0, result.totalCost);
    }

    @Test
    void emptyGraphBoundary() {
        Graph<String> g = new Graph<>();
        MSTResult<String> result = Kruskal.mst(g);
        assertEquals(0, result.edges.length);
        assertEquals(0.0, result.totalCost);
    }

    @Test
    void singleNodeBoundary() {
        Graph<String> g = new Graph<>();
        g.addNode("A");
        MSTResult<String> result = Kruskal.mst(g);
        assertEquals(0, result.edges.length);
        assertEquals(0.0, result.totalCost);
    }

    @Test
    void nullGraphThrows() {
        assertThrows(IllegalArgumentException.class, () -> Kruskal.mst(null));
    }
}
