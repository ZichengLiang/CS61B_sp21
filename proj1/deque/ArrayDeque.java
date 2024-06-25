package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque <T>, Iterable<T> {
    final int INIT_SIZE = 32;
    protected Object[] items;
    protected int size;
    // example array:
    // [a, b, c, d, e, f, g]
    //  0  1  2  3  4  5  6
    // size == 7
    public ArrayDeque() {
        items = new Object[INIT_SIZE];
        size = 0; //"size" is actually used as a pointer to the deque position.
    }

    @Override
    public Iterator<T> iterator() {
        return new ADIterator();
    }

    private class ADIterator implements Iterator<T> {
        private int curr;
        public ADIterator () {
           curr = 0;
        }
        @Override
        public boolean hasNext() {
            return curr < size;
        }

        @Override
        public T next() {
            T thisObj = (T) items[curr];
            curr += 1;
            return thisObj;
        }
    }

    @Override
    public void addFirst(Object item) {
        if (size == items.length) {
            resize(size * 2);
        }

        Object[] newItems = new Object[items.length];
        System.arraycopy(items, 0, newItems, 1, size);
        items = newItems;

        items[0] = item;
        size += 1;
    }

    @Override
    public void addLast(Object item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[size] = item;
        size += 1;
    }

    private void resize(int capacity) {
        Object[] newItems = new Object[capacity];
        System.arraycopy(items, 0, newItems, 0, items.length);
        items = newItems;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.printf(items[i] + " ");
        }
        System.out.printf("\n");
    }

    @Override
    public T removeFirst() {
        if (size <= 0) {
            return null;
        }
        T removed = (T) items[0];
        System.arraycopy(items, 1, items, 0, size - 1);
        size -= 1;
        return removed;
    }

    @Override
    public T removeLast() {
        if (size <= 0) {
            return null;
        }
        T removed = (T) items[size - 1];
        size -= 1;
        return removed;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return (T) items[index];
    }

    public boolean equals(Object o) {
        if (o instanceof Deque otherDeque) {
            if (this.size() != otherDeque.size()){
                return false;
            }
            for (int i = 0; i < this.size; i++) {
                if (!this.get(i).equals(otherDeque.get(i))) {
                    return false;
                    // can I use enhanced for loop to improve this?
                    // the problem is I don't have a .contains() method.
                    // but the current implementation may be even more efficient than EFLoop
                }
            }
            return true;
        }
        return false;
    }
}