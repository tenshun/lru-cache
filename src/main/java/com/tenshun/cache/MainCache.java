package com.tenshun.cache;

import com.googlecode.concurrentlinkedhashmap.EvictionListener;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;

/**
 * 03.05.2017.
 */
public class MainCache<K extends Serializable, V extends Serializable> implements Cache<K, V> {

    private static final Logger LOGGER = Logger.getLogger(InMemoryCache.class);

    public CacheStrategy strategy;
    private InMemoryCache<K, V> inMemoryCache;
    private FileSystemCache<K, V> fileSystemCache;

    EvictionListener<K, V> listener = (key, value) -> {
        try {
            fileSystemCache.cache(key, value);
        } catch (IOException e) {
            LOGGER.error("IO exception");
        }
    };


    private MainCache() {
        

    }

    public static final class Builder {
        private CacheStrategy strategy;
        private int capacity;

        private Builder() {
        }

        public Builder capacity(int capacity) {
            this.capacity = capacity;
            return null;//todo
        }

        public Builder withStrategy(CacheStrategy strategy) {
            this.strategy = strategy;
            return this;
        }


        public MainCache build() {
            return new MainCache();
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public void recache(){

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
