/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package adt;

import java.io.Serializable;

/**
 *
 * @author chao_
 * @param <K>
 * @param <V>
 */
public interface DictionaryInterface<K, V> extends Serializable {
    public V insert(K key, V value);
    public boolean containsKey(K key);
    public V get(K key);
    public V remove(K key);
    public void clear();
    public int keyCount();
    public int getSize();
    public boolean isEmpty();
    public void displayTable();
    public ListInterface<V> getValues();
}
