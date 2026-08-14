package gh.edu.ug.dsaoptimizer.algorithms;

import gh.edu.ug.dsaoptimizer.model.ServiceRequest;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LinearSearchTest {

    // ---- normal cases ----

    @Test
    void findsTargetInMiddleOfArray() {
        Integer[] array = {5, 3, 8, 1, 9};
        assertEquals(2, LinearSearch.search(array, 8));
    }

    @Test
    void returnsMinusOneWhenTargetAbsent() {
        Integer[] array = {5, 3, 8, 1, 9};
        assertEquals(-1, LinearSearch.search(array, 42));
    }

    @Test
    void searchesServiceRequestsByRequestIdKey() {
        ServiceRequest[] requests = {
                request("REQ-001"),
                request("REQ-002"),
                request("REQ-003")
        };
        int index = LinearSearch.searchByKey(requests, "REQ-002", ServiceRequest::getRequestId);
        assertEquals(1, index);
    }

    // ---- boundary cases ----

    @Test
    void emptyArrayReturnsMinusOne() {
        Integer[] array = {};
        assertEquals(-1, LinearSearch.search(array, 1));
    }

    @Test
    void singleElementArrayFindsMatch() {
        Integer[] array = {7};
        assertEquals(0, LinearSearch.search(array, 7));
    }

    @Test
    void singleElementArrayMissReturnsMinusOne() {
        Integer[] array = {7};
        assertEquals(-1, LinearSearch.search(array, 99));
    }

    // ---- duplicate keys ----

    @Test
    void returnsFirstOccurrenceWhenDuplicatesPresent() {
        Integer[] array = {4, 2, 4, 4, 1};
        assertEquals(0, LinearSearch.search(array, 4));
    }

    // ---- primitive-int overload ----

    @Test
    void primitiveIntSearchFindsTarget() {
        int[] array = {10, 20, 30, 40};
        assertEquals(3, LinearSearch.search(array, 40));
    }

    @Test
    void primitiveIntSearchOnEmptyArray() {
        int[] array = {};
        assertEquals(-1, LinearSearch.search(array, 5));
    }

    // ---- invalid input ----

    @Test
    void nullArrayThrows() {
        assertThrows(IllegalArgumentException.class, () -> LinearSearch.search((Integer[]) null, 1));
    }

    @Test
    void nullKeyExtractorThrows() {
        ServiceRequest[] requests = {request("REQ-001")};
        assertThrows(IllegalArgumentException.class, () -> LinearSearch.searchByKey(requests, "REQ-001", null));
    }

    private static ServiceRequest request(String id) {
        return new ServiceRequest(
                id, 1, 2, "plumbing", ServiceRequest.Urgency.MEDIUM,
                Instant.parse("2026-08-01T07:15:00Z"), null, ServiceRequest.Status.PENDING
        );
    }
}