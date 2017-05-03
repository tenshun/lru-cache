package com.tenshun.cache;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.EvictionListener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 03.05.2017.
 */

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {


        EvictionListener<String, String> listener = (key, value) -> System.out.println("Evicted key=" + key + ", value=" + value);
        ConcurrentLinkedHashMap<String, String> cache = new ConcurrentLinkedHashMap.Builder<String, String>()
                .maximumWeightedCapacity(3)
                .listener(listener)
                .build();



        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value1");


        String key1 = cache.get("key2");
        String key2 = cache.get("key2");
        String key3 = cache.get("key2");
        String key4 = cache.get("key2");

        Set<String> strings1 = cache.descendingKeySet();
        Set<String> strings = cache.ascendingKeySet();
        System.out.println(strings1);
        System.out.println(strings);

    }
}
