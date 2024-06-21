package deque;

public class ArrayDeque <T> implements Deque <T> {
    final int INIT_SIZE = 32;
    private Object[] items;
    private int size;
    // example array:
    // [a, b, c, d, e, f, g]
    //  0  1  2  3  4  5  6
    // size == 7
    public ArrayDeque() {
        items = new Object[INIT_SIZE];
        size = 0; //"size" is actually used as a pointer to the deque position.
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

    @Override
    public boolean equals(Object o) {
        return false;
    }

}
