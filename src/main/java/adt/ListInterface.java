/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package adt;

/**
 *
 * @author Liew Zheng Han
 * @param <T>
 */
public interface ListInterface<T> extends Iterable<T> {
    public void add(T item);
    public void add(T item, int index);
    public T remove(int index);
    public T get(int index);
    public void set(int index, T item);
    public int indexOf(T item);
    public int size();
    public boolean isEmpty();
    public boolean isFull();
    public int count(T item);
    public boolean contains(T item);
    public void clear();
}
