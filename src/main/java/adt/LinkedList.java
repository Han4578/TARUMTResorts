/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adt;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 *
 * @author louos
 * @param <T>
 */
public class LinkedList<T> implements ListInterface<T>, Serializable {
    private Node firstNode;
    private int numberOfEntries;

    // constructor
    private class Node implements Serializable {
        private T data;
        private Node next;

        private Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public LinkedList() {
        clear();
    }

    @Override
    public void add(T item) {
        Node newNode = new Node(item);
        if (isEmpty()) {
            firstNode = newNode;
        } else {
            Node currentNode = firstNode;
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
        numberOfEntries++;
    }

    @Override
    public void add(T item, int index) {
        if (index >= 0 && index <= numberOfEntries) {
            Node newNode = new Node(item);
            if (index == 0) {
                newNode.next = firstNode;
                firstNode = newNode;
            } else {
                Node nodeBefore = firstNode;
                for (int i = 0; i < index - 1; i++) {
                    nodeBefore = nodeBefore.next;
                }
                newNode.next = nodeBefore.next;
                nodeBefore.next = newNode;
            }
            numberOfEntries++;
        } else {
            throw new IndexOutOfBoundsException("Illegal index given to add operation.");
        }
    }

    @Override
    public T remove(int index) {
        T result = null;
        if (index >= 0 && index < numberOfEntries) {
            if (index == 0) {
                result = firstNode.data;
                firstNode = firstNode.next;
            } else {
                Node nodeBefore = firstNode;
                for (int i = 0; i < index - 1; i++) {
                    nodeBefore = nodeBefore.next;
                }
                result = nodeBefore.next.data;
                nodeBefore.next = nodeBefore.next.next;
            }
            numberOfEntries--;
        } else {
            throw new IndexOutOfBoundsException("Illegal index given to remove operation.");
        }
        return result;
    }

    @Override
    public T get(int index) {
        if (index >= 0 && index < numberOfEntries) {
            Node currentNode = firstNode;
            for (int i = 0; i < index; i++) {
                currentNode = currentNode.next;
            }
            return currentNode.data;
        }
        throw new IndexOutOfBoundsException("Illegal index given to get operation.");
    }

    @Override
    public void set(int index, T item) {
        if (index >= 0 && index < numberOfEntries) {
            Node currentNode = firstNode;
            for (int i = 0; i < index; i++) {
                currentNode = currentNode.next;
            }
            currentNode.data = item;
        } else {
            throw new IndexOutOfBoundsException("Illegal index given to set operation.");
        }
    }

    @Override
    public int indexOf(T item) {
        int index = 0;
        Node currentNode = firstNode;
        while (currentNode != null) {
            if (currentNode.data.equals(item)) {
                return index;
            }
            currentNode = currentNode.next;
            index++;
        }
        return -1;
    }

    @Override
    public int size() {
        return numberOfEntries;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public int count(T item) {
        int count = 0;
        Node currentNode = firstNode;
        while (currentNode != null) {
            if (currentNode.data.equals(item)) {
                count++;
            }
            currentNode = currentNode.next;
        }
        return count;
    }

    @Override
    public boolean contains(T item) {
        return indexOf(item) != -1;
    }

    @Override
    public void clear() {
        firstNode = null;
        numberOfEntries = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    // iterator for query
    private class LinkedListIterator implements Iterator<T> {
        private Node currentNode = firstNode;

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = currentNode.data;
            currentNode = currentNode.next;
            return data;
        }
    }
}