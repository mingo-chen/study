package cm.study.java.core.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 某个CPU里的某个核
 */
public class CpuCore implements RW {

    private static Logger ILOG = LoggerFactory.getLogger(CpuCore.class);

    public CoreCache cache;

    private String id;

    private Queue<Runnable> tasks;

    public CpuCore(String id, DataBus bus) {
        this.id = id;
        cache = new CoreCache(id, bus);
        tasks = new ArrayBlockingQueue<>(2);

        Thread bgTaskScheduler = new Thread(() -> {
            while (true) {
                synchronized (tasks) {
                    if (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                Runnable task = tasks.poll();
                new Thread(task).start();
            }

        });

        bgTaskScheduler.start();
    }

    @Override
    public Object load(String key) {
        ILOG.debug("cpu core {} load cache value, key: {}", id, key);
        Object result = cache.load(key);
        return result;
    }

    @Override
    public void store(String key, Object value) {
        ILOG.debug("cpu core {} store cache value, key: {}, value: {}", id, key, value);
        cache.store(key, value);
    }

    public void addTask(Runnable runnable) {
        synchronized (tasks) {
            tasks.add(runnable);
            tasks.notify();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CpuCore{" +
               "cache=" + cache +
               ", id='" + id + '\'' +
               '}';
    }
}
