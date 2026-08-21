package gh.edu.ug.dsaoptimizer.algorithms;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class QuickSortTest {

    private final Comparator<Integer> intComparator = Integer::compareTo;

    @Test
    void sortsUnsortedArrayOfIntegers() {
        Integer[] input = {38, 27, 43, 3, 9, 82, 10};
        Integer[] expected = {3, 9, 10, 27, 38, 43, 82};
        QuickSort.sort(input, intComparator);
        assertArrayEquals(expected, input);
    }

    @Test
    void sortsUnsortedArrayOfStrings() {
        String[] input = {"Legon", "Commonwealth", "Volta", "Akuafo", "Balme"};
        String[] expected = {"Akuafo", "Balme", "Commonwealth", "Legon", "Volta"};
        QuickSort.sort(input, String::compareTo);
        assertArrayEquals(expected, input);
    }

    @Test
    void alreadySortedArrayIsWorstCaseButStillCorrect() {
        Integer[] input = {1, 2, 3, 4, 5};
        QuickSort.sort(input, intComparator);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, input);
    }

    @Test
    void reverseSortedArray() {
        Integer[] input = {9, 7, 5, 3, 1};
        QuickSort.sort(input, intComparator);
        assertArrayEquals(new Integer[]{1, 3, 5, 7, 9}, input);
    }

    @Test
    void arrayWithDuplicates() {
        Integer[] input = {4, 1, 4, 2, 1, 3};
        QuickSort.sort(input, intComparator);
        assertArrayEquals(new Integer[]{1, 1, 2, 3, 4, 4}, input);
    }

    @Test
    void singleElementArrayBoundary() {
        Integer[] input = {42};
        QuickSort.sort(input, intComparator);
        assertArrayEquals(new Integer[]{42}, input);
    }

    @Test
    void emptyArrayBoundary() {
        Integer[] input = {};
        QuickSort.sort(input, intComparator);
        assertArrayEquals(new Integer[]{}, input);
    }

    @Test
    void primitiveIntOverloadSorts() {
        int[] input = {5, 3, 8, 1, 9};
        QuickSort.sort(input);
        assertArrayEquals(new int[]{1, 3, 5, 8, 9}, input);
    }

    @Test
    void nullArrayThrows() {
        assertThrows(IllegalArgumentException.class, () -> QuickSort.sort((Integer[]) null, intComparator));
        assertThrows(IllegalArgumentException.class, () -> QuickSort.sort((int[]) null));
    }

    @Test
    void nullComparatorThrows() {
        Integer[] input = {3, 1, 2};
        assertThrows(IllegalArgumentException.class, () -> QuickSort.sort(input, null));
    }
}
