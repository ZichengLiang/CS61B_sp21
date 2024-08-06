package bstmap;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.BST;

public class BSTMap<K extends Comparable, V> implements Map61B<K, V>{
    private class BSTNode {
        K key;
        V value;
        BSTNode parent, left, right;

        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public BSTNode getParent() {
            return this.parent;
        }

        public BSTNode getLeft() {
            return this.left;
        }

        public BSTNode getRight() {
            return this.right;
        }

        public void setParent(BSTNode parent) {
            this.parent = parent;
        }

        public void setLeft(BSTNode left) {
            this.left = left;
        }

        public void setRight(BSTNode right) {
            this.right = right;
        }
    }

    private BSTNode root = new BSTNode(null, null);
    private int size = 0;
    public BSTMap() {}

    public void printInOrder() {

    }
    @Override
    public void clear() {
        root = new BSTNode(null, null);
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        if (findNode(key).getKey() == null) { return false; }
        return (findNode(key)
                .getKey() // the returned node will be the exact one if exists
                .equals(key) // it can also be its parent if this does not exist
        );
    }

    /** return the exact node if found the key, or the parent node if key not found. */
    public BSTNode findNode(K key) {
        BSTNode curr = root;
        return findNodeHelper(curr, key);
    }
    public BSTNode findNodeHelper(BSTNode curr, K key) {
        if (curr.key == null) {
            return curr; // only for the initialisation
        }

        if (key.compareTo(curr.key) == 0) {
            return curr;
        } else if (key.compareTo(curr.key) > 0 && curr.right != null) {
            // if the passed key is greater than current node's
            curr = curr.getRight();
            return findNodeHelper(curr, key);
        } else if (key.compareTo(curr.key) < 0 && curr.left != null){
            curr = curr.getLeft();
            return findNodeHelper(curr, key);
        }

        return curr;
    }

    @Override
    public V get(K key) {
        if (this.containsKey(key)) {
            return findNode(key).getValue();
        } else {
            return null;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        BSTNode curr = root;

        if (curr.key == null) {
            curr.key = key;
            curr.value = value;
            size += 1;
        }

        if (this.containsKey(key)) {
            System.out.printf("This key: %s has already existed.\n", key.toString());
        } else {
            BSTNode parent = findNode(key);
            if (key.compareTo(parent.key) > 0) {
                parent.setRight(new BSTNode(key, value));
            } else {
                parent.setLeft(new BSTNode(key, value));
            }
            size += 1;
        }

    }

    @Override
    public Set keySet() {
        Set<K> returnSet = new TreeSet<>();
        BSTNode curr = root;

        treeWalk(curr, returnSet);

        return returnSet;
    }

    public void treeWalk(BSTNode curr, Set<K> returnSet) {
        if (curr.key != null) {
            returnSet.add(curr.key);
            if (curr.getLeft() != null) {
                treeWalk(curr.getLeft(), returnSet);
            }
            if (curr.getRight() != null) {
                treeWalk(curr.getRight(), returnSet);
            }
        }
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException();
    }
}
