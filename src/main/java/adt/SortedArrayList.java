/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adt;

import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Predicate;

/**
 *
 * @author Liew Zheng Han
 * @param <T>
 */
public class SortedArrayList<T extends Comparable<T>> implements SortedListInterface<T>, Serializable {
    private ListInterface<T> data;
    
    public SortedArrayList() {
        this(8);
    }
    
    public SortedArrayList(int initialSize) {
        this.data = new ArrayList<>(initialSize);
    }

    @Override
    public void add(T item) {
        if (this.data.isEmpty() || this.data.get(-1).compareTo(item) <= 0) this.data.add(item);
        else this.data.add(item, this.binarySearch(item2 -> item.compareTo(item2) <= 0));
    }

    @Override
    public T remove(T item) {
        int index = this.indexOf(item);
        if (index != -1) return this.data.remove(index);
        
        return null;
    }

    @Override
    public T remove(int index) {
        return this.data.remove(index);
    }

    @Override
    public T get(int index) {
        return this.data.get(index);
    }

    @Override
    public int indexOf(T item) {        
        for (int i = this.binarySearch(item); i < this.data.size(); ++i) {
            if (this.data.get(i).equals(item)) return i;
            if (this.data.get(i).compareTo(item) != 0) return -1;
        }
        
        return -1;
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    @Override
    public boolean isFull() {
        return this.data.isFull();
    }

    @Override
    public int count(T item) {
        int count = 0;
        int i = this.indexOf(item);
        if (i == -1) return 0;
        
        for (; i < this.data.size(); ++i) {
            if (item.equals(this.data.get(i))) ++count;
            else break;
        }
        
        return count;
    }

    @Override
    public boolean contains(T item) {        
        return this.indexOf(item) != -1;
    }

    @Override
    public int binarySearch(Predicate<T> condition) {
        int low = 0;
        int high = this.data.size() - 1;
        int index = this.data.size();
        
        while (low <= high) {
            int middle = (low + high) / 2;
            T item = this.data.get(middle);
            
            if (condition.test(item)) {
                high = middle - 1;
                index = middle;
            }
            else low = middle + 1;
        }
        
        return index;
    }

    @Override
    public int binarySearch(T item) {
        return this.binarySearch(item2 -> item.compareTo(item2) <= 0);
    }
    
    

    @Override
    public void clear() {
        this.data.clear();
    }

    @Override
    public Iterator<T> iterator() {
        return this.data.iterator();
    }    
}
