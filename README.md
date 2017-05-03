## Theory:
JVM Reference types

![alt text](http://2.bp.blogspot.com/-dponJrixU9Y/UzGXiXveSJI/AAAAAAAABVo/Lc3-d8ZsI2g/s1600/Weak+Strong+Soft+and+Phantom+Reference+in+Java.gif)

LRU (least recently used) CACHE

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
#### One possible solution is to use WeakHashMap
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


#### Second possible solution is to use Google Guava  CacheBuilder
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

#### Third solution might be ConcurrentHashMap
The ConcurrentHashMap in Java 8 has got a nice addition, the computeIfAbsent method. It takes two arguments: the key value and a function to calculate the value if not present in the cache
##### Definition
```java
computeIfAbsent(K key, Function<? super K,? extends V> mappingFunction)
```
>If the specified key is not already associated with a value (or is mapped to null), attempts to compute its value using the given mapping function and enters it into this map unless null.

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

#### Fourth solution
```java
public class LRUCache extends LinkedHashMap<integer, string=""> {
	private int cacheSize;

	public LRUCache(int size) {
		super(size, 0.75f, true);
		this.cacheSize = size;
	}

	@Override
	protected boolean removeEldestEntry(
			java.util.Map.Entry<integer, string=""> eldest) {

		// remove the oldest element when size limit is reached
		return size() > cacheSize;
	}
}
```

#### Fifth solution
A high performance version of java.util.LinkedHashMap for use as a software cache.
https://github.com/ben-manes/concurrentlinkedhashmap
#### Sixth solution
Caffeine is a high performance, near optimal caching library based on Java 8. For more details, see our user's guide and browse the API docs for the latest release.
https://github.com/ben-manes/caffeine
#### Custom implementation
In summary





```/gradlew```

#### Useful links
https://docs.oracle.com/javase/8/docs/api/java/util/Map.html
http://www.javaspecialist.ru/2012/02/java-lru-cache.html
http://www.vldb.org/conf/1994/P439.PDF