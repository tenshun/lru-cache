package com.tenshun.cache.caches;

import javax.annotation.Nonnull;
import java.util.Optional;


/**
 *
 * Interface for cache
 */

public interface Cache<K, V> {

    @Nonnull
    Optional<V> get(@Nonnull K key);

    /**
     * Puts to the cache
     *
     * @param key
     * @param value
     */

    void cache(@Nonnull K key, @Nonnull V value);

    /**
     * Removes element from cache
     *
     * @param key element to remove
     */

    @Nonnull
    Optional<V> remove(@Nonnull K key);

    /**
     * Returns the amount of elements cached
     *
     * @return
     */

    int size();

    boolean containsKey(@Nonnull K key);

    /**
     * Deletes all elements from cache
     */

    void clearCache();


}
