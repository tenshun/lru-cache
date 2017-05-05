package com.tenshun.cache.utils;

import com.sun.istack.internal.NotNull;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.Serializable;
import java.util.Map;


public interface MarshallerUnmarshaller<K extends Serializable, V extends Serializable> {

    Map.Entry<K, V> unmarshall(@NotNull File file);

    void marshal(@Nonnull K key, @NotNull V value, @NotNull File file);

}