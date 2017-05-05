package com.tenshun.cache;

import com.tenshun.cache.caches.Cache;
import com.tenshun.cache.caches.CacheStrategy;
import com.tenshun.cache.caches.MainCache;
import com.tenshun.cache.generators.ScrambledZipfianGenerator;
import com.tenshun.cache.caches.FiniteLinkedHashMap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

        ScrambledZipfianGenerator generator = new ScrambledZipfianGenerator(100000);

        Cache<Integer, Integer> cache = new MainCache.Builder<>()
                .withStrategy(CacheStrategy.TWO_LEVEL_CACHE)
                .capacity(100)
                .build();

        for(int i = 0; i < 200; i++){
            cache.cache(generator.nextInt(), generator.nextInt());
        }

        cache.clearCache(); //expected empty cache folder


    }
}
