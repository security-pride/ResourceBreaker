package com.userxxx.core;

public class Pair<K, V> {
    public final K key;
    public final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getLeft() {
        return key;
    }

    public V getRight() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return (key == null ? pair.key == null : key.equals(pair.key)) &&
                (value == null ? pair.value == null : value.equals(pair.value));
    }

    @Override
    public int hashCode() {
        int result = (key == null ? 0 : key.hashCode());
        result = 31 * result + (value == null ? 0 : value.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}