package gh.edu.ug.dsaoptimizer.algorithms;

import gh.edu.ug.dsaoptimizer.model.ServiceRequest;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SelectionSortTest {

    // ---- normal cases ----

    @Test
    void sortsUnorderedArrayAscending() {
        Integer[] array = {5, 3, 8, 1, 9, 2};
        SelectionSort.sort(array, Comparator.naturalOrder());
        assertArrayEquals(new Integer[]{1, 2, 3, 5, 8, 9}, array);
    }

    @Test
    void sortsAlreadySortedArray() {
        Integer[] array = {1, 2, 3, 4};
        SelectionSort.sort(array, Comparator.naturalOrder());
        assertArrayEquals(new Integer[]{1, 2, 3, 4}, array);
    }

    @Test
    void sortsReverseSortedArray() {
        Integer[] array = {9, 7, 5, 3, 1};
        SelectionSort.sort(array, Comparator.naturalOrder());
        assertArrayEquals(new Integer[]{1, 3, 5, 7, 9}, array);
    }

    @Test
    void sortsServiceRequestsByUrgencyDescending() {
        ServiceRequest low = request("REQ-001", ServiceRequest.Urgency.LOW, "2026-08-02T00:00:00Z");
        ServiceRequest critical = request("REQ-002", ServiceRequest.Urgency.CRITICAL, "2026-08-01T00:00:00Z");
        ServiceRequest high = request("REQ-003", ServiceRequest.Urgency.HIGH, "2026-08-01T12:00:00Z");
        ServiceRequest[] requests = {low, critical, high};

        Comparator<ServiceRequest> byUrgencyDesc =
                Comparator.comparing(ServiceRequest::getUrgency).reversed();
        SelectionSort.sort(requests, byUrgencyDesc);

        assertArrayEquals(new ServiceRequest[]{critical, high, low}, requests);
    }

    @Test
    void sortsServiceRequestsByUrgencyThenDeadlineOnTie() {
        // Two requests share the same urgency (HIGH) — this is the case
        // that actually exercises the deadline tiebreak.
        ServiceRequest highLaterDeadline = request("REQ-001", ServiceRequest.Urgency.HIGH, "2026-08-02T00:00:00Z");
        ServiceRequest highEarlierDeadline = request("REQ-002", ServiceRequest.Urgency.HIGH, "2026-08-01T00:00:00Z");
        ServiceRequest critical = request("REQ-003", ServiceRequest.Urgency.CRITICAL, "2026-08-03T00:00:00Z");
        ServiceRequest[] requests = {highLaterDeadline, critical, highEarlierDeadline};

        Comparator<ServiceRequest> byUrgencyThenDeadline =
                Comparator.comparing(ServiceRequest::getUrgency).reversed()
                        .thenComparing(ServiceRequest::getDeadline);
        SelectionSort.sort(requests, byUrgencyThenDeadline);

        // CRITICAL first; then the two HIGH requests ordered by earlier deadline first.
        assertArrayEquals(new ServiceRequest[]{critical, highEarlierDeadline, highLaterDeadline}, requests);
    }

    // ---- boundary cases ----

    @Test
    void sortsEmptyArrayWithoutError() {
        Integer[] array = {};
        SelectionSort.sort(array, Comparator.naturalOrder());
        assertArrayEquals(new Integer[]{}, array);
    }

    @Test
    void sortsSingleElementArray() {
        Integer[] array = {42};
        SelectionSort.sort(array, Comparator.naturalOrder());
        assertArrayEquals(new Integer[]{42}, array);
    }

    // ---- duplicate keys ----

    @Test
    void sortsArrayWithDuplicateValues() {
        Integer[] array = {4, 2, 4, 1, 2};
        SelectionSort.sort(array, Comparator.naturalOrder());
        assertArrayEquals(new Integer[]{1, 2, 2, 4, 4}, array);
    }

    // ---- primitive-int overload ----

    @Test
    void primitiveIntSortAscending() {
        int[] array = {5, 3, 8, 1, 9, 2};
        SelectionSort.sort(array);
        assertArrayEquals(new int[]{1, 2, 3, 5, 8, 9}, array);
    }

    @Test
    void primitiveIntSortOnEmptyArray() {
        int[] array = {};
        SelectionSort.sort(array);
        assertArrayEquals(new int[]{}, array);
    }

    // ---- invalid input ----

    @Test
    void nullArrayThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> SelectionSort.sort((Integer[]) null, Comparator.naturalOrder()));
    }

    @Test
    void nullComparatorThrows() {
        Integer[] array = {3, 1, 2};
        assertThrows(IllegalArgumentException.class, () -> SelectionSort.sort(array, null));
    }

    private static ServiceRequest request(String id, ServiceRequest.Urgency urgency, String deadlineIso) {
        return new ServiceRequest(
                id, 1, 2, "plumbing", urgency,
                Instant.parse("2026-08-01T07:15:00Z"), Instant.parse(deadlineIso),
                ServiceRequest.Status.PENDING
        );
    }
}
