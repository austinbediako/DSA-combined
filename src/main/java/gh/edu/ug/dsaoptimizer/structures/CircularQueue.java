package gh.edu.ug.dsaoptimizer.structures;

import java.util.NoSuchElementException;

/**
 * Fixed-capacity circular queue (array-backed).
 * - Null elements disallowed.
 * - enqueue when full throws IllegalStateException.
 * - dequeue on empty throws NoSuchElementException.
 * - peek returns null when empty.
 */
public class CircularQueue<T> {
    private final Object[] data;
    private final int capacity;
    private int head = 0;
    private int tail = 0;
    private int size = 0;

    public CircularQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
        this.capacity = capacity;
        this.data = new Object[capacity];
    }

    public void enqueue(T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        if (isFull()) throw new IllegalStateException("Queue is full");
        data[tail] = element;
        tail = (tail + 1) % capacity;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");
        T val = (T) data[head];
        data[head] = null;
        head = (head + 1) % capacity;
        size--;
        return val;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        return isEmpty() ? null : (T) data[head];
    }

    public boolean isFull() {
        return size == capacity;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    public void clear() {
        while (!isEmpty()) {
            dequeue();
        }
        head = tail = 0;
    }
}
