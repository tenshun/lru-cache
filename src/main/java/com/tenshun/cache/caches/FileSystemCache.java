package com.tenshun.cache.caches;

import org.apache.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class FileSystemCache<K extends Serializable, V extends Serializable> implements Cache<K, V> {

    private static final Logger LOGGER = Logger.getLogger(InMemoryCache.class);

    public static final String PATHNAME = "cache\\";
    private Map<K, String> metadataCache;
    public static final String TEMP_EXTENSION = ".temp";


    public FileSystemCache() {
        metadataCache = new HashMap<>();
        File tempFolder = new File(PATHNAME);
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }
    }

    @Override
    public void cache(@Nonnull K key, @Nonnull V value) {
        String pathToObject;
        pathToObject = PATHNAME + key.hashCode() + TEMP_EXTENSION;
        metadataCache.put(key, pathToObject);
        try (FileOutputStream fileStream = new FileOutputStream(pathToObject); //todo Refactor this code using DefaultMarshallerUnmarshaller
             ObjectOutputStream objectStream = new ObjectOutputStream(fileStream)) {
            objectStream.writeObject(value);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Nonnull
    @Override
    public Optional<V> get(@Nonnull K key) { //todo refactor
        if (metadataCache.containsKey(key)) {
            String pathToObject = metadataCache.get(key);
            try (FileInputStream fileStream = new FileInputStream(pathToObject);
                ObjectInputStream objectStream = new ObjectInputStream(fileStream)) {
                V deserializedObject = (V) objectStream.readObject();
                return Optional.of(deserializedObject);
            } catch (IOException | ClassNotFoundException e){
                LOGGER.error(e.getMessage(), e);
            }
        }
        return Optional.empty();
    }


    @Override
    public void clearCache() {
        for (K key : metadataCache.keySet()) {
            File deletingFile = new File(metadataCache.get(key));
            deletingFile.delete();
        }
        metadataCache.clear();
    }


    @Nonnull
    @Override
    public Optional<V> remove(@Nonnull K key) {
        if (metadataCache.containsKey(key)) {
            Optional<V> v = this.get(key);
            File deletingFile = new File(metadataCache.remove(key));
            deletingFile.delete();
            return v;
        }
        return Optional.empty();
    }


    @Override
    public boolean containsKey(@Nonnull K key) {
        return metadataCache.containsKey(key);
    }


    @Override
    public int size() {
        return metadataCache.size();
    }


}
