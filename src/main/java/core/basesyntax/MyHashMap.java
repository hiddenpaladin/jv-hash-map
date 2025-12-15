package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K,V> implements MyMap<K,V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int size = 0;
    private int currentCapacity = 16;
    public Node <K,V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        if (size >= currentCapacity * DEFAULT_LOAD_FACTOR) {
            reSize();
        }
        int hash = key == null ? 0 : key.hashCode();
        Node<K,V> newNode = new Node<K,V>(key,value,hash);
        putByIndex(table,(newNode.hash & 0x7fffffff) % table.length, newNode);
    }

    @Override
    public V getValue(K key) {
        int index = (key.hashCode() & 0x7fffffff) % table.length;
        Node<K,V> currentNode = table[index];
        if (currentNode == null) {
            return null;
        }
        while (currentNode.key != key ) {
            if (currentNode.next != null) {
                currentNode = currentNode.next;
            } else {
                throw new RuntimeException("input key is invalid");
            }
        }
        return currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void reSize() {
        if ((int)(currentCapacity + currentCapacity * DEFAULT_LOAD_FACTOR) < MAXIMUM_CAPACITY) {
            currentCapacity = (int)(currentCapacity + currentCapacity * DEFAULT_LOAD_FACTOR);
            Node<K,V>[] newTable = new Node[(int)(currentCapacity + currentCapacity * DEFAULT_LOAD_FACTOR)];
            for (Node<K,V> node : table) {
                if (node != null) {
                    putByIndex(newTable,node.key.hashCode(), node);
                    while (node.next != null) {
                        node = node.next;
                        putByIndex(newTable,node.key.hashCode(), node);
                    }
                }
            }
            table = newTable;
            }
        }

    public void putByIndex (Node[] table,int index, Node<K,V> node) {
        if (Objects.equals(table[index],null)) {
            table[index] = node;
            size++;
        } else {
            boolean keyAlreadyExists = false;
            Node<K,V> currentNode = table[index];

            while (currentNode.next != null) {
                if (table[index].key.equals(node.key)) {
                    table[index].value = node.value;
                    keyAlreadyExists = true;
                    break;
                }
                currentNode = currentNode.next;
            }
            if (keyAlreadyExists == false) {
                currentNode.next = node;
                size++;
            }
        }
    }

    static class Node<K,V> {
        int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key,V data,int hash) {
            this.hash = hash;
            this.key = key;
            this.value = data;
            this.next = null;
        }
    }

}
