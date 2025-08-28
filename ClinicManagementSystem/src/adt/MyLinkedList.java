package adt;

public class MyLinkedList<T> implements ListInterface<T> {
    
    private Node<T> firstNode;
    private int size;

    public MyLinkedList() {
        firstNode = null;
        size = 0;
    }

    // Add new entry at the end
    @Override
    public void add(T newEntry) {
        Node<T> newNode = new Node<>(newEntry);
        if (isEmpty()) {
            firstNode = newNode;
        } else {
            Node<T> current = firstNode;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    // Add at specific position
    @Override
    public boolean add(int newPosition, T newEntry) {
        if (newPosition >= 1 && newPosition <= size + 1) {
            Node<T> newNode = new Node<>(newEntry);
            if (newPosition == 1) {
                newNode.next = firstNode;
                firstNode = newNode;
            } else {
                Node<T> nodeBefore = getNodeAt(newPosition - 1);
                newNode.next = nodeBefore.next;
                nodeBefore.next = newNode;
            }
            size++;
            return true;
        }
        return false;
    }

    @Override
    public T remove(int givenPosition) {
        T result = null;

        if (givenPosition >= 1 && givenPosition <= size) {
            if (givenPosition == 1) {
                result = firstNode.data;
                firstNode = firstNode.next;
            } else {
                Node<T> nodeBefore = getNodeAt(givenPosition - 1);
                Node<T> nodeToRemove = nodeBefore.next;
                result = nodeToRemove.data;
                nodeBefore.next = nodeToRemove.next;
            }
            size--;
        }

        return result;
    }

    @Override
    public void clear() {
        firstNode = null;
        size = 0;
    }

    @Override
    public boolean replace(int givenPosition, T newEntry) {
        boolean success = false;

        if (givenPosition >= 1 && givenPosition <= size) {
            Node<T> desiredNode = getNodeAt(givenPosition);
            desiredNode.data = newEntry;
            success = true;
        }

        return success;
    }

    @Override
    public T getEntry(int givenPosition) {
        if (givenPosition >= 1 && givenPosition <= size) {
            return getNodeAt(givenPosition).data;
        }
        return null;
    }

    @Override
    public boolean contains(T anEntry) {
        Node<T> current = firstNode;
        while (current != null) {
            if (current.data.equals(anEntry)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public int getLength() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Helper method to get node at a specific position
    private Node<T> getNodeAt(int position) {
        Node<T> current = firstNode;
        for (int i = 1; i < position; i++) {
            current = current.next;
        }
        return current;
    }

    // Inner Node class
    private static class Node<T> {
        private T data;
        private Node<T> next;

        private Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
}
