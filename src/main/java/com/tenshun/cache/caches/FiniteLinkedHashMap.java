package com.tenshun.cache.caches;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Not thread safe
 */
public class FiniteLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 1L;
    private final int maximumCapacity;

    /**
     * @true for access-order,
     * @false for insertion-order
     */

    public enum AccessOrder {
        FIFO(false), LRU(true);

        final boolean accessOrder;
        private AccessOrder(boolean accessOrder) {
            this.accessOrder = accessOrder;
        }
        boolean get() {
            return accessOrder;
        }
    }


    public FiniteLinkedHashMap(AccessOrder accessOrder, int initialCapacity, int maximumCapacity) {
        super(initialCapacity, 0.75f, accessOrder.get());
        this.maximumCapacity = maximumCapacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maximumCapacity;
    }
}
