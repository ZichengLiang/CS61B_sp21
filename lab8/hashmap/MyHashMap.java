package hashmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.collections.list.NodeCachingLinkedList;


/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author Zicheng Liang
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    private double LOAD_FACTOR = 0.75;
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int initialSize = 16;
    private int numElements = 0;
    private int numBuckets = 0;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        buckets = createTable(initialSize);
    }

    public MyHashMap(int initialSize) {
        buckets = createTable(initialSize);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        LOAD_FACTOR = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        numBuckets = tableSize;
        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    @Override
    public void clear() {
        for (Collection<Node> bucket : buckets) {
            bucket.clear();
        }
        numElements = 0;
    }

    private Node find(K key) {
        int index = getBucketIndex(key);
        if (index < numBuckets && buckets[index] != null) {
            for (Node n : buckets[index]) {
                if (n.key.equals(key)) {
                    return n;
                }
            }
        }
        return null;
    }

    private Node find(K key, Collection<Node>[] table) {
        int index = getBucketIndex(key);
        if (index < table.length && table[index] != null) {
            for (Node n : table[index]) {
                if (n.key.equals(key)) {
                    return n;
                }
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return find(key) != null;
    }

    @Override
    public V get(K key) {
        Node n = find(key);
        if (n != null) {
            return find(key).value;
        }
        return null;
    }

    @Override
    public int size() {
        return numElements;
    }

    @Override
    public void put(K key, V value) {
        int index = getBucketIndex(key);
        Node n = find(key);

        if (buckets[index] == null) {
            buckets[index] = createBucket();
        }

        if (n != null) {
            remove(key);
        }

        buckets[index]
                .add(createNode(key, value));
        numElements++;
    }

    private void put(K key, V value, Collection<Node>[] table) {
        int index = getBucketIndex(key);
        Node n = find(key);
    }

    private int getBucketIndex(K key) {
        int index = ((key.hashCode() % numBuckets) + numBuckets) % numBuckets;
        return index;
    }

    @Override
    public Set<K> keySet() {
        Set keys = new HashSet();
        for (Collection<Node> bucket : buckets) {
            if (bucket != null) {
                for (Node n : bucket) {
                    keys.add(n.key);
                }
            }
        }
        return keys;
    }

    @Override
    public V remove(K key) {
        int index = getBucketIndex(key);
        if (buckets[index] != null) {
           for (Node n : buckets[index]) {
               if (n != null && n.key == key) {
                   V value = n.value;
                   buckets[index].remove(n);
                   numElements--;
                   return value;
               }
           }
        }
        return null;
    }

    @Override
    public V remove(K key, V value) {
        if (this.containsKey(key)) {
            remove(key);
        }
        return null;
    }

    public void resize() {
        if ( (double) numElements / (double) numBuckets >= LOAD_FACTOR) {
            Collection<Node>[] newTable = createTable(numBuckets * 2);
            for (K key : this) {
                //put(key);
            }
        }
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

}
