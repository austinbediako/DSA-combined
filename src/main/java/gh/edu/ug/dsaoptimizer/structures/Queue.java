package gh.edu.ug.dsaoptimizer.structures;

import java.util.NoSuchElementException;

/**
 * Simple FIFO queue using DoublyLinkedList.
 * - Null elements disallowed.
 * - dequeue on empty throws NoSuchElementException.
 * - peek returns null when empty.
 */
public class Queue<T> {
    private final DoublyLinkedList<T> list = new DoublyLinkedList<>();

    public void enqueue(T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        list.addLast(element);
    }

    public T dequeue() {
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
