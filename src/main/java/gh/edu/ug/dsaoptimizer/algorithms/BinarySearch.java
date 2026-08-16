package gh.edu.ug.dsaoptimizer.algorithms;

/**
 * Binary Search — searches for a target value in a sorted array
 * using the binary search algorithm.
 *
 * <p>Preconditions:
 * <ul>
 *   <li>The input array must not be null.</li>
 *   <li>The input array must be sorted in ascending order.</li>
 * </ul>
 *
 * <p>Complexity (n = array length):
 * <ul>
 *   <li>Best case: O(1) — the target is found at the middle
 *       position on the first comparison.</li>
 *   <li>Average case: O(log n).</li>
 *   <li>Worst case: O(log n) — the search space is approximately
 *       halved after every comparison.</li>
 *   <li>Space: O(1) — the iterative implementation uses only a
 *       constant amount of extra memory.</li>
 * </ul>
 *
 * <p>Loop invariant: before every iteration, if the target exists
 * in the array, it is contained within the search range
 * {@code array[low..high]}. Each iteration either finds the target
 * or reduces this range by approximately half. When the loop ends,
 * the search range is empty, so the target does not exist in the
 * array.
 *
 * <p>The array must be sorted in ascending order for the algorithm
 * to produce a correct result.
 */
public final class BinarySearch {

    private BinarySearch() {
        // utility class — no instances
    }

    /**
     * Searches for {@code target} in a sorted array using binary search.
     *
     * @param array sorted array of integers in ascending order
     * @param target value to search for
     * @return the index of {@code target} if found; otherwise -1
     * @throws IllegalArgumentException if {@code array} is null
     */
    public static int search(int[] array, int target) {
        if (array == null) {
            throw new IllegalArgumentException("array must not be null");
        }

        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (array[mid] == target) {
                return mid;
            }

            if (array[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1;
    }
}