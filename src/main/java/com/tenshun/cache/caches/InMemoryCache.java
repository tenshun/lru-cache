package com.tenshun.cache.caches;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.EvictionListener;
import org.apache.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Optional;


public class InMemoryCache<K, V> implements Cache<K, V> {

    private static final Logger LOGGER = Logger.getLogger(InMemoryCache.class);

    private final static long MAX_CACHE_CAPACITY = 10000;
    private final static long DEFAULT_CACHE_CAPACITY = 1000;

    private ConcurrentLinkedHashMap<K, V> lruCache;

    public InMemoryCache(long capacity, EvictionListener<K, V> listener) {
        lruCache = new ConcurrentLinkedHashMap.Builder<K, V>()
                .maximumWeightedCapacity(checkAndSetCapacity(capacity))
                .listener(listener)
                .build();
    }

    public InMemoryCache(long capacity) {
        lruCache = new ConcurrentLinkedHashMap.Builder<K, V>()
                .maximumWeightedCapacity(checkAndSetCapacity(capacity))
                .build();
    }

    public InMemoryCache() {
        this(DEFAULT_CACHE_CAPACITY);
    }

    private static long checkAndSetCapacity(long capacity){
        if (capacity > MAX_CACHE_CAPACITY) {
            capacity = MAX_CACHE_CAPACITY;
        } else if (capacity <= 0) {
            capacity = DEFAULT_CACHE_CAPACITY;
        }
        return capacity;
    }

    public long getCapacity() {
        return lruCache.capacity();
    }


    @Nonnull
    @Override
    public Optional<V> get(@Nonnull K key) {
        V v = lruCache.get(key);
        if (v == null) return Optional.empty();
        return Optional.of(v);
    }

    @Override
    public void cache(@Nonnull K key, @Nonnull V value) {
        lruCache.put(key, value);
    }

    @Nonnull
    @Override
    public Optional<V> remove(@Nonnull K key) {
        V removed = lruCache.remove(key);
        if (removed == null) return Optional.empty();
        return Optional.of(removed);
    }

    @Override
    public int size() {
        return lruCache.size();
    }

    @Override
    public boolean containsKey(@Nonnull K key) {
        return lruCache.containsKey(key);
    }

    @Override
    public void clearCache() {
        lruCache.clear();
    }

}
