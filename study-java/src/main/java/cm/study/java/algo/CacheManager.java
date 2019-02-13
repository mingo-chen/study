package cm.study.java.algo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * 缓存管理器
 * 当缓存空间用完后使用LRU(Least Recently Used)算法来淘汰缓存中的对象
 */
public class CacheManager {

    private static Logger ILOG = LoggerFactory.getLogger(CacheManager.class);

    /**
     * 缓存容量
     */
    private static int capacity;

    /**
     * 当数据不存在时, 是否要异步加载还是同步
     */
    private static boolean async;

    private static CacheManager instance;

    public static void create(int capacity, boolean async) {
        CacheManager.capacity = capacity;
        CacheManager.async = async;
    }

    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if(instance == null) {
                    instance = new CacheManager();
                }
            }
        }

        return instance;
    }

    /**
     * 缓存存储源
     */
    private LinkedHashMap<String, Object> store = new LinkedHashMap(16,  0.75F,false);

    /**
     * 计算缓存命中率
     */
    private AtomicInteger fail = new AtomicInteger(0);

    /**
     * 计算缓存命中率
     */
    private AtomicInteger success = new AtomicInteger(0);

    private AtomicInteger size = new AtomicInteger(0);

    public void put(String key, Object value) {
        if(size.get() + 1 > capacity) { // 容量已达上限, LRU淘汰
            synchronized (store) {
                Map.Entry<String, Object> eliminateEntry = store.entrySet().iterator().next();

                if (eliminateEntry != null) {
                    Object unused = store.remove(eliminateEntry.getKey());
                    ILOG.info("lru eliminate key: {}, value: {}", eliminateEntry.getKey(), unused);

                }

                size.decrementAndGet();
            }
        }

        store.put(key, value);
        size.incrementAndGet();
    }

    public Object get(String key, Function<String, Object> loadWhenNotFit) {
        Object value = store.get(key);
        if (value == null) {
            Object fromRemote = loadWhenNotFit.apply(key);
            put(key, fromRemote);
            fail.incrementAndGet();
            ILOG.info("load value from remote, key: {}, value: {}", key, fromRemote);
            return fromRemote;

        } else {
            success.incrementAndGet();
            return value;
        }

    }

    public void debug() {
        System.out.println("=========================");
        System.out.println("store  size: " + store.size());
        System.out.println("cache  size: " + size);
        System.out.println("succs times: " + success.get());
        System.out.println("total times: " + (success.get() + fail.get()));
        System.out.println("fit    rate: " + success.get() * 100.0 / (success.get() + fail.get()));
        System.out.println("store      : " + store);
        System.out.println("=========================");
    }

}
