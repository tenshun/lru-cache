import com.tenshun.cache.caches.FileSystemCache;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * 04.05.2017.
 */
public class FileSystemCacheTest {

    @Test
    public void numberOfFilesInDirTest(){
        FileSystemCache<String, String> cache = new FileSystemCache<>();
        cache.cache("key1", "value1");
        cache.cache("key2", "value2");
        cache.cache("key3", "value3");
        cache.cache("key4", "value4");

        File dir = new File(FileSystemCache.PATHNAME);
        int beforeClear = dir.listFiles().length;
        Assert.assertEquals(beforeClear, cache.size());

        cache.clearCache();
        int afterClear = dir.listFiles().length;
        Assert.assertEquals(afterClear, cache.size());
    }

    @Test
    public void checkValueFromFileAfterWriting(){

    }
}
