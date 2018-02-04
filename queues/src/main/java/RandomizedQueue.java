import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int size;
    private Item[] q;

    public RandomizedQueue() {
        q = (Item[]) new Object[16];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == q.length) {
            increaseCapacity();
        }
        q[size++] = item;
    }

    private void increaseCapacity() {
        Item[] newQ = (Item[]) new Object[q.length * 2];
        System.arraycopy(q, 0, newQ, 0, q.length);
        q = newQ;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int idx = StdRandom.uniform(size);
        Item res = q[idx];
        q[idx] = q[--size];
        q[size] = null;
        if (size < q.length / 4) {
            decreaseSize();
        }
        return res;
    }

    private void decreaseSize() {
        Item[] newQ = (Item[]) new Object[q.length / 2];
        System.arraycopy(q, 0, newQ, 0, size);
        q = newQ;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return q[StdRandom.uniform(size)];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator<>(q, size);
    }

    private static class RandomizedQueueIterator<Item> implements Iterator<Item> {

        private final Item[] items;
        private int cur;

        RandomizedQueueIterator(Item[] q, int size) {
            items = (Item[]) new Object[size];
            System.arraycopy(q, 0, items, 0, size);
            StdRandom.shuffle(items);
        }

        @Override
        public boolean hasNext() {
            return cur != items.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return items[cur++];
        }
    }
}
