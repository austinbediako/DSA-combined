package gh.edu.ug.dsaoptimizer.structures;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class StackTest {

    @Test
    void testPushPopPeek() {
        Stack<Integer> s = new Stack<>();
        assertTrue(s.isEmpty());
        s.push(1);
        s.push(2);
        assertEquals(2, s.size());
        assertEquals(2, s.peek());
        assertEquals(2, s.pop());
        assertEquals(1, s.pop());
        assertTrue(s.isEmpty());
    }

    @Test
    void testPopEmptyThrows() {
        Stack<String> s = new Stack<>();
        assertThrows(NoSuchElementException.class, s::pop);
    }

    @Test
    void testNullsDisallowed() {
        Stack<String> s = new Stack<>();
        assertThrows(NullPointerException.class, () -> s.push(null));
    }
}
