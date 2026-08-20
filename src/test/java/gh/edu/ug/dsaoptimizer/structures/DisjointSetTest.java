package gh.edu.ug.dsaoptimizer.structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisjointSetTest {

    @Test
    void makeSetAndFindEachElementIsItsOwnRootInitially() {
        DisjointSet<String> ds = new DisjointSet<>();
        ds.makeSet("A");
        ds.makeSet("B");
        assertEquals("A", ds.find("A"));
        assertEquals("B", ds.find("B"));
        assertEquals(2, ds.setCount());
    }

    @Test
    void unionMergesTwoSetsAndFindAgrees() {
        DisjointSet<String> ds = new DisjointSet<>();
        ds.makeSet("A");
        ds.makeSet("B");
        assertTrue(ds.union("A", "B"));
        assertEquals(ds.find("A"), ds.find("B"));
        assertEquals(1, ds.setCount());
    }

    @Test
    void unionOnAlreadyConnectedElementsReturnsFalse() {
        DisjointSet<String> ds = new DisjointSet<>();
        ds.makeSet("A");
        ds.makeSet("B");
        ds.union("A", "B");
        assertFalse(ds.union("A", "B")); // already same set -- would form a cycle
        assertEquals(1, ds.setCount());
    }

    @Test
    void kruskalConnectivityTraceFourNodesThreeEdges() {
        // Mirrors evidence/trace-tables/kruskal-trace.md: A-B, B-C, C-D
        // should union into a single connected component with no cycle.
        DisjointSet<String> ds = new DisjointSet<>();
        for (String node : new String[]{"A", "B", "C", "D"}) ds.makeSet(node);
        assertEquals(4, ds.setCount());

        assertTrue(ds.union("A", "B"));
        assertEquals(3, ds.setCount());
        assertTrue(ds.union("B", "C"));
        assertEquals(2, ds.setCount());
        assertTrue(ds.union("C", "D"));
        assertEquals(1, ds.setCount());

        // A-D edge would now close a cycle -- union must report false.
        assertFalse(ds.union("A", "D"));
        assertEquals(1, ds.setCount());
    }

    @Test
    void pathCompressionFlattensChainAfterFind() {
        DisjointSet<Integer> ds = new DisjointSet<>();
        for (int i = 1; i <= 5; i++) ds.makeSet(i);
        // build a chain 1<-2<-3<-4<-5 via successive unions
        ds.union(1, 2);
        ds.union(2, 3);
        ds.union(3, 4);
        ds.union(4, 5);

        int root = ds.find(1);
        // after find(1), every element should point directly to the same root
        for (int i = 1; i <= 5; i++) {
            assertEquals(root, ds.find(i));
        }
    }

    @Test
    void findOnUnknownElementThrows() {
        DisjointSet<String> ds = new DisjointSet<>();
        assertThrows(IllegalArgumentException.class, () -> ds.find("ghost"));
    }

    @Test
    void nullInputThrows() {
        DisjointSet<String> ds = new DisjointSet<>();
        assertThrows(NullPointerException.class, () -> ds.makeSet(null));
        assertThrows(NullPointerException.class, () -> ds.find(null));
        assertThrows(NullPointerException.class, () -> ds.contains(null));
    }
}
