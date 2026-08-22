package gh.edu.ug.dsaoptimizer.structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    @Test
    void testPutGetRemove() {
        HashTable<String, Integer> ht = new HashTable<>();
        assertNull(ht.put("a", 1));
        assertEquals(1, ht.get("a"));
        assertTrue(ht.containsKey("a"));
        assertEquals(1, ht.remove("a"));
        assertNull(ht.get("a"));
        assertFalse(ht.containsKey("a"));
    }

    @Test
    void testOverwriteAndSize() {
        HashTable<String, String> ht = new HashTable<>();
        ht.put("k", "v1");
        assertEquals("v1", ht.put("k", "v2"));
        assertEquals("v2", ht.get("k"));
        assertEquals(1, ht.size());
    }

    @Test
    void testResizeAndManyEntries() {
        HashTable<Integer, Integer> ht = new HashTable<>();
        int n = 1000;
        for (int i = 0; i < n; i++) {
            ht.put(i, i * 2);
        }
        assertEquals(n, ht.size());
        for (int i = 0; i < n; i++) {
            assertEquals(i * 2, ht.get(i));
        }
    }

    @Test
    void testNullsDisallowed() {
        HashTable<String, Integer> ht = new HashTable<>();
        assertThrows(NullPointerException.class, () -> ht.put(null, 1));
        assertThrows(NullPointerException.class, () -> ht.put("k", null));
        assertThrows(NullPointerException.class, () -> ht.get(null));
        assertThrows(NullPointerException.class, () -> ht.remove(null));
    }

    @Test
    void testCustomInitialCapacity() {
        HashTable<Integer, Integer> ht = new HashTable<>(4);
        assertEquals(4, ht.capacity());
        assertThrows(IllegalArgumentException.class, () -> new HashTable<Integer, Integer>(0));
    }

    @Test
    void testLoadFactorReflectsSizeOverCapacity() {
        HashTable<Integer, Integer> ht = new HashTable<>(10);
        ht.setAutoResize(false);
        for (int i = 0; i < 5; i++) ht.put(i, i);
        assertEquals(0.5, ht.loadFactor(), 0.0001);
    }

    @Test
    void testCollisionCountIncreasesAsTableFillsUpWithAutoResizeDisabled() {
        HashTable<Integer, Integer> ht = new HashTable<>(4);
        ht.setAutoResize(false);
        assertEquals(0, ht.collisionCount());
        // Insert far more keys than buckets, forced to collide since resize is off.
        for (int i = 0; i < 20; i++) ht.put(i, i);
        assertEquals(4, ht.capacity()); // never resized
        assertTrue(ht.collisionCount() > 0);
        assertEquals(20, ht.size());
        // Every key must still be retrievable despite the collisions.
        for (int i = 0; i < 20; i++) assertEquals(i, ht.get(i));
    }

    @Test
    void testAutoResizeStillWorksByDefault() {
        HashTable<Integer, Integer> ht = new HashTable<>(4);
        for (int i = 0; i < 20; i++) ht.put(i, i);
        assertTrue(ht.capacity() > 4); // grew automatically
    }
}
