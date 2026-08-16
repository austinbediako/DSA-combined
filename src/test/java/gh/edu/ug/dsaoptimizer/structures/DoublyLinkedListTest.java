package gh.edu.ug.dsaoptimizer.structures;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class DoublyLinkedListTest {

    @Test
    void testAddFirstAndLastAndPeek() {
        DoublyLinkedList<Integer> list = new DoublyLinkedList<>();
        assertNull(list.peekFirst());
        assertNull(list.peekLast());

        list.addFirst(2); // 2
        list.addFirst(1); // 1,2
        list.addLast(3);  // 1,2,3

        assertEquals(3, list.size());
        assertEquals(1, list.peekFirst());
        assertEquals(3, list.peekLast());
    }

    @Test
    void testAddAtIndexAndGetSet() {
        DoublyLinkedList<String> list = new DoublyLinkedList<>();
        list.addLast("a");
        list.addLast("c");
        list.add(1, "b"); // a,b,c
        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));

        assertEquals("b", list.set(1, "B"));
        assertEquals("B", list.get(1));
    }

    @Test
    void testRemoveFirstLastAndRemoveIndex() {
        DoublyLinkedList<Integer> list = new DoublyLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(1, list.removeFirst());
        assertEquals(2, list.size());
        assertEquals(3, list.removeLast());
        assertEquals(1, list.size());
        list.addLast(4); // list: 2,4
        assertEquals(4, list.remove(1));
        assertEquals(1, list.size());
        assertEquals(2, list.remove(0));
        assertTrue(list.isEmpty());
    }

    @Test
    void testBoundsAndExceptions() {
        DoublyLinkedList<Integer> list = new DoublyLinkedList<>();
        assertThrows(NoSuchElementException.class, list::removeFirst);
        assertThrows(NoSuchElementException.class, list::removeLast);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 5));
        list.addLast(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(2));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(2));
    }

    @Test
    void testNullsDisallowed() {
        DoublyLinkedList<String> list = new DoublyLinkedList<>();
        assertThrows(NullPointerException.class, () -> list.addFirst(null));
        assertThrows(NullPointerException.class, () -> list.addLast(null));
        list.addLast("ok");
        assertThrows(NullPointerException.class, () -> list.set(0, null));
    }

    @Test
    void testClearAndSingleElementTransitions() {
        DoublyLinkedList<Integer> list = new DoublyLinkedList<>();
        list.addLast(10);
        assertEquals(10, list.removeFirst());
        assertTrue(list.isEmpty());

        list.addFirst(20);
        assertEquals(20, list.removeLast());
        assertTrue(list.isEmpty());

        list.addLast(1);
        list.addLast(2);
        list.clear();
        assertTrue(list.isEmpty());
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    }

    @Test
    void testToArrayReturnsElementsHeadToTail() {
        DoublyLinkedList<String> list = new DoublyLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        assertArrayEquals(new String[]{"A", "B", "C"}, list.toArray());
    }

    @Test
    void testToArrayOnEmptyListReturnsEmptyArray() {
        DoublyLinkedList<String> list = new DoublyLinkedList<>();
        assertEquals(0, list.toArray().length);
    }
}
