package com.tenshun.cache.utils;

import com.sun.istack.internal.NotNull;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.Serializable;
import java.util.Map;


public class DefaultMarshallerUnmarshaller<K extends Serializable, V extends Serializable> implements MarshallerUnmarshaller<K, V> {


    @Override
    public Map.Entry<K, V> unmarshall(@NotNull File file) {
        return null;
    }

    @Override
    public void marshal(@Nonnull K key, @NotNull V value, @NotNull File file) {

    }
}
