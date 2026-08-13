package gh.edu.ug.dsaoptimizer.structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest {

    @Test
    void testAddAndGet() {
        DynamicArray<Integer> arr = new DynamicArray<>();
        for (int i = 0; i < 100; i++) {
            arr.add(i);
        }
        assertEquals(100, arr.size());
        for (int i = 0; i < 100; i++) {
            assertEquals(i, arr.get(i));
        }
    }

    @Test
    void testAddAtIndex() {
        DynamicArray<String> arr = new DynamicArray<>();
        arr.add("a");
        arr.add("c");
        arr.add(1, "b"); // a, b, c
        assertEquals(3, arr.size());
        assertEquals("a", arr.get(0));
        assertEquals("b", arr.get(1));
        assertEquals("c", arr.get(2));

        arr.add(0, "start");
        assertEquals("start", arr.get(0));
        assertEquals("a", arr.get(1));
    }

    @Test
    void testSetAndRemove() {
        DynamicArray<String> arr = new DynamicArray<>();
        arr.add("x");
        arr.add("y");
        arr.add("z");
        assertEquals("y", arr.set(1, "Y"));
        assertEquals("Y", arr.get(1));
        assertEquals("x", arr.remove(0));
        assertEquals(2, arr.size());
        assertEquals("Y", arr.get(0));
    }

    @Test
    void testBounds() {
        DynamicArray<Integer> arr = new DynamicArray<>();
        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.add(-1, 5));
        arr.add(1); // after this size==1
        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(2));
    }

    @Test
    void testNullsDisallowed() {
        DynamicArray<String> arr = new DynamicArray<>();
        assertThrows(NullPointerException.class, () -> arr.add(null));
        arr.add("ok");
        assertThrows(NullPointerException.class, () -> arr.set(0, null));
    }

    @Test
    void testClearAndIsEmpty() {
        DynamicArray<Integer> arr = new DynamicArray<>();
        arr.add(1);
        arr.add(2);
        arr.clear();
        assertTrue(arr.isEmpty());
        assertEquals(0, arr.size());
        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(0));
    }
}
