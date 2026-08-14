package gh.edu.ug.dsaoptimizer.algorithms;

import java.util.function.Function;

/**
 * Linear (sequential) search.
 *
 * <p>Precondition: none — unlike binary search, the input array does
 * NOT need to be sorted. This is linear search's main advantage over
 * binary search and should be called out explicitly when the two are
 * compared in the report (brief §7: "binary search precondition
 * stated and tested").
 *
 * <p>Complexity (n = array length):
 * <ul>
 *   <li>Best case: O(1) — target is at index 0.</li>
 *   <li>Average case: O(n).</li>
 *   <li>Worst case: O(n) — target is at the last index, or absent.</li>
 *   <li>Space: O(1) — no extra structures, in-place scan.</li>
 * </ul>
 *
 * <p>Loop invariant (for the proof sketch, brief §10): at the start of
 * each iteration {@code i}, none of {@code array[0..i-1]} equals the
 * target. The loop terminates either when a match is found (the
 * invariant plus the match gives correctness) or when {@code i == n},
 * in which case the invariant states no element matches, so returning
 * "not found" is correct.
 *
 * No dependency on structures/persistence/ui, per docs/ARCHITECTURE.md.
 */
public final class LinearSearch {

    private LinearSearch() {
        // utility class — no instances
    }

    /**
     * Searches {@code array} for the first element equal to {@code target}
     * according to {@code equals}.
     *
     * @return the index of the first match, or -1 if not found.
     */
    public static <T> int search(T[] array, T target) {
        if (array == null) {
            throw new IllegalArgumentException("array must not be null");
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null ? target == null : array[i].equals(target)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Searches {@code array} for the first element whose extracted key
     * equals {@code key}. Useful when you want to search by a field
     * (e.g. requestId) rather than full object equality — e.g.
     * {@code searchByKey(requests, "REQ-042", ServiceRequest::getRequestId)}.
     *
     * @return the index of the first match, or -1 if not found.
     */
    public static <T, K> int searchByKey(T[] array, K key, Function<T, K> keyExtractor) {
        if (array == null) {
            throw new IllegalArgumentException("array must not be null");
        }
        if (keyExtractor == null) {
            throw new IllegalArgumentException("keyExtractor must not be null");
        }
        for (int i = 0; i < array.length; i++) {
            K extracted = keyExtractor.apply(array[i]);
            if (extracted == null ? key == null : extracted.equals(key)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Primitive-int overload, handy for the search-comparison experiment
     * in docs/PERFORMANCE_EXPERIMENTS.md and for trace tables, without
     * boxing overhead skewing timing results.
     *
     * @return the index of the first match, or -1 if not found.
     */
    public static int search(int[] array, int target) {
        if (array == null) {
            throw new IllegalArgumentException("array must not be null");
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
}