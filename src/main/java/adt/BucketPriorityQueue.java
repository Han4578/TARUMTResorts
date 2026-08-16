/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adt;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 *
 * @author Liew Zheng Han
 * @param <T>
 */
public class BucketPriorityQueue<T> implements PriorityQueueInterface<T>, Serializable {
    private ListInterface<Node> buckets;
    private BitSet containsItem = new BitSet();
    private int numberOfItems = 0;
    private long latestNodeId = 0;
    
    public BucketPriorityQueue() {
        this(4);
    }
    
    public BucketPriorityQueue(int initialBucketCount) {
        this.buckets = new ArrayList<>(initialBucketCount);
    }

    @Override
    public void insert(T item, int priority) {
        if (priority < 0) throw new IllegalArgumentException("Priority cannot be less than 0");
        
        if (this.buckets.get(priority) == null) {
            this.buckets.set(priority, new Node(item, this.latestNodeId++));
            this.containsItem.set(priority);
        } else {
            this.buckets.set(priority, this.buckets.get(priority).addNext(item));
        }
        
        ++this.numberOfItems;
    }

    @Override
    public T pop() {
        int lowestPriority = this.containsItem.nextSetBit(0);
        if (lowestPriority == -1) return null;
        Node nodeTail = this.buckets.get(lowestPriority);
        
        if (!nodeTail.hasNext()) { // Only 1 node in the circular linked list
            this.clear(lowestPriority);
            return nodeTail.getItem();
        } else --this.numberOfItems;
        
        return nodeTail.removeNext(); // Remove head
    }

    @Override
    public T peek() {
        int lowestPriority = this.containsItem.nextSetBit(0);
        Node nodeTail = this.buckets.get(lowestPriority);
        if (nodeTail == null) return null;
        return nodeTail.getItem();
    }

    @Override
    public int size() {
        return this.numberOfItems;
    }

    @Override
    public void mergePriority(int priorityFrom, int priorityTo) {
        if (priorityFrom < 0 || priorityTo < 0) throw new IllegalArgumentException("Priority cannot be less than 0");
        
        if (!this.containsItem.get(priorityFrom)) return; // Nothing to merge
        
        if (!this.containsItem.get(priorityTo)) { // Just move, no need to merge
            this.containsItem.set(priorityTo);
            this.buckets.set(priorityTo, this.buckets.get(priorityFrom));
        } else { // Merge based on nodeId as arrival time 
            this.mergePriority(priorityTo, this.buckets.get(priorityFrom));
        }
        this.buckets.set(priorityFrom, null);
        this.containsItem.clear(priorityFrom);
    }

    @Override
    public void mergePriority(int priorityFrom, int priorityTo, Predicate<T> condition) {
        if (priorityFrom < 0 || priorityTo < 0) throw new IllegalArgumentException("Priority cannot be less than 0");

        if (!this.containsItem.get(priorityFrom)) return; // Nothing to merge
        
        Node filteredNodes = this.filterPriority(priorityFrom, condition);
        
        if (filteredNodes == null) return;
        
        if (!this.containsItem.get(priorityTo)) { // Just move, no need to merge
            this.containsItem.set(priorityTo);
            this.buckets.set(priorityTo, filteredNodes);
        } else { // Merge based on nodeId as arrival time
            this.mergePriority(priorityTo, filteredNodes);
        }
    }
    
    private void mergePriority(int priority, Node fromTail) {
        if (priority < 0) throw new IllegalArgumentException("Priority cannot be less than 0");
        
        Node fromHead = fromTail.getNext();
        Node toTail = this.buckets.get(priority);
        Node toHead = toTail.getNext();

        fromTail.setNext(null);
        toTail.setNext(null);

        Node current = new Node(-1);

        while (toHead != null && fromHead != null) {
            if (toHead.getNodeId() < fromHead.getNodeId()) {
                Node newHead = toHead.getNext();
                current = current.addNext(toHead);
                toHead = newHead;
            } else {
                Node newHead = fromHead.getNext();
                current = current.addNext(fromHead);
                fromHead = newHead;
            }
        }

        if (toHead != null) {
            toTail.setNext(current.getNext());
            current.setNext(toHead);
            current = toTail;
        } else if (fromHead != null) {
            fromTail.setNext(current.getNext());
            current.setNext(fromHead);
            current = fromTail;
        }

        current.removeNext(); // Remove blank node

        this.buckets.set(priority, current);
    }

