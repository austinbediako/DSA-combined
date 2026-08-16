package gh.edu.ug.dsaoptimizer.algorithms;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BinarySearchTest {

    // ---- normal cases ----

    @Test
    void findsTargetInMiddleOfArray() {
        int[] array = {1, 3, 5, 7, 9, 11, 13};
        assertEquals(3, BinarySearch.search(array, 7));
    }

    @Test
    void findsTargetAtFirstIndex() {
        int[] array = {2, 4, 6, 8, 10};
        assertEquals(0, BinarySearch.search(array, 2));
    }

    @Test
    void findsTargetAtLastIndex() {
        int[] array = {2, 4, 6, 8, 10};
        assertEquals(4, BinarySearch.search(array, 10));
    }

    @Test
    void returnsMinusOneWhenTargetAbsent() {
        int[] array = {1, 3, 5, 7, 9};
        assertEquals(-1, BinarySearch.search(array, 4));
    }

    // ---- boundary cases ----

    @Test
    void emptyArrayReturnsMinusOne() {
        int[] array = {};
        assertEquals(-1, BinarySearch.search(array, 1));
    }

    @Test
    void singleElementArrayFindsMatch() {
        int[] array = {42};
        assertEquals(0, BinarySearch.search(array, 42));
    }

    @Test
    void singleElementArrayMissReturnsMinusOne() {
        int[] array = {42};
        assertEquals(-1, BinarySearch.search(array, 7));
    }

    @Test
    void targetBelowRangeReturnsMinusOne() {
        int[] array = {5, 10, 15, 20};
        assertEquals(-1, BinarySearch.search(array, 1));
    }

    @Test
    void targetAboveRangeReturnsMinusOne() {
        int[] array = {5, 10, 15, 20};
        assertEquals(-1, BinarySearch.search(array, 100));
    }

    // ---- duplicate keys ----

    @Test
    void findsAValidIndexWhenDuplicatesPresent() {
        int[] array = {1, 2, 2, 2, 3, 4};
        int index = BinarySearch.search(array, 2);
        assertEquals(2, array[index]);
    }

    // ---- invalid input ----

    @Test
    void nullArrayThrows() {
        assertThrows(IllegalArgumentException.class, () -> BinarySearch.search(null, 1));
    }

    /**
     * Counterexample (brief §10): binary search's precondition is that the
     * input array is sorted ascending. This implementation does not (and
     * cannot cheaply) validate that precondition, so running it on unsorted
     * input silently produces an unreliable result instead of throwing.
     *
     * <p>Array is deliberately unsorted; the target (99) is present at
     * index 0, but binary search's first probe lands in the middle of the
     * array and, following the algorithm's comparisons on this unsorted
     * data, never revisits index 0 — demonstrating that correctness
     * depends entirely on the caller upholding the sorted precondition.
     */
    @Test
    void unsortedInputViolatesPreconditionAndGivesWrongAnswer() {
        int[] unsorted = {99, 2, 47, 3, 15, 8, 4};
        int result = BinarySearch.search(unsorted, 99);
        assertNotEquals(0, result, "binary search should NOT reliably find "
                + "the target on unsorted input -- this demonstrates why the "
                + "sorted precondition matters, it is not a bug in the search "
                + "logic itself");
    }
}
