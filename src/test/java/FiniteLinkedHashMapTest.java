import com.tenshun.cache.caches.FiniteLinkedHashMap;
import com.tenshun.cache.generators.ScrambledZipfianGenerator;
import org.junit.Assert;
import org.junit.Test;

/**
 * 05.05.2017.
 */
public class FiniteLinkedHashMapTest {

    @Test
    public void putTest(){

        ScrambledZipfianGenerator generator = new ScrambledZipfianGenerator(100000);
        FiniteLinkedHashMap<Integer, String> finiteLinkedHashMap = new FiniteLinkedHashMap<>(FiniteLinkedHashMap.AccessOrder.LRU, 100, 100);

        for(int i = 0; i < 1000; i++){
            finiteLinkedHashMap.put(generator.nextInt(), generator.nextString());
        }

        Assert.assertNotEquals(1000, finiteLinkedHashMap.size());

        /*finiteLinkedHashMap.forEach((key, value) -> System.out.println(key + ":" + value));*/
    }
}