    @Override
    public void swapPriority(int priorityA, int priorityB) {
        if (priorityA < 0 || priorityB < 0) throw new IllegalArgumentException("Priority cannot be less than 0");

        Node swap = this.buckets.get(priorityA);
        
        this.buckets.set(priorityA, this.buckets.get(priorityB));
        this.buckets.set(priorityB, swap);
        
        boolean bit = this.containsItem.get(priorityB);
        this.containsItem.set(priorityB, this.containsItem.get(priorityA));
        this.containsItem.set(priorityA, bit);
    }

    @Override
    public void movePriority(int priorityFrom, int priorityTo) { //Doesn't merge, shifts everything back (inclduding null) for consistency
        if (priorityFrom < 0 || priorityTo < 0) throw new IllegalArgumentException("Priority cannot be less than 0");
        
        Node node = this.buckets.get(priorityFrom);
        boolean val = this.containsItem.get(priorityFrom);
        
        this.buckets.set(priorityFrom, null);
        this.containsItem.clear(priorityFrom);
        
        this.buckets.add(node, priorityTo);
        
        for (int i = this.containsItem.size() - 1; i >= priorityTo; --i) {
            this.containsItem.set(i + 1, this.containsItem.get(i));
        }
        
        this.containsItem.set(priorityTo, val);
    }

    @Override
    public void movePriority(int priorityFrom, int priorityTo, Predicate<T> condition) { //Doesn't merge, shifts everything back (inclduding null) for consistency
        if (priorityFrom < 0 || priorityTo < 0) throw new IllegalArgumentException("Priority cannot be less than 0");
        
        Node filteredNodes = this.filterPriority(priorityFrom, condition);
        this.buckets.add(filteredNodes, priorityTo);
        boolean val = this.containsItem.get(priorityFrom);
        
        for (int i = this.containsItem.size() - 1; i >= priorityTo; --i) {
            this.containsItem.set(i + 1, this.containsItem.get(i));
        }
        
        this.containsItem.set(priorityTo, val);
    }
    
    private Node filterPriority(int priority, Predicate<T> condition) {
        if (priority < 0) throw new IllegalArgumentException("Priority cannot be less than 0");
        
        if (!this.containsItem.get(priority)) return null;
        
        Node tail = this.buckets.get(priority);
        Node head = new Node(-1);
        
        head.setNext(tail.getNext());
        tail.setNext(null);
        
        Node previous = head;
        Node current = head.getNext();
        Node filteredTail = new Node(-1);
        
        while (current != null) {
            if (condition.test(current.getItem())) {
                previous.removeNext();
                filteredTail = filteredTail.addNext(current);
                current = previous;
            }
            
            previous = current;
            current = current.getNext();
        }
        
        // Current is null and previous is now the tail. 
        previous.setNext(head); 
        
        if (previous.hasNext()) { // Remove blank head, and save to buckets
            previous.removeNext();
            this.buckets.set(priority, previous);
        } else { // Previous is the blank head, no nodes left in priority
            this.buckets.set(priority, null);
            this.containsItem.clear(priority);
        }
        
        if (!filteredTail.hasNext()) return null;
        
        filteredTail.removeNext(); // Remove blank
        
        return filteredTail;
    }

    @Override
    public void clear() {
        this.containsItem.clear();
        this.buckets = new ArrayList(4);
        this.numberOfItems = 0;
    }

