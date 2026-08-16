package gh.edu.ug.dsaoptimizer.algorithms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class MergeSortTest {

    private final Comparator<Integer> intComparator = Integer::compareTo;
    private final Comparator<String> stringComparator = String::compareTo;

    @Test
    @DisplayName("Normal Case: Sorts an unsorted array of integers")
    public void testNormalUnsortedArray() {
        Integer[] input = { 38, 27, 43, 3, 9, 82, 10 };
        Integer[] expected = { 3, 9, 10, 27, 38, 43, 82 };

        MergeSort.sort(input, intComparator);
        assertArrayEquals(expected, input);
    }

    @Test
    @DisplayName("Normal Case: Sorts an unsorted array of strings")
    public void testNormalUnsortedStrings() {
        String[] input = { "Legon", "Commonwealth", "Volta", "Akuafo", "Balme" };
        String[] expected = { "Akuafo", "Balme", "Commonwealth", "Legon", "Volta" };

        MergeSort.sort(input, stringComparator);
        assertArrayEquals(expected, input);
    }

    @Test
    @DisplayName("Boundary Case: Single element array remains unchanged")
    public void testSingleElementArray() {
        Integer[] input = { 42 };
        Integer[] expected = { 42 };

        MergeSort.sort(input, intComparator);
        assertArrayEquals(expected, input);
    }

    @Test
    @DisplayName("Boundary Case: Empty array handles without errors")
    public void testEmptyArray() {
        Integer[] input = {};
        Integer[] expected = {};

        MergeSort.sort(input, intComparator);
        assertArrayEquals(expected, input);
    }

    @Test
    @DisplayName("Boundary Case: Already sorted array")
    public void testAlreadySortedArray() {
        Integer[] input = { 1, 2, 3, 4, 5 };
        Integer[] expected = { 1, 2, 3, 4, 5 };

        MergeSort.sort(input, intComparator);
        assertArrayEquals(expected, input);
    }

    @Test
    @DisplayName("Boundary Case: Reverse sorted array")
    public void testReverseSortedArray() {
        Integer[] input = { 9, 7, 5, 3, 1 };
        Integer[] expected = { 1, 3, 5, 7, 9 };

        MergeSort.sort(input, intComparator);
        assertArrayEquals(expected, input);
    }

    @Test
    @DisplayName("Boundary Case: Array with duplicate elements")
    public void testArrayWithDuplicates() {
        Integer[] input = { 4, 1, 4, 2, 1, 3 };
        Integer[] expected = { 1, 1, 2, 3, 4, 4 };

        MergeSort.sort(input, intComparator);
        assertArrayEquals(expected, input);
    }

    @Test
    @DisplayName("Invalid Input Case: Null array throws IllegalArgumentException")
    public void testNullArrayThrows() {
        assertThrows(IllegalArgumentException.class, () -> MergeSort.sort(null, intComparator));
    }

    @Test
    @DisplayName("Invalid Input Case: Null comparator throws IllegalArgumentException")
    public void testNullComparatorThrows() {
        Integer[] input = { 3, 1, 2 };
        assertThrows(IllegalArgumentException.class, () -> MergeSort.sort(input, null));
    }
}