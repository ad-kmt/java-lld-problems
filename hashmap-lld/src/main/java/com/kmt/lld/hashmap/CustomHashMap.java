package com.kmt.lld.hashmap;

import java.util.Objects;

/**
 * Custom implementation of a HashMap in Java.
 *
 * This implementation uses an array of linked lists (buckets) to handle collisions via chaining.
 * Each bucket is a linked list of nodes where each node contains a key-value pair.
 * The capacity and load factor determine when the HashMap will resize to accommodate more entries.
 *
 * - When a key is inserted, its hash code determines the index in the array.
 * - If the bucket at that index is empty, the key-value pair is added directly.
 * - If the bucket is not empty, the implementation checks for key equality to either update the existing value or add a new node at the beginning of the list.
 * - Resizing occurs when the number of entries exceeds the capacity multiplied by the load factor.
 */
public class CustomHashMap<K, V> {

    // Default initial capacity of the HashMap
    private static final int DEFAULT_CAPACITY = 16;

    // Default load factor (threshold for resizing)
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    // Current capacity of the HashMap
    private int capacity;

    // Load factor to determine when to resize
    private float loadFactor;

    // Number of key-value pairs in the HashMap
    private int size;

    // Array of buckets (each bucket is a linked list of nodes)
    private Node<K, V>[] hashTable;

    /**
     * Node class representing a key-value pair in the HashMap.
     */
    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    /**
     * Constructs a new CustomHashMap with default capacity and load factor.
     */
    public CustomHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs a new CustomHashMap with specified capacity and load factor.
     *
     * @param capacity   Initial capacity of the HashMap.
     * @param loadFactor Load factor for resizing.
     */
    @SuppressWarnings("unchecked")
    public CustomHashMap(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.hashTable = (Node<K, V>[]) new Node[capacity];
    }

    /**
     * Computes the hash code for a given key.
     *
     * @param key The key to hash.
     * @return The index in the hashTable array.
     */
    private int hash(K key) {
        return Math.abs(Objects.hashCode(key)) % capacity;
    }

    /**
     * Inserts a key-value pair into the HashMap.
     *
     * @param key   The key.
     * @param value The value.
     */
    public void put(K key, V value) {
        int index = hash(key);  // Compute the bucket index
        Node<K, V> node = hashTable[index];

        // Traverse the linked list at the bucket index
        while (node != null) {
            if (node.key.equals(key)) {
                node.value = value;  // Update existing key
                return;
            }
            node = node.next;
        }

        // Insert new node at the beginning of the linked list
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = hashTable[index];
        hashTable[index] = newNode;
        size++;

        // Resize if the load factor threshold is exceeded
        if (size > capacity * loadFactor) {
            resize();
        }
    }

    /**
     * Retrieves the value associated with a given key.
     *
     * @param key The key.
     * @return The value, or null if the key does not exist.
     */
    public V get(K key) {
        int index = hash(key);  // Compute the bucket index
        Node<K, V> node = hashTable[index];

        // Traverse the linked list at the bucket index
        while (node != null) {
            if (node.key.equals(key)) {
                return node.value;  // Return value if key is found
            }
            node = node.next;
        }
        return null;  // Return null if key is not found
    }

    /**
     * Removes the key-value pair associated with a given key.
     *
     * @param key The key.
     */
    public void remove(K key) {
        int index = hash(key);  // Compute the bucket index
        Node<K, V> curr = hashTable[index];
        Node<K, V> prev = null;

        // Traverse the linked list at the bucket index
        while (curr != null) {
            if (curr.key.equals(key)) {
                if (prev == null) {
                    hashTable[index] = curr.next;  // Remove node from head
                } else {
                    prev.next = curr.next;  // Remove node from middle/end
                }
                size--;
                return;  // Return after removal
            }
            prev = curr;
            curr = curr.next;
        }
    }

    /**
     * Resizes the hashTable array to double its current capacity.
     */
    private void resize() {
        int newCapacity = capacity * 2;  // Double the capacity

        @SuppressWarnings("unchecked")
        Node<K, V>[] newHashTable = (Node<K, V>[]) new Node[newCapacity];

        // Rehash all existing nodes and insert them into the new hash table
        for (int i = 0; i < capacity; i++) {
            Node<K, V> node = hashTable[i];
            while (node != null) {
                Node<K, V> nextNode = node.next;  // Save next node
                int index = Math.abs(Objects.hashCode(node.key)) % newCapacity;  // Recompute bucket index
                node.next = newHashTable[index];
                newHashTable[index] = node;
                node = nextNode;
            }
        }

        hashTable = newHashTable;  // Update hash table reference
        capacity = newCapacity;  // Update capacity
    }
}
