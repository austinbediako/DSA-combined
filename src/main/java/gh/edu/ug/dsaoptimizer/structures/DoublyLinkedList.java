package gh.edu.ug.dsaoptimizer.structures;

import java.util.NoSuchElementException;

/**
 * Doubly linked list implementation.
 * - Disallows null elements (add/set with null throws NullPointerException).
 * - Destructive operations on empty list throw NoSuchElementException.
 * - Indexed access throws IndexOutOfBoundsException for invalid indexes.
 *
 * Provides O(1) add/remove at both ends and O(min(index, size-index)) indexed access.
 */
public class DoublyLinkedList<T> {

    private static class Node<E> {
        E value;
        Node<E> prev;
        Node<E> next;

        Node(E value, Node<E> prev, Node<E> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public DoublyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addFirst(T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        Node<T> newNode = new Node<>(element, null, head);
        if (head != null) head.prev = newNode;
        head = newNode;
        if (tail == null) tail = head;
        size++;
    }

    public void addLast(T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        Node<T> newNode = new Node<>(element, tail, null);
        if (tail != null) tail.next = newNode;
        tail = newNode;
        if (head == null) head = tail;
        size++;
    }

    /**
     * Insert element at index (0..size). index == size appends at end.
     */
    public void add(int index, T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        checkIndexForAdd(index);
        if (index == 0) {
            addFirst(element);
            return;
        }
        if (index == size) {
            addLast(element);
            return;
        }
        Node<T> succ = nodeAt(index);
        Node<T> pred = succ.prev;
        Node<T> newNode = new Node<>(element, pred, succ);
        pred.next = newNode;
        succ.prev = newNode;
        size++;
    }

    public T removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("List is empty");
        T val = head.value;
        head = head.next;
        if (head != null) head.prev = null;
        else tail = null;
        size--;
        return val;
    }

    public T removeLast() {
        if (isEmpty()) throw new NoSuchElementException("List is empty");
        T val = tail.value;
        tail = tail.prev;
        if (tail != null) tail.next = null;
        else head = null;
        size--;
        return val;
    }

    public T remove(int index) {
        checkIndex(index);
        if (index == 0) return removeFirst();
        if (index == size - 1) return removeLast();
        Node<T> node = nodeAt(index);
        Node<T> pred = node.prev;
        Node<T> succ = node.next;
        pred.next = succ;
        succ.prev = pred;
        T val = node.value;
        // help GC
        node.prev = node.next = null;
        node.value = null;
        size--;
        return val;
    }

    public T get(int index) {
        checkIndex(index);
        return nodeAt(index).value;
    }

    public T set(int index, T element) {
        if (element == null) throw new NullPointerException("null elements not allowed");
        checkIndex(index);
        Node<T> node = nodeAt(index);
        T old = node.value;
        node.value = element;
        return old;
    }

    /**
     * Non-destructive peek: returns null if empty.
     */
    public T peekFirst() {
        return head == null ? null : head.value;
    }

    /**
     * Non-destructive peek: returns null if empty.
     */
    public T peekLast() {
        return tail == null ? null : tail.value;
    }

    public void clear() {
        Node<T> cur = head;
        while (cur != null) {
            Node<T> next = cur.next;
            cur.prev = cur.next = null;
            cur.value = null;
            cur = next;
        }
        head = tail = null;
        size = 0;
    }

    // helper: get node at index
    private Node<T> nodeAt(int index) {
        // choose direction based on index
        if (index < (size >> 1)) {
            Node<T> x = head;
            for (int i = 0; i < index; i++) x = x.next;
            return x;
        } else {
            Node<T> x = tail;
            for (int i = size - 1; i > index; i--) x = x.prev;
            return x;
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
}
