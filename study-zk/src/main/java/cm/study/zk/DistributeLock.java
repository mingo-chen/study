package cm.study.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class DistributeLock {
    private static Logger ILOG = LoggerFactory.getLogger(DistributeLock.class);

    private String zkPath;

    private CuratorFramework client;

    public DistributeLock(String zkPath) {
        this.zkPath = zkPath;

        init();
    }

    void init() {
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(zkPath, policy);
        client.start();
    }

    public <Result, Input> Result concurrentRun(String bizId, Function<Input, Result> function, Input args) {
        InterProcessMutex lock = new InterProcessMutex(client, bizId);
        try {
            if (lock.acquire(1, TimeUnit.SECONDS)) {
                // 获取到分布式锁
                return function.apply(args);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
//                lock.release();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
        // 没抢到锁就算了, 如果事务只能执行一次
        return null;
    }

    public static void main(String[] args) throws Exception {
        DistributeLock lock = new DistributeLock("localhost:2181");

        Function<Void, String> biz = new Function<Void, String>() {
            @Override
            public String apply(Void aVoid) {
                ILOG.info("distribute env run start...");
                return "SUCCESS";
            }
        };

        String result = lock.concurrentRun("/just/for/test", biz, null);
        System.out.println("--> " + result);

        System.in.read(); //
    }
}
