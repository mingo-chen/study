package cm.study.java.core.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据总线
 *
 * 记录每个核心里每个缓存的状态
 *
 * dataKey -> [core: dataStatus, core2: dataStatus]
 * dataKey -> [core: dataStatus, core2: dataStatus]
 */
public class DataBus implements RW {

    private static Logger ILOG = LoggerFactory.getLogger(DataBus.class);

    private RW memory;

    private List<CpuCore> cores;

    public DataBus(RW raw) {
        this.memory = raw;
        this.cores = new ArrayList<>();
    }

    public void addCore(CpuCore core) {
        this.cores.add(core);
    }

    /**
     * 总线检测指定缓存数据在core缓存里的状态
     */
    public boolean statusIsX(String key, CoreCache.CoreCacheStatus except) {
        for (CpuCore cpuCore : cores) {
            CoreCache.CoreCacheLine fromCache = cpuCore.cache.localRead(key);
            if (null != fromCache && fromCache.status == except) {
                return true;
            }
        }

        return false;
    }

    /**
     * 总线把指定缓存数据的core缓存状态从A改成B
     */
    public void statusA2B(String key, CoreCache.CoreCacheStatus A, CoreCache.CoreCacheStatus B) {
        for (CpuCore cpuCore : cores) {
            CoreCache.CoreCacheLine fromCache = cpuCore.cache.localRead(key);
            if (null != fromCache) {
                if (A == null || fromCache.status == A) {
                    ILOG.debug("change cpu core cache status, {} from {} to {}", cpuCore.getId(), A, B);
                    fromCache.status = B;
                    cpuCore.cache.localWrite(key, fromCache);
                }
            }
        }
    }

    /**
     * 总线把指定缓存数据从core缓存刷新回主存
     */
    public void flush(String key) {
        for (CpuCore cpuCore : cores) {
            CoreCache.CoreCacheLine fromCache = cpuCore.cache.localRead(key);
            if (null != fromCache && fromCache.status == CoreCache.CoreCacheStatus.Modify) {
                cpuCore.cache.remoteWrite(key);
                ILOG.debug("flush cpu core cache to ram, {} from {} to {}",
                        cpuCore.getId(), fromCache.status, CoreCache.CoreCacheStatus.Exclusive);
                fromCache.status = CoreCache.CoreCacheStatus.Exclusive; // 刷后主存后, 状态为独占
                cpuCore.cache.localWrite(key, fromCache);
            }
        }
    }

    @Override
    public Object load(String key) {
        return memory.load(key);
    }

    @Override
    public void store(String key, Object value) {
        memory.store(key, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (CpuCore cpuCore : cores) {
            sb.append(cpuCore.toString()).append("\n");
        }

        sb.append("---------------").append("\n");

        sb.append(memory.toString()).append("\n");

        return sb.toString();
    }

    public void debug() {
        System.out.println(this);
        System.out.println("==============\n");
    }
}
