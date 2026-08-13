package gh.edu.ug.dsaoptimizer.structures;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class PriorityQueueHeapTest {

    @Test
    void testOfferPeekPollNaturalOrder() {
        PriorityQueueHeap<Integer> pq = new PriorityQueueHeap<>();
        pq.offer(5);
        pq.offer(1);
        pq.offer(3);
        assertEquals(1, pq.peek());
        assertEquals(1, pq.poll());
        assertEquals(3, pq.poll());
        assertEquals(5, pq.poll());
        assertTrue(pq.isEmpty());
    }

    @Test
    void testOfferWithComparator() {
        PriorityQueueHeap<String> pq = new PriorityQueueHeap<>(Comparator.comparingInt(String::length));
        pq.offer("aaa");
        pq.offer("b");
        pq.offer("cc");
        assertEquals("b", pq.poll());
        assertEquals("cc", pq.poll());
        assertEquals("aaa", pq.poll());
    }

    @Test
    void testNullsDisallowedAndEmptyPollThrows() {
        PriorityQueueHeap<Integer> pq = new PriorityQueueHeap<>();
        assertThrows(NullPointerException.class, () -> pq.offer(null));
        assertThrows(NoSuchElementException.class, pq::poll);
    }

    @Test
    void testResize() {
        PriorityQueueHeap<Integer> pq = new PriorityQueueHeap<>(null, 2);
        for (int i = 10; i >= 1; i--) pq.offer(i);
        for (int i = 1; i <= 10; i++) assertEquals(i, pq.poll());
    }
}
