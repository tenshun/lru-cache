package com.tenshun.cache;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import org.apache.log4j.Logger;

import java.util.*;


/**
 * 03.05.2017.
 */
public class InMemoryCache<K, V> implements Cache {

    private static final Logger LOGGER = Logger.getLogger(InMemoryCache.class);
    private final static int CACHE_MAX_CAPACITY = 10000;

    private int capacity;



    /* Old version:
    private Map<K, V> myCache = Collections.synchronizedMap(new WeakHashMap<K, V>());
     */

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
    public Object get(Object key) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public void putAll(Map map) {

    }

    @Override
    public void remove(Object key) {

    }


    @Override
    public Map getAllPresent(Iterable keys) {
        return null;
    }


}
