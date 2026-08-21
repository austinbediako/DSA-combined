package gh.edu.ug.dsaoptimizer.algorithms;

import java.util.Comparator;

/**
 * Quicksort -- in-place (aside from the recursion stack), ascending
 * order under the supplied {@link Comparator}. Uses Lomuto partitioning
 * with the last element as pivot.
 *
 * <p>Complexity (n = array length):
 * <ul>
 *   <li>Best/average case: O(n log n) -- pivot roughly halves the
 *       remaining range each time.</li>
 *   <li>Worst case: O(n^2) -- already-sorted (or reverse-sorted) input
 *       with last-element pivoting degenerates to one-sided partitions
 *       every time; see {@code docs/PERFORMANCE_EXPERIMENTS.md} for
 *       where this shows up empirically.</li>
 *   <li>Space: O(log n) average recursion depth, O(n) worst case.</li>
 *   <li>Stability: NOT stable -- partitioning can swap an element past
 *       another equal-keyed element.</li>
 * </ul>
 *
 * <p>Recurrence (brief section 7): average case
 * {@code T(n) = T(k) + T(n-k-1) + O(n)} where k is the partition split
 * point; when the pivot is consistently near the median, k ~ n/2,
 * giving the same O(n log n) shape as merge sort's
 * {@code T(n) = 2T(n/2) + O(n)}. Worst case
 * {@code T(n) = T(n-1) + O(n)} resolves to O(n^2), unlike merge sort
 * which has no data-dependent worst case.
 */
public final class QuickSort {

    private QuickSort() {
        // utility class -- no instances
    }

    /** Sorts {@code array} in place, ascending, using {@code comparator}. */
    public static <T> void sort(T[] array, Comparator<T> comparator) {
        if (array == null) {
            throw new IllegalArgumentException("array must not be null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("comparator must not be null");
        }
        if (array.length > 1) {
            quickSort(array, 0, array.length - 1, comparator);
        }
    }

    private static <T> void quickSort(T[] array, int low, int high, Comparator<T> comparator) {
        if (low < high) {
            int pivotIndex = partition(array, low, high, comparator);
            quickSort(array, low, pivotIndex - 1, comparator);
            quickSort(array, pivotIndex + 1, high, comparator);
        }
    }

    private static <T> int partition(T[] array, int low, int high, Comparator<T> comparator) {
        T pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (comparator.compare(array[j], pivot) <= 0) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, high);
        return i + 1;
    }

    private static <T> void swap(T[] array, int i, int j) {
        T tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    /**
     * Primitive-int overload, handy for the sorting-comparison experiment
     * in docs/PERFORMANCE_EXPERIMENTS.md and for trace tables, without
     * boxing overhead skewing timing results.
     */
    public static void sort(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("array must not be null");
        }
        if (array.length > 1) {
            quickSort(array, 0, array.length - 1);
        }
    }

    private static void quickSort(int[] array, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(array, low, high);
            quickSort(array, low, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, high);
        }
    }

    private static int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                int tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
            }
        }
        int tmp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = tmp;
        return i + 1;
    }
}
