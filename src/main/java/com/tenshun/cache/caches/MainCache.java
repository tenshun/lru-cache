package com.tenshun.cache.caches;

import com.googlecode.concurrentlinkedhashmap.EvictionListener;
import org.apache.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Optional;

/**
 * 03.05.2017.
 */
public class MainCache<K extends Serializable, V extends Serializable> implements Cache<K, V> {

    private static final Logger LOGGER = Logger.getLogger(InMemoryCache.class);


    private final static CacheStrategy DEFAULT_STRATEGY = CacheStrategy.ONLY_IN_MEMORY_CACHE;

    private CacheStrategy strategy;
    private InMemoryCache<K, V> inMemoryCache;
    private FileSystemCache<K, V> fileSystemCache;


    public MainCache(Builder builder) {

        this.strategy = builder.strategy;
        switch (strategy) {
            case ONLY_IN_MEMORY_CACHE: {
                LOGGER.info("Configuring ONLY_IN_MEMORY_CACHE...");

                EvictionListener<K, V> listener = (key, value) -> {
                }; //do nothing
                inMemoryCache = new InMemoryCache<>(builder.capacity, listener);
                LOGGER.info("Done.");
            } break;
            case TWO_LEVEL_CACHE: {
                LOGGER.info("Configuring TWO_LEVEL_CACHE...");

                fileSystemCache = new FileSystemCache<>();
                inMemoryCache = new InMemoryCache<>(builder.capacity, (key, value) -> {
                    fileSystemCache.cache(key, value); //save object to second level cache
                });
                LOGGER.info("Done.");
            } break;
        }

    }

    @Nonnull
    @Override
    public Optional<V> get(@Nonnull K key) {
        Optional<V> value1 = inMemoryCache.get(key);
        if (!value1.isPresent() && strategy == CacheStrategy.TWO_LEVEL_CACHE) {
            return fileSystemCache.get(key);
        }
        return value1;
    }

    @Override
    public void cache(@Nonnull K key, @Nonnull V value){
        inMemoryCache.cache(key, value);
    }

    @Nonnull
    @Override
    public Optional<V> remove(@Nonnull K key){
        Optional<V> removed = inMemoryCache.remove(key);
        if (!removed.isPresent() && strategy == CacheStrategy.TWO_LEVEL_CACHE) {
            return fileSystemCache.remove(key);
        }
        return removed;
    }

    @Override
    public int size() {
        return inMemoryCache.size();
    }

    @Override
    public boolean containsKey(@Nonnull K key) {
        return inMemoryCache.containsKey(key) || strategy == CacheStrategy.TWO_LEVEL_CACHE && fileSystemCache.containsKey(key);
    }

    @Override
    public void clearCache() {
        inMemoryCache.clearCache();
        if (strategy == CacheStrategy.TWO_LEVEL_CACHE) {
            fileSystemCache.clearCache();
        }
    }

    public static final class Builder<K extends Serializable, V extends Serializable> {
        private CacheStrategy strategy;
        private long capacity;

        public Builder(){
            this.capacity = -1;
            this.strategy= DEFAULT_STRATEGY;
        }

        public Builder capacity(long capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder withStrategy(@Nonnull CacheStrategy strategy) {
            this.strategy = strategy;
            return this;
        }

        public MainCache<K, V> build() {
            return new MainCache<>(this);
        }
    }


}
