package gh.edu.ug.dsaoptimizer.structures;

/**
 * A simple resizable array implementation.
 * - Disallows null elements (add/set with null throws NullPointerException).
 * - Destructive operations on invalid indexes throw IndexOutOfBoundsException.
 *
 * Amortized O(1) append, O(n) insert/remove at arbitrary index.
 */
public class DynamicArray<T> {
    private static final int DEFAULT_CAPACITY = 8;
    private Object[] data;
    private int size;

    public DynamicArray() {
        this.data = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("initialCapacity must be > 0");
        }
        this.data = new Object[initialCapacity];
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return data.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        ensureCapacity(size + 1);
        data[size++] = element;
    }

    public void add(int index, T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        checkIndexForAdd(index);
        ensureCapacity(size + 1);
        // shift right
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = element;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) data[index];
    }

    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        checkIndex(index);
        T old = (T) data[index];
        data[index] = element;
        return old;
    }

    @SuppressWarnings("unchecked")
    public T remove(int index) {
        checkIndex(index);
        T removed = (T) data[index];
        // shift left
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null;
        size--;
        // optional shrink: halve capacity when usage < 1/4
        if (data.length > DEFAULT_CAPACITY && size <= data.length / 4) {
            resize(data.length / 2);
        }
        return removed;
    }

    public void clear() {
        for (int i = 0; i < size; i++) data[i] = null;
        size = 0;
        if (data.length > DEFAULT_CAPACITY) {
            data = new Object[DEFAULT_CAPACITY];
        }
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > data.length) {
            int newCap = data.length * 2;
            if (newCap < minCapacity) newCap = minCapacity;
            resize(newCap);
        }
    }

    private void resize(int newCapacity) {
        Object[] newData = new Object[newCapacity];
        for (int i = 0; i < size; i++) newData[i] = data[i];
        data = newData;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
}
