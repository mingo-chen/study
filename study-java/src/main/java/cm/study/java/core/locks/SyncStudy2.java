package cm.study.java.core.locks;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程同步, 采用Synchronized
 */
public class SyncStudy2 implements SyncTask {

    private static Logger ILOG = LoggerFactory.getLogger(SyncStudy2.class);

    private List<String> output = new ArrayList<>();
    private int times;

    // true => A
    // false => B
    private ReentrantLock lock = new ReentrantLock();

    public String signal = "A";

    private Condition RoundA = lock.newCondition();
    private Condition RoundB = lock.newCondition();
    private Condition RoundC = lock.newCondition();

    public SyncStudy2(int times) {
        this.times = times;
    }

    public void sayA() {
        say(times, "A", "B", RoundA, RoundB);
    }

    public void sayB() {
        say(times, "B", "C", RoundB, RoundC);
    }

    public void sayC() {
        say(times,"C", "A", RoundC, RoundA);
    }

    void say(int times, String jobId, String nextJobId, Condition current, Condition next) {
        for(int n = 0; n < times; ) {
            lock.lock();
            try {
                ILOG.debug("{} get lock, signal: {}, is match: {}", jobId, signal, StringUtils.equals(signal, jobId));
                if(StringUtils.equals(signal, jobId)) {
//                System.out.printf("[%d]:%s\n", n, "C");
                    output.add(jobId);
                    signal = nextJobId;
                    n++;
                    ILOG.debug("complete {} job and notify other start work, index: {}, value: {}", jobId, n, jobId);
                    next.signal();

                } else {
                    try {
                        current.await(10, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } finally {
                lock.unlock();
            }
        }

        lock.lock();
        try {
            next.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<String> getOutput() {
        return output;
    }

}
