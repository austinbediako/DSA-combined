package gh.edu.ug.dsaoptimizer.algorithms;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BinarySearchTest {

    // ---- normal cases ----

    @Test
    void findsTargetAtBeginning() {
        int[] array = {1, 3, 5, 7, 9};
        assertEquals(0, BinarySearch.search(array, 1));
    }

    @Test
    void findsTargetInMiddle() {
        int[] array = {1, 3, 5, 7, 9};
        assertEquals(2, BinarySearch.search(array, 5));
    }

    @Test
    void findsTargetAtEnd() {
        int[] array = {1, 3, 5, 7, 9};
        assertEquals(4, BinarySearch.search(array, 9));
    }

    @Test
    void returnsMinusOneWhenTargetAbsent() {
        int[] array = {1, 3, 5, 7, 9};
        assertEquals(-1, BinarySearch.search(array, 6));
    }

    // ---- boundary cases ----

    @Test
    void emptyArrayReturnsMinusOne() {
        int[] array = {};
        assertEquals(-1, BinarySearch.search(array, 5));
    }

    @Test
    void singleElementArrayFindsMatch() {
        int[] array = {42};
        assertEquals(0, BinarySearch.search(array, 42));
    }

    @Test
    void singleElementArrayMissReturnsMinusOne() {
        int[] array = {42};
        assertEquals(-1, BinarySearch.search(array, 99));
    }

    // ---- duplicate values ----

    @Test
    void findsTargetWhenDuplicatesArePresent() {
        int[] array = {1, 2, 2, 2, 5};
        int result = BinarySearch.search(array, 2);

        // Binary search may return any valid occurrence.
       org.junit.jupiter.api.Assertions.assertTrue(
        result >= 1 && result <= 3
);
assertEquals(2, array[result]);
    }

    // ---- negative values ----

    @Test
    void findsNegativeTarget() {
        int[] array = {-10, -5, -2, 0, 4, 8};
        assertEquals(1, BinarySearch.search(array, -5));
    }

    // ---- invalid input ----

        @Test
    void nullArrayThrows() {
        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> BinarySearch.search(null, 5)
        );
    }

    // ---- additional edge cases ----
    @Test
    void findsTargetInTwoElementArray() {
        int[] array = {10, 20};
        assertEquals(1, BinarySearch.search(array, 20));
    }

    @Test
    void findsFirstElementInLargeArray() {
        int[] array = new int[100];

        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }

        assertEquals(0, BinarySearch.search(array, 0));
    }

    @Test
    void findsLastElementInLargeArray() {
        int[] array = new int[100];

        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }

        assertEquals(99, BinarySearch.search(array, 99));
    }

    @Test
    void returnsMinusOneWhenTargetIsSmallerThanAllElements() {
        int[] array = {10, 20, 30, 40, 50};
        assertEquals(-1, BinarySearch.search(array, 5));
    }

    @Test
    void returnsMinusOneWhenTargetIsGreaterThanAllElements() {
        int[] array = {10, 20, 30, 40, 50};
        assertEquals(-1, BinarySearch.search(array, 55));
    }

    @Test
    void findsZeroInArrayContainingNegativeAndPositiveValues() {
        int[] array = {-20, -10, -5, 0, 5, 10, 20};
        assertEquals(3, BinarySearch.search(array, 0));
    }

    @Test
    void findsTargetInLargerSortedArray() {
        int[] array = new int[1000];

        for (int i = 0; i < array.length; i++) {
            array[i] = i * 2;
        }

        assertEquals(500, BinarySearch.search(array, 1000));
    }
}