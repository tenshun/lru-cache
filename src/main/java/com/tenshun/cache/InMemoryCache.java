package com.tenshun.cache;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;


/**
 * 03.05.2017.
 */
public class InMemoryCache<K, V> implements Cache<K, V> {

    private static final Logger LOGGER = Logger.getLogger(InMemoryCache.class);
    private final static int CACHE_MAX_CAPACITY = 10000;

    private final static int IN_MEMORY_DEFAULT_CAPACITY = 1000;
    //DEFAULT_NUMBER_OF_RETRIEVAL_OBJECTS = 3

    private int capacity;


    Map<K, V> lruCache;

    public InMemoryCache() {
        if(capacity > CACHE_MAX_CAPACITY){
            this.capacity = CACHE_MAX_CAPACITY;
        }

        lruCache = new ConcurrentLinkedHashMap.Builder<K, V>().initialCapacity(10000).build();
        lruCache = new LinkedHashMap<K, V>(capacity, 0.75f, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > CACHE_MAX_CAPACITY;
            }
        };

    }


    @Override
    public V get(K key) throws IOException {
        return null;
    }

    @Override
    public void cache(K key, V value) throws IOException {

    }

    @Override
    public V remove(K key) throws IOException {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean containsKey(K key) {
        return false;
    }

    @Override
    public void clearCache() {

    }
}
