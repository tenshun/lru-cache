## Theory:

### Cache
Wiktionary defines a cache as a store of things that will be required in the future, and can be retrieved rapidly. A cache is a collection of temporary data that either duplicates data located elsewhere or is the result of a computation. Data that is already in the cache can be repeatedly accessed with minimal costs in terms of time and resources.

### Expiration
The removal of entries from the cache after some amount of time has passed, typically as a strategy to avoid stale data in the cache

## Solutions
#### One possible solution is to use WeakHashMap

Update:
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

#### Third solution might be
In Java 8 new method in Map: computeIfAbsent
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

#### Custom implementation
In summary





```/gradlew```

#### Useful links
https://docs.oracle.com/javase/8/docs/api/java/util/Map.html