package gh.edu.ug.dsaoptimizer.structures;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class DequeTest {

    @Test
    void testAddRemoveBothEnds() {
        Deque<Integer> d = new Deque<>();
        d.addFirst(2); // 2
        d.addFirst(1); // 1,2
        d.addLast(3);  // 1,2,3
        assertEquals(1, d.removeFirst());
        assertEquals(3, d.removeLast());
        assertEquals(2, d.removeFirst());
        assertTrue(d.isEmpty());
    }

    @Test
    void testPeekAndExceptions() {
        Deque<String> d = new Deque<>();
        assertNull(d.peekFirst());
        assertNull(d.peekLast());
        assertThrows(NoSuchElementException.class, d::removeFirst);
        assertThrows(NoSuchElementException.class, d::removeLast);
    }

    @Test
    void testNullsDisallowed() {
        Deque<String> d = new Deque<>();
        assertThrows(NullPointerException.class, () -> d.addFirst(null));
        assertThrows(NullPointerException.class, () -> d.addLast(null));
    }
}
