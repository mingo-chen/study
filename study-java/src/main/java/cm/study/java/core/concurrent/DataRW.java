package cm.study.java.core.concurrent;

import cm.study.java.core.utils.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class DataRW {

    private static Logger ILOG = LoggerFactory.getLogger(DataRW.class);

    private int value = 0;

    // 版本号, 每次修改value值, version都加自增1
    private volatile int version = 0;

    @Override
    protected void finalize() throws Throwable {
        ILOG.info("run here..");
    }

    public int add(int offset) {
        value += offset;
        version++;
        return value;
    }

    public int get() {
        return value;
    }

    public int getVer() {
        return version;
    }

    public static void main(String[] args) {
        DataRW rw = new DataRW();

        Thread write = new Thread(() -> {
            for (; ; ) {
                int value = rw.add(1);
//                System.out.println();
                ILOG.info("read value from local: {}, time: {}, {}", value, System.currentTimeMillis(), System.nanoTime());
                T.sleep(TimeUnit.SECONDS, 1);
            }
        });

        write.start();

        Thread read = new Thread(() -> {
            int lastVer = 0;
            for (; ; ) {
                if (lastVer != rw.getVer()) {
                    int value = rw.get();
                    lastVer = rw.getVer();
                    ILOG.info("read value from other: {}, time: {}, {}", value, System.currentTimeMillis(), System.nanoTime());
                }

            }
        });

        read.start();
    }
}
