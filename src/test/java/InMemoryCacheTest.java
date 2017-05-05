import com.tenshun.cache.caches.Cache;
import com.tenshun.cache.caches.InMemoryCache;
import org.junit.Assert;
import org.junit.Test;


public class InMemoryCacheTest {

    @Test
    public void evictionListenerTest() {

        final String[][] deleted = new String[0][1];
        InMemoryCache<String, String> cache = new InMemoryCache<>(3, ((key, value) -> {
            deleted[0][0] = key;
            deleted[0][1] = value;
        }));

        cache.cache("key1", "value1");
        cache.cache("key2", "value2");
        cache.cache("key3", "value3");
        cache.cache("key4", "value4");

        Assert.assertEquals(deleted[0][0], "key1");
        Assert.assertEquals(deleted[0][1], "value1");
    }

    @Test
    public void maxCapacityTest(){
        int maxCapacity = 3;
        Cache<String, String> cache = new InMemoryCache<>(maxCapacity);

        cache.cache("key1", "value1"); //must be deleted after last insertion
        cache.cache("key2", "value2");
        cache.cache("key3", "value3");
        cache.cache("key4", "value4");

        Assert.assertEquals(cache.size(), maxCapacity);

    }


}
