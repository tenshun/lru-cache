package com.tenshun.cache;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class FileSystemCache<K extends Serializable, V extends Serializable> implements Cache<K, V> {

    public static final String PATHNAME = "temp\\";
    private Map<K, String> fileMetadataCache;

    //todo params


    public FileSystemCache() {
        fileMetadataCache = new HashMap<>();

        File tempFolder = new File(PATHNAME);
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }
    }

    @Override
    public void cache(K key, V value) throws IOException {
        String pathToObject;
        pathToObject = PATHNAME + UUID.randomUUID().toString() + ".temp";

        fileMetadataCache.put(key, pathToObject);

        FileOutputStream fileStream = new FileOutputStream(pathToObject);
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);

        objectStream.writeObject(value);
        objectStream.flush();
        objectStream.close();
        fileStream.flush();
        fileStream.close();
    }


    @Override
    public V get(K key) throws IOException { //todo refactor
        if (fileMetadataCache.containsKey(key)) {

            String pathToObject = fileMetadataCache.get(key);


            try {
                FileInputStream fileStream = new FileInputStream(pathToObject);
                ObjectInputStream objectStream = new ObjectInputStream(fileStream);


                V deserializedObject = (V) objectStream.readObject();

                fileStream.close();
                objectStream.close();

                return deserializedObject;
            } catch (IOException | ClassNotFoundException ex) {
                return null;
            }
        }

        return null;
    }


    @Override
    public void clearCache() {
        for (K key : fileMetadataCache.keySet()) {
            File deletingFile = new File(fileMetadataCache.get(key));
            deletingFile.delete();
        }

        fileMetadataCache.clear();

    }


    @Override
    public V remove(K key) throws IOException {
        if (fileMetadataCache.containsKey(key)) {
            V result = this.get(key);
            File deletingFile = new File(fileMetadataCache.remove(key));
            deletingFile.delete();
            return result;
        }
        return null;
    }


    @Override
    public boolean containsKey(K key) {
        return fileMetadataCache.containsKey(key);
    }


    @Override
    public int size() {
        return fileMetadataCache.size();
    }


}
