

### Purpose of the repository


>"Create a configurable two-level cache (for caching Objects). Level 1 is memory, level 2 is filesystem. Config params should let one specify the cache strategies and max sizes of level 1 and 2"

#### Special for Wiley.

## Theory:
JVM Reference types

#### Strong Reference Object

```java
StringBuilder builder = new StringBuilder();
```
#### Weak Reference Object

```java
WeakReference<StringBuilder> weakBuilder = new WeakReference<StringBuilder>(builder);
```

#### Levels of Weakness

>Two different levels of weakness can be enlisted: **soft** and **phantom**.

>A soft Reference Object is basically a weak Reference Object that remains in memory a bit more: normally, it resists GC cycle until memory is available and there is no risk of OutOfMemoryError (in that case, it can be removed).

>On the other hand, a phantom Reference Object is useful only to know exactly when an object has been effectively removed from memory: normally they are used to fix weird finalize() revival/resurrection behavior, since they actually do not return the object itself but only help in keeping track of their memory presence.

![alt text](http://2.bp.blogspot.com/-dponJrixU9Y/UzGXiXveSJI/AAAAAAAABVo/Lc3-d8ZsI2g/s1600/Weak+Strong+Soft+and+Phantom+Reference+in+Java.gif)

LRU (least recently used) cache

![alt text](http://2.bp.blogspot.com/-9emrB3ylgzE/Tyg-MAfvsEI/AAAAAAAAAzU/qJk63KHp5Xw/s1600/LinkedHashMap.png)
## Terminology
### Cache
Wiktionary defines a cache as a store of things that will be required in the future, and can be retrieved rapidly. A cache is a collection of temporary data that either duplicates data located elsewhere or is the result of a computation. Data that is already in the cache can be repeatedly accessed with minimal costs in terms of time and resources.

### Cache Entry
A cache entry consists of a key and its mapped data value within the cache.

### Cache Hit
When a data entry is requested from cache and the entry exists for the given key, it is referred to as a cache hit (or simply, a hit).

### Cache Miss
When a data entry is requested from cache and the entry does not exist for the given key, it is referred to as a cache miss (or simply, a miss).

### System-of-Record (SoR)
The authoritative source of truth for the data. The cache acts as a local copy of data retrieved from or stored to the system-of-record (SOR). The SOR is often a traditional database, although it might be a specialized file system or some other reliable long-term storage. It can also be a conceptual component such as an expensive computation.

### Eviction
The removal of entries from the cache in order to make room for newer entries (typically when the cache has run out of data storage capacity).

### Expiration
The removal of entries from the cache after some amount of time has passed, typically as a strategy to avoid stale data in the cache

### Hot Data
Data that has recently been used by an application is very likely to be accessed again soon. Such data is considered hot. A cache may attempt to keep the hottest data most quickly available, while attemping to choose the least hot data for eviction

## Solutions

#### Solution 1: HashMap

Doubly Linked List is used to store list of items with most recently used item at the start of the list. So, as more items are added to the list, least recently used times are moved to the end of the list with item at tail being the least recently used item in the list.

```TrivialCache.java```
```java

public class TrivialCache<K, V> implements Cache<K, V> {

    private static final int DEFAULT_CACHE_SIZE = 100;

    private Map<K, V> cache;
    private DoublyLinkedList<K> list;
    private int cacheSize;
}
```


#### Solution 2: WeakHashMap
Java Docs:
>Hash table based implementation of the Map interface, with weak keys. An entry in a WeakHashMap will automatically be removed when its key is no longer in ordinary use.
More precisely, the presence of a mapping for a given key will not prevent the key from being discarded by the garbage collector, that is, made finalizable, finalized, and then reclaimed.
When a key has been discarded its entry is effectively removed from the map, so this class behaves somewhat differently from other Map implementations.

>Like most collection classes, this class is not synchronized. A synchronized WeakHashMap may be constructed using the Collections.synchronizedMap method.

Usage example:
```java
Map<K, V> cache = Collections.synchronizedMap(new WeakHashMap<K, V>());
```

##### Update:
http://stackoverflow.com/questions/1802809/javas-weakhashmap-and-caching-why-is-it-referencing-the-keys-not-the-values
> WeakHashMap isn't useful as a cache, at least the way most people think of it. As you say, it uses weak keys, not weak values,
> so it's not designed for what most people want to use it for (and, in fact, I've seen people use it for, incorrectly).
>WeakHashMap is mostly useful to keep metadata about objects whose lifecycle you don't control.

> For example, if you have a bunch of objects passing through your class, and you want to keep track of extra data about them without needing to be notified when they go out of scope, and without your reference to them keeping them alive.

>Elements in a weak hashmap can be reclaimed by the garbage collector if there are no other strong references to the key object, this makes them useful for caches/lookup storage.

###### Effective Java, edition 2, page 26
>Another common source of memory leaks is caches. Once you put an object reference into a cache, it’s easy to forget that it’s there and leave it in the cache long after it becomes irrelevant.
>There are several solutions to this problem. If you’re lucky enough to implement a cache for which an entry is relevant exactly so long as there are references to its key outside of the cache, represent the cache as a WeakHashMap;
>entries will be removed automatically after they become obsolete.
>Remember that WeakHashMap is useful only if the desired lifetime of cache entries is determined by external references to the key, not the value.


#### Solution 3: LinkedHashMap
```FiniteLinkedHashMap.java```

```java
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
```

#### Solution 4: ConcurrentHashMap
The ConcurrentHashMap in Java 8 has got a nice addition, the computeIfAbsent method. It takes two arguments: the key value and a function to calculate the value if not present in the cache
##### Definition
```java
computeIfAbsent(K key, Function<? super K,? extends V> mappingFunction)
```
>If the specified key is not already associated with a value (or is mapped to null), attempts to compute its value using the given mapping function and enters it into this map unless null.

Implementation of cache using ConcurrentHashMap very similar to common HashMap

Example:
```java
ConcurrentMap<Key, Graph> cache = new ConcurrentHashMap<>();

static {
   cache.put(0,0L); //fibonacci(0)
   cache.put(1,1L); //fibonacci(1)
}
public static long fibonacci(int x) {
   return cache.computeIfAbsent(x, n -> fibonacci(n-2) + fibonacci(n-1));
}
```



#### And finally Solution 5: ConcurrentLinkedHashMap
A high performance version of java.util.LinkedHashMap for use as a software cache.
https://github.com/ben-manes/concurrentlinkedhashmap

> In this benchmark an unbounded ConcurrentHashMap is compared to a ConcurrentLinkedHashMap v1.0 with a maximum size of 5,000 entries under an artificially high load (250 threads, 4-cores).

![alt text](https://raw.githubusercontent.com/ben-manes/concurrentlinkedhashmap/wiki/images/performance/get.png)
![alt text](https://raw.githubusercontent.com/ben-manes/concurrentlinkedhashmap/wiki/images/performance/put.png)

#### Solution 6: We might also use Google Guava Cache
##### Features:
###### Size-based Eviction
``` java
CacheBuilder.maximumSize(long)
```
###### Timed Eviction
``` java
expireAfterAccess(long, TimeUnit)
expireAfterWrite(long, TimeUnit)
```
###### Reference-based Eviction
``` java
CacheBuilder.weakKeys()
CacheBuilder.weakValues()
CacheBuilder.softValues()
```
###### Explicit Removals
###### Removal Listeners



##### Usage:
```java
Cache<String, String> cache = new CacheBuilder.newBuilder()
        .maximumSize(1000)
        .expireAfterWrite(10, TimeUnit.MINUTES).build();

cache.put("key", "value");

LoadingCache<Key, Graph> graphs = CacheBuilder.newBuilder()
       .maximumSize(1000)
       .expireAfterWrite(10, TimeUnit.MINUTES)
       .removalListener(MY_LISTENER)
       .build(
           new CacheLoader<Key, Graph>() {
             public Graph load(Key key) throws AnyException {
               return createExpensiveGraph(key);
             }
           });
```


#### Solution 7: Caffeine
Caffeine is a high performance, near optimal caching library based on Java 8. For more details, see our user's guide and browse the API docs for the latest release.
https://github.com/ben-manes/caffeine

##### Features
* automatic loading of entries into the cache, optionally asynchronously
* size-based eviction when a maximum is exceeded based on frequency and recency
* time-based expiration of entries, measured since last access or last write
* asynchronously refresh when the first stale request for an entry occurs
* keys automatically wrapped in weak references
* values automatically wrapped in weak or soft references
* notification of evicted (or otherwise removed) entries
* writes propagated to an external resource
* accumulation of cache access statistics



### Custom implementation using Solution 5 (ConcurrentLinkedHashMap)

```java

public interface Cache<K, V> {

    @Nonnull
    Optional<V> get(@Nonnull K key);

    void cache(@Nonnull K key, @Nonnull V value);

    @Nonnull
    Optional<V> remove(@Nonnull K key);

    int size();

    boolean containsKey(@Nonnull K key);

    void clearCache();

}
```

#### Configuration

Currently we have two strategies: ONLY_IN_MEMORY_CACHE and TWO_LEVEL_CACHE

Default strategy: ONLY_IN_MEMORY_CACHE



##### Usage
##### Default strategy:

```java

Cache<String, String> cache = new MainCache.Builder<>()
                                    .capacity(100)
                                    .build();
```

##### Two level cache strategy:

```java

Cache<String, String> cache = new MainCache.Builder<>()
                                    .withStrategy(CacheStrategy.TWO_LEVEL_CACHE)
                                    .capacity(100)
                                    .build();
```


##### Two level cache internal configuration using EvictionListener:

```java
fileSystemCache = new FileSystemCache<>();
inMemoryCache = new InMemoryCache<>(builder.capacity, (key, value) -> {
                    fileSystemCache.cache(key, value); //save object to second level cache after eviction
                });
```


**Взаимодействие между кэшами:** Если элемент удален при переполнении кэша первого уровня, то он перемещается во второй уровент кэша. Это позволяет получать доступ к менее используемым элементам, тогда как наиболее используемые всегда будут оставаться в памяти.


#### How to run


```bash
$ gradle bootRun
```



#### Useful links
https://docs.oracle.com/javase/8/docs/api/java/util/Map.html

http://www.javaspecialist.ru/2012/02/java-lru-cache.html

http://www.vldb.org/conf/1994/P439.PDF