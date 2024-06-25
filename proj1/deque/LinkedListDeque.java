package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private final Node sentinel;
    private int size = 0;
    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.setPrev(sentinel);
        sentinel.setNext(sentinel);
    }

    @Override
    public Iterator<T> iterator() {
        return new LLDIterator();
    }

    private class LLDIterator implements Iterator<T> {
        private Node curr;
        LLDIterator() {
            curr = sentinel.next;
        }
        @Override
        public boolean hasNext() {
            return curr.getNext() != sentinel;
        }

        @Override
        public T next() {
            T thisOne = curr.getValue();
            curr = curr.getNext();
            return thisOne;
        }
    }

    private class Node {
        T value;
        Node prev, next;

        Node(T value, Node prev, Node next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
        public void setValue(T value) {
            this.value = value;
        }
        public void setPrev(Node prev) {
            this.prev = prev;
        }
        public void setNext(Node next) {
            this.next = next;
        }
        public T getValue() {
            return this.value;
        }
        public Node getPrev() {
            return this.prev;
        }
        public Node getNext() {
            return this.next;
        }
    }


    @Override
    public void addFirst(T item) {
        Node first = new Node(item, sentinel, sentinel.next);
        sentinel.next.setPrev(first);
        sentinel.setNext(first);
        size += 1;
    }

    @Override
    public void addLast(T item) {
        Node last = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.setNext(last);
        sentinel.setPrev(last);
        size += 1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void printDeque() {
        Node curr = sentinel.next;
        while (curr.getValue() != null) {
            System.out.print(curr.getValue() + " ");
            curr = curr.getNext();
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        Node removed = sentinel.next;

        sentinel.setNext(removed.next);
        removed.next.setPrev(sentinel);

        size -= 1;

        return removed.getValue();
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }

        Node removed = sentinel.prev;

        sentinel.setPrev(removed.prev);
        removed.prev.setNext(sentinel);

        size -= 1;

        return removed.getValue();
    }

    @Override
    public T get(int index) {
        if (size <= index) {
            return null;
        }

        int i = 0;
        Node curr = sentinel.next;
        while (i < index) {
            curr = curr.getNext();
            i += 1;
        }
        return curr.getValue();
    }

    public T getRecursive(int index) {
        return getRecursiveHelper(sentinel.getNext(), index);
    }

    private T getRecursiveHelper(Node next, int index) {
        if (index == 0) {
            return next.getValue();
        }
        return getRecursiveHelper(next.getNext(), index - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Deque otherDeque) {
            if (this.size != otherDeque.size()) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (!this.get(i).equals(otherDeque.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
