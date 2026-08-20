package gh.edu.ug.dsaoptimizer.structures;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SetTest {

    @Test
    void addAndContainsMembershipUseCase() {
        Set<String> set = new Set<>();
        assertTrue(set.add("Commonwealth Hall"));
        assertTrue(set.add("Legon Hall"));
        assertTrue(set.contains("Commonwealth Hall"));
        assertFalse(set.contains("Volta Hall"));
        assertEquals(2, set.size());
    }

    @Test
    void addingDuplicateReturnsFalseAndDoesNotGrowSize() {
        Set<String> set = new Set<>();
        assertTrue(set.add("A"));
        assertFalse(set.add("A"));
        assertEquals(1, set.size());
    }

    @Test
    void removeReturnsWhetherElementExisted() {
        Set<String> set = new Set<>();
        set.add("A");
        assertTrue(set.remove("A"));
        assertFalse(set.remove("A"));
        assertFalse(set.contains("A"));
    }

    @Test
    void emptySetBoundary() {
        Set<String> set = new Set<>();
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
        assertEquals(0, set.toArray().length);
    }

    @Test
    void toArrayReturnsAllMembers() {
        Set<String> set = new Set<>();
        set.add("A");
        set.add("B");
        set.add("C");
        Object[] arr = set.toArray();
        Arrays.sort(arr);
        assertArrayEquals(new String[]{"A", "B", "C"}, arr);
    }

    @Test
    void nullInputThrows() {
        Set<String> set = new Set<>();
        assertThrows(NullPointerException.class, () -> set.add(null));
        assertThrows(NullPointerException.class, () -> set.contains(null));
        assertThrows(NullPointerException.class, () -> set.remove(null));
    }
}
