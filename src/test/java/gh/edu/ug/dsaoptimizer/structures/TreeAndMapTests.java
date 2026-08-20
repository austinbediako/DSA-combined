package gh.edu.ug.dsaoptimizer.structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreeAndMapTests {
    @Test
    void bstMapBasic() {
        BSTMap<Integer,String> m = new BSTMap<>();
        assertNull(m.put(5, "five"));
        assertNull(m.put(3, "three"));
        assertNull(m.put(7, "seven"));
        assertEquals("five", m.get(5));
        Object[] keys = m.keysInOrder();
        assertArrayEquals(new Integer[]{3,5,7}, keys);
    }

    @Test
    void redBlackBasic() {
        RedBlackTreeMap<Integer,String> m = new RedBlackTreeMap<>();
        m.put(10, "x"); m.put(5,"a"); m.put(15,"b");
        assertEquals("x", m.get(10));
        assertTrue(m.containsKey(5));
        assertFalse(m.containsKey(99));
    }

    @Test
    void btreeBasic() {
        BTreeMap<Integer,String> b = new BTreeMap<>(3);
        b.put(10,"x"); b.put(20,"y"); b.put(5,"a"); b.put(6,"b"); b.put(12,"c");
        assertEquals("x", b.get(10));
        assertNull(b.get(999));
        Object[] keys = b.keysInOrder();
        assertTrue(contains(keys, 5)); assertTrue(contains(keys, 20));
    }

    @Test
    void hashWrappers() {
        HashMapWrapper<String,Integer> hm = new HashMapWrapper<>();
        assertNull(hm.put("a", 1)); assertEquals(1, hm.get("a"));
        HashSetWrapper<String> hs = new HashSetWrapper<>();
        assertTrue(hs.add("z")); assertTrue(hs.contains("z"));
    }

    @Test
    void treeWrappers() {
        TreeMapWrapper<Integer,String> tm = new TreeMapWrapper<>();
        assertNull(tm.put(1,"one")); assertEquals("one", tm.get(1));
        TreeSetWrapper<Integer> ts = new TreeSetWrapper<>(); assertTrue(ts.add(2)); assertTrue(ts.contains(2));
    }

    private static boolean contains(Object[] array, Object value) {
        for (Object o : array) {
            if (o.equals(value)) return true;
        }
        return false;
    }
}
