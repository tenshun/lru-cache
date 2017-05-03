package com.tenshun.cache;

import java.io.IOException;
import java.util.Map;

public interface Cache<K, V> {

    V get(K key)  throws IOException;

    void cache(K key, V value) throws IOException;

    V remove(K key) throws IOException;

    int size();

    boolean containsKey(K key);

    void clearCache();

}
