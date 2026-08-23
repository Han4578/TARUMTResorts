package adt;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Tan Kah Chao
 */
import java.io.Serializable;
import java.math.BigInteger;

public class HashedDictionary<K, V> implements DictionaryInterface<K, V>, Serializable {
    private Entry<K, V>[] table;
    private double loadFactor;
    private int size, threshold;
    private int usedBuckets, keyCount;
    
    private final Entry<K, V> TOMBSTONE = new Entry<>(null, null);
    
    private static int DEFAULT_SIZE = 7;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    
    public HashedDictionary() {
        this(DEFAULT_SIZE, DEFAULT_LOAD_FACTOR);
    }

    public HashedDictionary(int size) {
        this(size, DEFAULT_LOAD_FACTOR);
    }
    
    public HashedDictionary(int size, double loadFactor) {
        this.loadFactor = loadFactor;
        this.size = Math.max(DEFAULT_SIZE, size);
        adjustSize();
        threshold = (int) (this.size * loadFactor);
        
        table = (Entry<K, V>[]) new Entry[this.size];
    }
    
    private int hash1(K key) {
        return normalizeIndex(key.hashCode());
    }
    
    private int hash2(K key) {
        int h = key.hashCode() & 0x7FFFFFFF;
        return 1 + (h % (size - 1));
    }
    
    private int probe(int x, int h2) {
        return x * h2;
    }
    
    private void adjustSize() {
        while(!BigInteger.valueOf(size).isProbablePrime(20)) {
            size++;
        }
    }
    
    private void increaseSize() {
        size = (2 * size) + 1;
    }
    
    @Override
    public V insert(K key, V value) {
        if(key == null)
            throw new IllegalArgumentException("Null key");
        
        if (usedBuckets >= threshold)
            resizeTable();
        
        int h1 = hash1(key);
        int h2 = hash2(key);
        
        for(int x = 0, j = -1; ; x++) {
            int index = normalizeIndex(h1 + probe(x , h2));
            
            if(table[index] == TOMBSTONE) {
                if (j == -1) {
                    j = index;
                }
            } else if (table[index] != null) {
                if(table[index].getKey().equals(key)){
                    V oldValue = table[index].getValue();
                    if(j == -1) {
                        table[index] = new Entry<>(key, value);
                    } else {
                        table[index] = TOMBSTONE;
                        table[j] = new Entry<>(key, value);
                        
                    }
                
                return oldValue;
                } 
            } else {
                if (j == -1) {
                    usedBuckets++;
                    keyCount++;
                    table[index] = new Entry<>(key, value);
                } else {
                    keyCount++;
                    table[j] = new Entry<>(key, value);
                }
                
                return null;
            }
        }
    }
    
    @Override
    public boolean containsKey(K key) {
        if(key == null) {
            throw new IllegalArgumentException("Null key");
        }
        
        int h1 = hash1(key);
        int h2 = hash2(key);
        
        for(int x = 0, j = -1; ; x++) {
            int index = normalizeIndex(h1 + probe(x, h2));
            
            if(table[index] == TOMBSTONE) {
                if(j == -1)
                    j = index;
            } else if (table[index] != null) {
                if(table[index].getKey().equals(key)) {
                   if(j != -1) {
                       table[j] = table[index];
                       table[index] = TOMBSTONE;
                   }
                   return true;
                } 
            } else {
                return false;
            }
        }
    }
    
    @Override
    public void displayTable() {
    System.out.println("Hash Table:");

    for (int i = 0; i < table.length; i++) {

        System.out.print("[" + i + "] ");

        if (table[i] == null) {
            System.out.println("-");
        } else if (table[i] == TOMBSTONE) {
            System.out.println("TOMBSTONE");
        } else {
            System.out.println(
                table[i].getKey() + " -> " + table[i].getValue()
            );
        }
    }
}
    
    @Override
    public V get(K key) {
        if (key == null)
            throw new IllegalArgumentException("Null key");
            
        int h1 = hash1(key);
        int h2 = hash2(key);
        
        for (int x = 0, j = -1; ; x++) {
            int index = normalizeIndex(h1 + probe(x, h2));
            
            
            if(table[index] == TOMBSTONE) {
                if(j == -1) {
                    j = index;
                }
            } else if (table[index] != null) {
                if (table[index].getKey().equals(key)) {
                    if(j != -1) {
                        table[j] = table[index];
                        table[index] = TOMBSTONE;

                        return table[j].getValue();
                    }
                    return table[index].getValue();
                }
                
            } else {
                return null;
            }
        }
    }

    @Override
    public V remove(K key) {
        if(key == null) 
            throw new IllegalArgumentException("Null key");
        
        int h1 = hash1(key);
        int h2 = hash2(key);
        
        for(int x = 0; ; x++) {
            int index = normalizeIndex(h1 + probe(x, h2));
            
            if(table[index] == TOMBSTONE)
                continue;
            
            if(table[index] == null) 
                return null;
            
            if(table[index].getKey().equals(key)) {
                keyCount--;
               
                
                V oldValue = table[index].getValue();
                
                table[index] = TOMBSTONE;
                
                return oldValue;
            }
        }
    }
    
    private void resizeTable() {
        increaseSize();
        adjustSize();
        threshold = (int) (size * loadFactor);
        
        Entry<K, V>[] oldTable = table;
        
        table = (Entry<K, V>[]) new Entry[size];
        
        keyCount = 0;
        usedBuckets = 0;
        
        for(int i = 0; i < oldTable.length; i ++) {
            if(oldTable[i] != null && oldTable[i] != TOMBSTONE) {
                insert(oldTable[i].getKey(), oldTable[i].getValue());
            }
            oldTable[i] = null;
        }

    }
    
    @Override
    public void clear() {
        for (int index = 0; index < table.length; index++) {
           table[index] = null;
         }

        keyCount = 0;
        usedBuckets = 0;
    }
    
    @Override
    public int keyCount() {
        return keyCount;
    }
    
    @Override
    public int getSize() {
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        return keyCount == 0;
    }
    
    @Override
    public ListInterface<V> getValues() {
        ListInterface<V> values = new ArrayList<>();
        
        for (Entry<K, V> entry : table) {
            if (entry != null && !entry.equals(TOMBSTONE)) values.add(entry.value);
        }
        
        return values;
    }
    
    private int normalizeIndex(int keyHash) {
        return (keyHash & 0x7FFFFFFF) % this.size;
    }
    
    
    private class Entry<K, V> implements Serializable {

        private final K key;
        private final V value;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        private K getKey() {
            return key;
        }

        private V getValue() {
            return value;
        }
    }
}   
