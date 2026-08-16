package gh.edu.ug.dsaoptimizer.structures;

import java.util.NoSuchElementException;

/**
 * Stack implemented on top of DoublyLinkedList for O(1) push/pop.
 * - Null elements disallowed.
 * - pop on empty throws NoSuchElementException.
 * - peek returns null when empty.
 */
public class Stack<T> {
    private final DoublyLinkedList<T> list = new DoublyLinkedList<>();

    public void push(T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        list.addFirst(element);
    }

    public T pop() {
        return list.removeFirst();
    }

    public T peek() {
        return list.peekFirst();
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void clear() {
        list.clear();
    }
}
