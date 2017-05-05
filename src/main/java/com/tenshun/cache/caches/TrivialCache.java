package com.tenshun.cache.caches;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Trivial hashMap based implementation of cache
 *
 */
public class TrivialCache<K, V> implements Cache<K, V> {

    private static final int DEFAULT_CACHE_SIZE = 100;

    private Map<K, V> cache;
    private DoublyLinkedList<K> doublyLinkedList;
    private final int cacheSize;


    public TrivialCache() {
        this.cacheSize = DEFAULT_CACHE_SIZE;
        this.cache = new HashMap<>();
        this.doublyLinkedList = new DoublyLinkedList<>();
    }

    public TrivialCache(int cacheSize) {
        this.cacheSize = cacheSize;
        this.cache = new HashMap<>();
        this.doublyLinkedList = new DoublyLinkedList<>();
    }

    @Nonnull
    @Override
    public Optional<V> get(@Nonnull K key) {
        //todo check if hashmap contains key
        //todo if so move linked list node to the head of linked list
        //todo get item from hashmap by key and convert to Optional.of(item)
        //todo return optional
        return Optional.empty();
    }

    @Override
    public void cache(@Nonnull K key, @Nonnull V value) {

        //todo 1) check hashmap size
        //todo 2) if size == cacheSize - get least used element from the list(from tail),
        //todo get key of that element, remove element from hashmap using the key,
        //todo delete node from list
        //todo add current k,v to the hashmap
        //todo move key of element to head
    }

    @Nonnull
    @Override
    public Optional<V> remove(@Nonnull K key) {
        //todo remove from hashmap
        //todo remove from linkedlist
        return Optional.empty();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean containsKey(@Nonnull K key) {
        return false;
    }

    @Override
    public void clearCache() {
        cache.clear();
        //todo implement doublyLinkedList.clear()
    }
}