    @Override
    public void clear(int priority) {
        if (priority < 0) throw new IllegalArgumentException("Priority cannot be less than 0");
        
        if (!this.containsItem.get(priority)) return;
        
        this.containsItem.clear(priority);
        Node tailNode = this.buckets.get(priority);
        if (tailNode == null) return;
        
        while (tailNode.hasNext()) {
            --this.numberOfItems;
            tailNode.removeNext();
        }
        
        --this.numberOfItems;
        this.buckets.set(priority, null);
    }

    @Override
    public void clear(int priority, Predicate<T> condition) {
        if (priority < 0) throw new IllegalArgumentException("Priority cannot be less than 0");
        
        if (!this.containsItem.get(priority)) return;
        
        Node tailNode = this.filterPriority(priority, condition);
        
        if (tailNode == null) return;
        
        while (tailNode.hasNext()) {
            --this.numberOfItems;
            tailNode.removeNext();
        }
        
        --this.numberOfItems;
    }

    @Override
    public int priorityCount() {
        return this.containsItem.cardinality();
    }

    @Override
    public boolean isEmpty() {
        return this.numberOfItems == 0;
    }

    @Override
    public boolean isEmpty(int priority) {
        if (priority < 0) throw new IllegalArgumentException("Priority cannot be less than 0");
        
        return !this.containsItem.get(priority);
    }

    @Override
    public Iterator iterator() {
        return new CustomIterator();
    }

    @Override
    public Iterator getIterator() {
        return iterator();
    }
    
    private class CustomIterator implements Iterator {
        private int currentPriority;
        Node previous;
        Node current;
        
        private CustomIterator() {
            this.currentPriority = -1;
            this.current = null;
            this.previous = null;
        }

        @Override
        public boolean hasNext() {
            if (this.current != null && this.current.hasNext() && this.current.getNodeId() < this.current.getNext().getNodeId()) return true; // Same circular queue
            return containsItem.nextSetBit(this.currentPriority + 1) != -1;
        }

        @Override
        public T next() {
            // If circular queue about to loop back to start, check next circular queue
            if (this.current == null || this.current.getNodeId() >= this.current.getNext().getNodeId()) {
                int nextPriority = containsItem.nextSetBit(this.currentPriority + 1);
                if (nextPriority == -1) throw new NoSuchElementException();
                
                this.currentPriority = nextPriority;
                this.current = buckets.get(nextPriority);
            }
            
            this.previous = this.current;
            this.current = this.current.getNext();
            return this.current.getItem();
        }

        @Override
        public void remove() {
            if (this.previous == null) throw new IllegalStateException();
            
            if (this.current == this.previous) { // Circular queue with 1 node
                clear(this.currentPriority);
            } else {
                this.previous.removeNext();
                this.current = this.previous;
            }
            
            this.previous = null;
        }
    }
    
    private class Node {
        private Node next = null;
        private T item = null;
        long nodeId;
        
        private Node() {
            this.next = this;
            this.nodeId = latestNodeId++;
        }
        
        private Node(long nodeId) {
            this.next = this;
            this.nodeId = nodeId;
        }
        
        private Node(T item, long nodeId) {
            this.item = item;
            this.next = this;
            this.nodeId = nodeId;
        }
        
        private long getNodeId() {
            return this.nodeId;
        }
        
        private Node getNext() {
            return this.next;
        }
        
        private T getItem() {
            return this.item;
        }
        
        private void setNext(Node next) {
            this.next = next;
        }
        
        private void setItem(T item) {
            this.item = item;
        }
        
        private Node addNext(T item) {
            Node nextNode = new Node(item, latestNodeId++);
            nextNode.setNext(this.next);
            this.next = nextNode;
            return nextNode;
        }
        
        private Node addNext(Node node) {
            node.setNext(this.next);
            this.next = node;
            return node;
        }
        
        private T removeNext() {
            if (!this.hasNext()) return this.item;
            
            T nextItem = this.next.getItem();
            this.next = this.next.getNext();
            return nextItem;
        }
        
        private boolean hasNext() {
            return this.next != null && this.next != this;
        }

        @Override
        public String toString() {
            return "Node: " + this.nodeId;
        }
        
        
    }
}
