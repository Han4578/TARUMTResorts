/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adt;

import java.io.Serializable;
import java.util.Iterator;

/**
 *
 * @author Liew Zheng Han
 * @param <T>
 */
public class ArrayList<T> implements ListInterface<T>, Serializable {
    private T[] data;
    int lastIndex = 0;
    
    public ArrayList() {
        this(8);
    }
    
    public ArrayList(int initialSize) {
        this.data = (T[]) new Object[Integer.max(initialSize, 1)];
    }

    @Override
    public void add(T item) {
        if (this.lastIndex == this.data.length) this.doubleArray();
        
        this.data[this.lastIndex++] = item;
    }

    @Override
    public void add(T item, int index) {
        if (index < -this.lastIndex) throw new IndexOutOfBoundsException();
        
        if (index > this.lastIndex) this.set(index, item);
        else {
            if (this.lastIndex == this.data.length) this.doubleArray();
            if (index < 0) index = (index + this.lastIndex) % this.lastIndex;

            if (index < this.lastIndex) System.arraycopy(this.data, index, this.data, index + 1, this.lastIndex - index);
            this.data[index] = item;
            ++this.lastIndex;
        }
    }

    @Override
    public T remove(int index) {
        if (index < -this.lastIndex || index >= this.lastIndex) throw new IndexOutOfBoundsException();
        T removedItem = this.data[(index + this.lastIndex) % this.lastIndex];
        
        --this.lastIndex;
        System.arraycopy(this.data, index + 1, this.data, index, this.lastIndex - index);
        
        this.data[this.lastIndex] = null;
        return removedItem;
    }

    @Override
    public T get(int index) {
        if (index < -this.lastIndex) throw new IndexOutOfBoundsException();
        while (index >= this.data.length) this.doubleArray();
        return index < 0? this.data[(index + this.lastIndex) % this.lastIndex]: this.data[index];
    }

    @Override
    public void set(int index, T item) {
        if (index < -this.lastIndex) throw new IndexOutOfBoundsException();
        if (index < 0) index = (index + this.lastIndex) % this.lastIndex;
        while (index >= this.data.length) this.doubleArray();
        if (index >= this.lastIndex) this.lastIndex = index + 1;
        this.data[index] = item;
    }

    @Override
    public int indexOf(T item) {
        for (int i = 0; i < this.lastIndex; ++i) {
            if (item.equals(this.data[i])) return i;
        }
        
        return -1;
    }

    @Override
    public int size() {
        return this.lastIndex;
    }

    @Override
    public boolean isEmpty() {
        return this.lastIndex == 0;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public int count(T item) {
        int counted = 0;
        
        for (T item2 : this.data) {
            if (item.equals(item2)) ++counted;
        }
        
        return counted;
    }

    @Override
    public boolean contains(T item) {
        for (T item2 : this.data) {
            if (item.equals(item2)) return true;
        }
        
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.lastIndex; ++i) {
            this.data[i] = null;
        }
    }
    
    private void doubleArray() {
        T[] newArray = (T[]) new Object[this.data.length << 1];
        System.arraycopy(this.data, 0, newArray, 0, lastIndex);
        this.data = newArray;
    }

    @Override
    public Iterator<T> iterator() {
        return new CustomIterator();
    }
    
    private class CustomIterator implements Iterator {
        int index = 0;

        @Override
        public boolean hasNext() {
            return index >= lastIndex;
        }

        @Override
        public T next() {
            return data[index++];
        }
    }
}
