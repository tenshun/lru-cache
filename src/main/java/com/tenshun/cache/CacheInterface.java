package com.tenshun.cache;

import java.util.Map;

/**
 * 03.05.2017.
 */
public interface CacheInterface<K, V> {

    V getIfPresent(@Nonnull Object key);

    Map<K, V> getAllPresent(@Nonnull Iterable<?> keys);

    void put(@Nonnull K key, @Nonnull V value);

    void putAll(@Nonnull Map<? extends K,? extends V> map);

    void invalidateAll();

    default void invalidateAll(Iterable<?> keys) {
        for (Object key : keys) {
            remove(key);
        }
    }
}
