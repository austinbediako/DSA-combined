package gh.edu.ug.dsaoptimizer.algorithms;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class InsertionSortTest {

    private final Comparator<Integer> intComparator = Integer::compareTo;

    @Test
    void sortsUnsortedArrayOfIntegers() {
        Integer[] input = {29, 10, 14, 37, 13};
        Integer[] expected = {10, 13, 14, 29, 37};
        InsertionSort.sort(input, intComparator);
        assertArrayEquals(expected, input);
    }

    @Test
    void sortsUnsortedArrayOfStrings() {
        String[] input = {"Legon", "Commonwealth", "Volta", "Akuafo"};
        String[] expected = {"Akuafo", "Commonwealth", "Legon", "Volta"};
        InsertionSort.sort(input, String::compareTo);
        assertArrayEquals(expected, input);
    }

    @Test
    void alreadySortedArrayBestCase() {
        Integer[] input = {1, 2, 3, 4, 5};
        InsertionSort.sort(input, intComparator);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, input);
    }

    @Test
    void reverseSortedArrayWorstCase() {
        Integer[] input = {9, 7, 5, 3, 1};
        InsertionSort.sort(input, intComparator);
        assertArrayEquals(new Integer[]{1, 3, 5, 7, 9}, input);
    }

    @Test
    void arrayWithDuplicates() {
        Integer[] input = {4, 1, 4, 2, 1, 3};
        InsertionSort.sort(input, intComparator);
        assertArrayEquals(new Integer[]{1, 1, 2, 3, 4, 4}, input);
    }

    @Test
    void singleElementArrayBoundary() {
        Integer[] input = {42};
        InsertionSort.sort(input, intComparator);
        assertArrayEquals(new Integer[]{42}, input);
    }

    @Test
    void emptyArrayBoundary() {
        Integer[] input = {};
        InsertionSort.sort(input, intComparator);
        assertArrayEquals(new Integer[]{}, input);
    }

    @Test
    void primitiveIntOverloadSorts() {
        int[] input = {5, 3, 8, 1, 9};
        InsertionSort.sort(input);
        assertArrayEquals(new int[]{1, 3, 5, 8, 9}, input);
    }

    @Test
    void nullArrayThrows() {
        assertThrows(IllegalArgumentException.class, () -> InsertionSort.sort((Integer[]) null, intComparator));
        assertThrows(IllegalArgumentException.class, () -> InsertionSort.sort((int[]) null));
    }

    @Test
    void nullComparatorThrows() {
        Integer[] input = {3, 1, 2};
        assertThrows(IllegalArgumentException.class, () -> InsertionSort.sort(input, null));
    }
}
