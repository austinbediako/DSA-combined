package gh.edu.ug.dsaoptimizer.algorithms;

import java.util.Comparator;

/**
 * Selection sort — in-place, ascending order under the supplied
 * {@link Comparator}.
 *
 * <p>Precondition: none on ordering of the input (works on any array);
 * only requires a valid total-order comparator for the element type.
 *
 * <p>Complexity (n = array length):
 * <ul>
 *   <li>Best case: O(n^2) comparisons — selection sort scans the
 *       remaining unsorted region on every pass regardless of input
 *       order, so best/average/worst are all O(n^2) in comparisons.</li>
 *   <li>Swaps: O(n) — at most one swap per pass (its main advantage
 *       over e.g. bubble sort when writes are expensive).</li>
 *   <li>Space: O(1) — in-place, no auxiliary array.</li>
 *   <li>Stability: NOT stable in this implementation (a single swap
 *       can move an equal-keyed element past another equal-keyed one).</li>
 * </ul>
 *
 * <p>Loop invariant (for the proof sketch, brief §10): after the
 * iteration that fixes index {@code i}, {@code array[0..i]} is sorted
 * and every element in {@code array[0..i]} is less than or equal to
 * every element in {@code array[i+1..n-1]}. Combined with the base
 * case (i = -1, an empty prefix is trivially sorted), induction over
 * i gives full-array correctness when i reaches n-1.
 *
 * No dependency on structures/persistence/ui, per docs/ARCHITECTURE.md.
 */
public final class SelectionSort {

    private SelectionSort() {
        // utility class — no instances
    }

    /** Sorts {@code array} in place, ascending, using {@code comparator}. */
    public static <T> void sort(T[] array, Comparator<T> comparator) {
        if (array == null) {
            throw new IllegalArgumentException("array must not be null");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("comparator must not be null");
        }
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (comparator.compare(array[j], array[minIndex]) < 0) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                T tmp = array[i];
                array[i] = array[minIndex];
                array[minIndex] = tmp;
            }
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
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                int tmp = array[i];
                array[i] = array[minIndex];
                array[minIndex] = tmp;
            }
        }
    }
}