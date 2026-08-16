package gh.edu.ug.dsaoptimizer.structures;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * Min-heap priority queue with optional Comparator. If no comparator is provided,
 * elements must implement Comparable at runtime.
 */
public class PriorityQueueHeap<T> {
    private Object[] heap;
    private int size = 0;
    private Comparator<? super T> comparator;
    private static final int DEFAULT_CAPACITY = 16;

    public PriorityQueueHeap() {
        this(null, DEFAULT_CAPACITY);
    }

    public PriorityQueueHeap(Comparator<? super T> comparator) {
        this(comparator, DEFAULT_CAPACITY);
    }

    public PriorityQueueHeap(Comparator<? super T> comparator, int initialCapacity) {
        if (initialCapacity <= 0) throw new IllegalArgumentException("initialCapacity must be > 0");
        this.comparator = comparator;
        this.heap = new Object[initialCapacity];
    }

    public boolean offer(T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        ensureCapacity(size + 1);
        heap[size] = element;
        siftUp(size++);
        return true;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        return size == 0 ? null : (T) heap[0];
    }

    @SuppressWarnings("unchecked")
    public T poll() {
        if (size == 0) throw new NoSuchElementException("PriorityQueue is empty");
        T root = (T) heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (size > 0) siftDown(0);
        return root;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private int compare(T a, T b) {
        if (comparator != null) return comparator.compare(a, b);
        return ((Comparable<? super T>) a).compareTo(b);
    }

    private void siftUp(int idx) {
        int i = idx;
        @SuppressWarnings("unchecked") T value = (T) heap[i];
        while (i > 0) {
            int parent = (i - 1) >>> 1;
            @SuppressWarnings("unchecked") T p = (T) heap[parent];
            if (compare(value, p) >= 0) break;
            heap[i] = p;
            i = parent;
        }
        heap[i] = value;
    }

    private void siftDown(int idx) {
        int i = idx;
        @SuppressWarnings("unchecked") T value = (T) heap[i];
        int half = size >>> 1; // nodes with children
        while (i < half) {
            int left = (i << 1) + 1;
            int right = left + 1;
            int smallest = left;
            @SuppressWarnings("unchecked") T leftVal = (T) heap[left];
            if (right < size) {
                @SuppressWarnings("unchecked") T rightVal = (T) heap[right];
                if (compare(rightVal, leftVal) < 0) smallest = right;
            }
            @SuppressWarnings("unchecked") T smallestVal = (T) heap[smallest];
            if (compare(smallestVal, value) >= 0) break;
            heap[i] = smallestVal;
            i = smallest;
        }
        heap[i] = value;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > heap.length) {
            int newCap = heap.length * 2;
            if (newCap < minCapacity) newCap = minCapacity;
            Object[] newHeap = new Object[newCap];
            for (int i = 0; i < size; i++) newHeap[i] = heap[i];
            heap = newHeap;
        }
    }
}
