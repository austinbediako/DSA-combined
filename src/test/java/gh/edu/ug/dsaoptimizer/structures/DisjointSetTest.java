package gh.edu.ug.dsaoptimizer.structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisjointSetTest {
    @Test
    void basicUnionFind() {
        DisjointSet<String> ds = new DisjointSet<>();
        ds.makeSet("a"); ds.makeSet("b"); ds.makeSet("c");
        assertFalse(ds.connected("a","b"));
        ds.union("a","b");
        assertTrue(ds.connected("a","b"));
        assertFalse(ds.connected("a","c"));
    }
}
