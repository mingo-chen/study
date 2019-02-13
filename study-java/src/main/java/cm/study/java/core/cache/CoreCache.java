package cm.study.java.core.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 每个CPU核心缓存
 */
public class CoreCache implements RW {
    private static Logger ILOG = LoggerFactory.getLogger(CoreCache.class);

    private DataBus bus;

    private String coreId;

    private Map<String, CoreCacheLine> store = new HashMap<>();

    public CoreCache(String coreId, DataBus dataBus) {
        this.coreId = coreId;
        this.bus = dataBus;
    }

    @Override
    public Object load(String key) {
        CoreCacheLine cacheLine = localRead(key);

        if (cacheLine != null && cacheLine.status != CoreCacheStatus.Invalid) { // 本地存在且有效
            ILOG.debug("core cache fit, id: {}, key: {}, value: {}", coreId, key, cacheLine);
            return cacheLine.data;

        } else { // 本地不存在或失效
            Object remoteData = remoteRead(key);

            CoreCacheLine cacheStore = new CoreCacheLine();
            cacheStore.data = remoteData;

            synchronized (key) {
                if (bus.statusIsX(key, CoreCacheStatus.Modify)) { // 其它core对此有修改, 先让其它人把数据写入主存
                    bus.flush(key);
                    ILOG.debug("core cache is modified by other core, id: {}, key: {}, value: {}", coreId, key, cacheStore);
                    return load(key);

                } else if (bus.statusIsX(key, CoreCacheStatus.Exclusive)) { // 独占, 两人都改成Share
                    bus.statusA2B(key, CoreCacheStatus.Exclusive, CoreCacheStatus.Share);
                    cacheStore.status = CoreCacheStatus.Share;

                } else if (bus.statusIsX(key, CoreCacheStatus.Share)) {
                    cacheStore.status = CoreCacheStatus.Share;

                } else {
                    cacheStore.status = CoreCacheStatus.Exclusive;

                }
            }

            localWrite(key, cacheStore);
            ILOG.debug("core cache remote load, id: {}, key: {}, value: {}", coreId, key, cacheStore);
            return localRead(key);
        }

    }

    /**
     * 修改缓存里的数据
     * @param key
     * @param value
     */
    @Override
    public void store(String key, Object value) {
        CoreCacheLine cacheLine = new CoreCacheLine();
        cacheLine.status = CoreCacheStatus.Modify;

        /*
        为什么要把其它core的缓存状态都改为Invalid再写最新值?
        能否颠倒顺序?

        是为了保证其它core一定能读取本core写的最新值
         */
        synchronized (key) {
            bus.statusA2B(key, null, CoreCacheStatus.Invalid);
            cacheLine.data = value;
            localWrite(key, cacheLine);
        }

    }

    /**
     * core从缓存读
     * 缓存的效果通过命中率来评估
     */
    public CoreCacheLine localRead(String key) {
        return store.get(key);
    }

    /**
     * 缓存从主存读
     * 本地缓存对象没有或失败
     * 主存的读写速度远远低于CPU缓存, 影响CPU性能
     */
    public Object remoteRead(String key) {
        return bus.load(key);
    }

    /**
     * core往缓存写
     * 1: 其它core的缓存状态改为失效, 本身core的缓存状态改为修改
     * 2: cpu cache如果满了, 执行LRU算法
     */
    public void localWrite(String key, CoreCacheLine value) {
        store.put(key, value);
        ILOG.debug("core cache local write, core id: {}, key: {}, value: {}", coreId, key, value);
    }

    /**
     * 缓存往主存写
     * 本core修改了缓存数据,并且其它core有读取缓存的触发; 写完之后本缓存状态国独占; 当有其它core再次读取时, 双方都改成共享
     * 主存的读写速度远远低于CPU缓存, 影响CPU性能
     */
    public void remoteWrite(String key) {
        bus.store(key, store.get(key).data);
    }

    @Override
    public String toString() {
        return "CoreCache{" +
               ", coreId='" + coreId + '\'' +
               ", store=" + store +
               '}';
    }

    /**
     * 实际存储数据的类
     */
    public static class CoreCacheLine {
        public Object data;
        public volatile CoreCacheStatus status;

        @Override
        public String toString() {
            return "CacheLine{" +
                   "data=" + data +
                   ", status=" + status +
                   '}';
        }
    }

    /**
     * 当本core里的缓存数据变更, status-> modify
     * 当变更后的数据刷新后主存后, status-> exclusive
     * 当其它core把新
     */
    public enum CoreCacheStatus {
        /**
         * 描述: 该Cache line有效，数据被修改了，和内存中的数据不一致，数据只存在于本Cache中
         *
         * 监听任务: 缓存行必须时刻监听所有试图读该缓存行相对就主存的操作，
         * 这种操作必须在缓存将该缓存行写回主存并将状态变成S（共享）状态之前被延迟执行
         */
        Modify,

        /**
         * 描述: 该Cache line有效，数据和内存中的数据一致，数据只存在于本Cache中
         *
         * 监听任务: 缓存行也必须监听其它缓存读主存中该缓存行的操作，一旦有这种操作，该缓存行需要变成S（共享）状态
         */
        Exclusive,

        /**
         * 描述: 该Cache line有效，数据和内存中的数据一致，数据存在于很多Cache中
         *
         * 监听任务: 缓存行也必须监听其它缓存使该缓存行无效或者独享该缓存行的请求，并将该缓存行变成无效（Invalid）
         */
        Share,

        /**
         * 描述: 该Cache line无效
         *
         * 监听任务: 无
         */
        Invalid
        ;
    }


}
