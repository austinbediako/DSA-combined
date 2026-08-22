package gh.edu.ug.dsaoptimizer.structures;

/**
 * Simple hash table using separate chaining with singly linked lists.
 * - Disallows null keys and null values.
 * - Resizes when load factor exceeds 0.75.
 */
public class HashTable<K, V> {
    public interface EntryConsumer<K, V> {
        void accept(K key, V value);
    }

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Node<K, V>[] buckets;
    private int size = 0;
    private int capacity;
    private boolean autoResize = true;
    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public HashTable() {
        this.capacity = DEFAULT_CAPACITY;
        this.buckets = (Node<K, V>[]) new Node[capacity];
    }

    /** Starts with a given table size, for experiments on how initial capacity affects collisions. */
    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity) {
        if (initialCapacity <= 0) throw new IllegalArgumentException("initialCapacity must be > 0");
        this.capacity = initialCapacity;
        this.buckets = (Node<K, V>[]) new Node[capacity];
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    public double loadFactor() {
        return (double) size / capacity;
    }

    /**
     * Number of entries that are NOT the first entry in their bucket
     * (i.e. entries that collided with something already there) --
     * used as the collision metric for the hash-table load-factor
     * performance experiment.
     */
    public int collisionCount() {
        int collisions = 0;
        for (int i = 0; i < capacity; i++) {
            Node<K, V> cur = buckets[i];
            if (cur == null) continue;
            cur = cur.next; // first entry in the bucket isn't a collision
            while (cur != null) {
                collisions++;
                cur = cur.next;
            }
        }
        return collisions;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public V put(K key, V value) {
        if (key == null) throw new NullPointerException("null keys not allowed");
        if (value == null) throw new NullPointerException("null values not allowed");
        int idx = indexFor(key);
        Node<K, V> cur = buckets[idx];
        while (cur != null) {
            if (cur.key.equals(key)) {
                V old = cur.value;
                cur.value = value;
                return old;
            }
            cur = cur.next;
        }
        buckets[idx] = new Node<>(key, value, buckets[idx]);
        size++;
        if (autoResize && size > capacity * LOAD_FACTOR) resize(capacity * 2);
        return null;
    }

    /**
     * Disables the automatic resize-at-0.75-load-factor behaviour, for
     * the hash-table load-factor performance experiment, which needs to
     * observe collision behaviour at load factors the table would
     * otherwise grow itself out of.
     */
    public void setAutoResize(boolean autoResize) {
        this.autoResize = autoResize;
    }

    public V get(K key) {
        if (key == null) throw new NullPointerException("null keys not allowed");
        int idx = indexFor(key);
        Node<K, V> cur = buckets[idx];
        while (cur != null) {
            if (cur.key.equals(key)) return cur.value;
            cur = cur.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public V remove(K key) {
        if (key == null) throw new NullPointerException("null keys not allowed");
        int idx = indexFor(key);
        Node<K, V> cur = buckets[idx];
        Node<K, V> prev = null;
        while (cur != null) {
            if (cur.key.equals(key)) {
                V val = cur.value;
                if (prev == null) buckets[idx] = cur.next;
                else prev.next = cur.next;
                size--;
                return val;
            }
            prev = cur;
            cur = cur.next;
        }
        return null;
    }

    public void clear() {
        @SuppressWarnings("unchecked") Node<K, V>[] nb = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        this.buckets = nb;
        this.capacity = DEFAULT_CAPACITY;
        this.size = 0;
    }

    private int indexFor(K key) {
        int h = key.hashCode();
        return (h & 0x7fffffff) % capacity;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Node<K, V>[] old = buckets;
        buckets = (Node<K, V>[]) new Node[newCapacity];
        int oldCap = capacity;
        capacity = newCapacity;
        size = 0;
        for (int i = 0; i < oldCap; i++) {
            Node<K, V> cur = old[i];
            while (cur != null) {
                put(cur.key, cur.value);
                cur = cur.next;
            }
        }
    }

    /**
     * Iterate all entries in the hash table in bucket order.
     */
    public void forEach(EntryConsumer<K, V> consumer) {
        if (consumer == null) throw new NullPointerException("consumer cannot be null");
        for (int i = 0; i < capacity; i++) {
            Node<K, V> cur = buckets[i];
            while (cur != null) {
                consumer.accept(cur.key, cur.value);
                cur = cur.next;
            }
        }
    }
}
