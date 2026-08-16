/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package adt;

import java.util.function.Predicate;

/**
 *
 * @author Liew Zheng Han
 * @param <T>
 */public interface SortedListInterface<T> extends Iterable<T>{
    public void add(T item);
    public T remove(int index);
    public T remove(T item);
    public T get(int index);
    public int indexOf(T item);
    public int size();
    public boolean isEmpty();
    public boolean isFull();
    public int count(T item);
    public boolean contains(T item);
    public void clear();
    public int binarySearch(Predicate<T> condition);
    public int binarySearch(T item);
}
