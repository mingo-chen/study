package cm.study.java.assembly.redis;

import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.TreeSet;

import static org.testng.Assert.*;

public class CacheStoreTest {

    @Test
    public void testTreeSet() {
        TreeSet<CacheStore.Tuple> strings = new TreeSet<>();
        strings.add(new CacheStore.Tuple("cm", 10));
        strings.add(new CacheStore.Tuple("mz", 11));
        strings.add(new CacheStore.Tuple("abc", 8));

        System.out.println("--> " + strings);
    }

    @Test
    public void testMapSortByValue() {
        LinkedHashMap<String, Long> sortedMap = new LinkedHashMap<>();

    }

}