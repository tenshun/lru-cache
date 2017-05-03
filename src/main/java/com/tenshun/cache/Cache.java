package com.tenshun.cache;

import java.util.Map;

public interface Cache<K, V> {

    V get(K key);

    Map<K, V> getAllPresent(Iterable<K> keys);

    void put(K key, V value);

    void putAll(Map<? extends K,? extends V> map);

    void remove(K key);

    default void invalidateAll(Iterable<K> keys) {
        for (K key : keys) {
            remove(key);
        }
    }
}
