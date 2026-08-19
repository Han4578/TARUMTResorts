/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package adt;

import java.util.Iterator;
import java.util.function.Predicate;

/**
 *
 * @author Liew Zheng Han
 * @param <T>
 */
public interface PriorityQueueInterface<T> extends Iterable<T> {
    public void insert(T item, int priority);
    public T pop();
    public T peek();
    public int size();
    public void mergePriority(int priorityFrom, int priorityTo);
    public void mergePriority(int priorityFrom, int priorityTo, Predicate<T> condition);
    public void movePriority(int priorityFrom, int priorityTo);
    public void movePriority(int priorityFrom, int priorityTo, Predicate<T> condition);
    public void clear();
    public void clear(int priority);
    public void clear(int priority, Predicate<T> condition);
    public boolean isEmpty();
    public boolean isEmpty(int priority);
    public Iterator getIterator();
}
