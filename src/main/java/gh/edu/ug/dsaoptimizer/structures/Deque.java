package gh.edu.ug.dsaoptimizer.structures;

/**
 * Deque implemented as a thin wrapper around DoublyLinkedList.
 * - Null elements disallowed.
 * - removeFirst/removeLast throw NoSuchElementException when empty (delegated).
 * - peekFirst/peekLast return null when empty.
 */
public class Deque<T> {
    private final DoublyLinkedList<T> list = new DoublyLinkedList<>();

    public void addFirst(T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        list.addFirst(element);
    }

    public void addLast(T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        list.addLast(element);
    }

    public T removeFirst() {
        return list.removeFirst();
    }

    public T removeLast() {
        return list.removeLast();
    }

    public T peekFirst() {
        return list.peekFirst();
    }

    public T peekLast() {
        return list.peekLast();
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
