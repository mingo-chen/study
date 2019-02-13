package cm.study.java.algo;

import cm.study.java.core.utils.T;
import org.testng.annotations.Test;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class CacheManagerTest {

    @Test
    public void testTreeMap() {
        TreeMap<String, Object> treeMap = new TreeMap<>();

        treeMap.put("height", 170);
        treeMap.put("age", 31);
        treeMap.put("name", "cm");
        System.out.println("--> " + treeMap);
    }

    public static void main(String[] args) {
        CacheManager.create(2, false);

        Function<String, Object> loadWhenNotFit = (s) -> s.toUpperCase();

        CacheManager.getInstance().get("cm", loadWhenNotFit);

//        T.sleep(TimeUnit.MILLISECONDS, 100);
        Object value = CacheManager.getInstance().get("name", loadWhenNotFit);
        System.out.println("###: " + value);

        value = CacheManager.getInstance().get("name", loadWhenNotFit);
        System.out.println("###: " + value);

        T.sleep(TimeUnit.MILLISECONDS, 100);
        CacheManager.getInstance().get("ljx", loadWhenNotFit);

        CacheManager.getInstance().debug();
    }
}