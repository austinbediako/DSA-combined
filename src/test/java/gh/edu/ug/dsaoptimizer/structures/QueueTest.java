package gh.edu.ug.dsaoptimizer.structures;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class QueueTest {

    @Test
    void testEnqueueDequeuePeek() {
        Queue<Integer> q = new Queue<>();
        assertTrue(q.isEmpty());
        q.enqueue(10);
        q.enqueue(20);
        assertEquals(10, q.peek());
        assertEquals(10, q.dequeue());
        assertEquals(20, q.dequeue());
        assertTrue(q.isEmpty());
    }

    @Test
    void testDequeueEmptyThrows() {
        Queue<String> q = new Queue<>();
        assertThrows(NoSuchElementException.class, q::dequeue);
    }

    @Test
    void testNullsDisallowed() {
        Queue<String> q = new Queue<>();
        assertThrows(NullPointerException.class, () -> q.enqueue(null));
    }
}
