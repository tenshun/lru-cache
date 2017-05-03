package com.tenshun.cache;

import org.apache.log4j.Logger;

import java.util.*;


/**
 * 03.05.2017.
 */
public class Cache <K, V> implements CacheInterface{

    private static final Logger LOGGER = Logger.getLogger(Cache.class);
    private final static int CACHE_MAX_CAPACITY = 1000;
    private static int EXPIRED_TIME_IN_SEC_MIN = 10000;
    private static int EXPIRED_TIME_IN_SEC_MAX = Integer.MAX_VALUE;

    private int capacity;
    private int expiry;



    private int timeToExpire;
    private Set<CacheStrategy> strategies = new HashSet<>();

    /* Old version:
    private Map<K, V> myCache = Collections.synchronizedMap(new WeakHashMap<K, V>());
     */

    Map<K, V> lruCache;

    public Cache(int capacity, int timeToExpire) {
        if(capacity > CACHE_MAX_CAPACITY){
            capacity = CACHE_MAX_CAPACITY;
        }
        if (timeToExpire < EXPIRED_TIME_IN_SEC_MIN) {
            this.timeToExpire = EXPIRED_TIME_IN_SEC_MIN;
        }
        lruCache = new LinkedHashMap<K, V>(capacity, 0.75f, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > CACHE_MAX_CAPACITY;
            }
        };

    }



    public Cache addStrategy(CacheStrategy strategy){
        strategies.add(strategy);
        return this;
    }

    public V get(K key){
        return myCache.get(key);
    }


    @Override
    public Object getIfPresent(Object key) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public void putAll(Map map) {

    }

    @Override
    public void invalidateAll() {

    }

    @Override
    public Map getAllPresent(Iterable keys) {
        return null;
    }

    @Nonnull
    public Cache<K, V> expireAfterWrite(@Nonnegative long duration, @Nonnull TimeUnit unit) {
        requireState(expireAfterWriteNanos == UNSET_INT,
                "expireAfterWrite was already set to %s ns", expireAfterWriteNanos);
        requireState(expiry == null, "expireAfterAccess may not be used with variable expiration");
        requireArgument(duration >= 0, "duration cannot be negative: %s %s", duration, unit);
        this.expireAfterWriteNanos = unit.toNanos(duration);
        return this;
    }
}
