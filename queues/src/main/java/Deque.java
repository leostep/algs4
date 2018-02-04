import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private int size;
    private Node<Item> first;
    private Node<Item> last;

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        checkArg(item);
        if (isEmpty()) {
            addToEmpty(item);
        } else {
            Node<Item> newNode = new Node<>(item, null, first);
            first.prev = newNode;
            first = newNode;
            size++;
        }
    }

    public void addLast(Item item) {
        checkArg(item);
        if (isEmpty()) {
            addToEmpty(item);
        } else {
            Node<Item> newNode = new Node<>(item, last, null);
            last.next = newNode;
            last = newNode;
            size++;
        }
    }

    private void checkArg(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
    }

    private void addToEmpty(Item item) {
        first = new Node<>(item);
        last = first;
        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (size == 1) {
            return removeSingleElem();
        }
        Item res = first.item;
        first = first.next;
        if (first != null) {
            first.prev = null;
        }
        size--;
        return res;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (size == 1) {
            return removeSingleElem();
        }
        Item res = last.item;
        last = last.prev;
        if (last != null) {
            last.next = null;
        }
        size--;
        return res;
    }

    private Item removeSingleElem() {
        Item res = first.item;
        first = null;
        last = null;
        size--;
        return res;
    }

    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Node<Item> current = first;

            public boolean hasNext() {
                return current != null;
            }

            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Item res = current.item;
                current = current.next;
                return res;
            }
        };
    }

    private static class Node<Item> {
        Item item;
        Node<Item> prev = null;
        Node<Item> next = null;

        Node(Item item) {
            this.item = item;
        }

        Node(Item item, Node<Item> prev, Node<Item> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Item i : this) {
            sb.append(i).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }
}
