package gh.edu.ug.dsaoptimizer.algorithms;

import java.util.Comparator;

/**
 * Insertion sort -- in-place, ascending order under the supplied
 * {@link Comparator}.
 *
 * <p>Complexity (n = array length):
 * <ul>
 *   <li>Best case: O(n) -- input already sorted, inner loop exits
 *       immediately on every pass.</li>
 *   <li>Average/worst case: O(n^2) -- reverse-sorted input maximises
 *       shifting.</li>
 *   <li>Space: O(1) -- in-place, no auxiliary array.</li>
 *   <li>Stability: stable -- equal-keyed elements are only shifted
 *       past, never swapped past each other (the shift loop condition
 *       uses strict {@code < 0}, so it stops at the first equal
 *       element it encounters).</li>
 * </ul>
 *
 * <p>Loop invariant (proof sketch, brief section 10): after the
 * iteration that inserts element at original index {@code i},
 * {@code array[0..i]} is sorted. Combined with the base case
 * (a single-element prefix {@code array[0..0]} is trivially sorted),
 * induction over i gives full-array correctness when i reaches n-1.
 */
public final class InsertionSort {

    private InsertionSort() {
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
        for (int i = 1; i < array.length; i++) {
            T key = array[i];
            int j = i - 1;
            while (j >= 0 && comparator.compare(array[j], key) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
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
        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }
}
