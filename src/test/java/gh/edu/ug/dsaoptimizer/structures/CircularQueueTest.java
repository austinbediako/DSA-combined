package gh.edu.ug.dsaoptimizer.structures;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CircularQueueTest {

    @Test
    void testEnqueueDequeueWrap() {
        CircularQueue<Integer> q = new CircularQueue<>(3);
        assertTrue(q.isEmpty());
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        assertTrue(q.isFull());
        assertEquals(1, q.dequeue());
        assertFalse(q.isFull());
        q.enqueue(4); // wrap-around into freed slot
        assertEquals(2, q.dequeue());
        assertEquals(3, q.dequeue());
        assertEquals(4, q.dequeue());
        assertTrue(q.isEmpty());
    }

    @Test
    void testFullThrows() {
        CircularQueue<Integer> q = new CircularQueue<>(2);
        q.enqueue(1);
        q.enqueue(2);
        assertThrows(IllegalStateException.class, () -> q.enqueue(3));
    }

    @Test
    void testDequeueEmptyThrows() {
        CircularQueue<String> q = new CircularQueue<>(2);
        assertThrows(NoSuchElementException.class, q::dequeue);
    }

    @Test
    void testNullsDisallowed() {
        CircularQueue<String> q = new CircularQueue<>(2);
        assertThrows(NullPointerException.class, () -> q.enqueue(null));
    }
}
