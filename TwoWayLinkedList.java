import java.util.*;

public class TwoWayLinkedList<E> implements MyList<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;

    // Constructor to initialize an empty TwoWayLinkedList
    public TwoWayLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        Node<E> newNode = new Node<>(e);
        if (size == 0) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.previous = tail;
            tail = newNode;
        }
        size++;
        return true;
    }

    @Override
    public void add(int index, E e) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == size) {
            add(e); // Add to the end of the list
        } else {
            Node<E> newNode = new Node<>(e);
            if (index == 0) {
                newNode.next = head;
                head.previous = newNode;
                head = newNode;
            } else {
                Node<E> current = getNode(index);
                newNode.next = current;
                newNode.previous = current.previous;
                current.previous.next = newNode;
                current.previous = newNode;
            }
            size++;
        }
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean contains(E e) {
        return indexOf(e) >= 0;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return getNode(index).element;
    }

    @Override
    public int indexOf(E e) {
        int index = 0;
        Node<E> current = head;
        while (current != null) {
            if (e.equals(current.element)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1; // Element not found
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedListIterator();
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> removedNode;
        if (size == 1) {
            removedNode = head;
            head = null;
            tail = null;
        } else if (index == 0) {
            removedNode = head;
            head = head.next;
            head.previous = null;
        } else if (index == size - 1) {
            removedNode = tail;
            tail = tail.previous;
            tail.next = null;
        } else {
            Node<E> current = getNode(index);
            removedNode = current;
            current.previous.next = current.next;
            current.next.previous = current.previous;
        }
        size--;
        return removedNode.element;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new LinkedListIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return new LinkedListIterator(index);
    }

    // Helper method to get the Node at a specific index
    private Node<E> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    // Inner class for ListIterator
    private class LinkedListIterator implements ListIterator<E> {
        private Node<E> current;
        private Node<E> lastReturned;
        private int currentIndex;

        public LinkedListIterator() {
            this.current = head;
            this.currentIndex = -1;
        }

        public LinkedListIterator(int index) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            this.current = getNode(index);
            this.currentIndex = index - 1;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = current;
            current = current.next;
            currentIndex++;
            return lastReturned.element;
        }

        @Override
        public boolean hasPrevious() {
            return currentIndex >= 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            if (current == null) {
                current = tail;
            } else {
                current = current.previous;
            }
            lastReturned = current;
            currentIndex--;
            return lastReturned.element;
        }

        @Override
        public int nextIndex() {
            return currentIndex + 1;
        }

        @Override
        public int previousIndex() {
            return currentIndex;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            TwoWayLinkedList.this.remove(currentIndex);
            currentIndex--;
            lastReturned = null;
        }

        @Override
        public void set(E e) {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            lastReturned.element = e;
        }

        @Override
        public void add(E e) {
            if (currentIndex < -1 || currentIndex >= size) {
                throw new IndexOutOfBoundsException("Index: " + currentIndex + ", Size: " + size);
            }
            if (currentIndex == -1) {
                add(e); // Add to the beginning
            } else {
                TwoWayLinkedList.this.add(currentIndex + 1, e);
            }
            currentIndex++;
            lastReturned = null;
        }
    }
}
