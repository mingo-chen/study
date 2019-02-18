package cm.study.java.core.locks;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 多线程同步, 采用Synchronized
 */
public class SyncStudy1 implements SyncTask {

    private static Logger ILOG = LoggerFactory.getLogger(SyncStudy1.class);

    private List<String> output = new ArrayList<>();
    private int times;

    public String signal = "A";

    public SyncStudy1(int times) {
        this.times = times;
    }

    public void sayA() {
        String jobId = "A";
        String nextJobId = "B";

        say(jobId, nextJobId);

//        synchronized (this) {
//            this.notifyAll();
//        }

    }

    public void sayB() {
        String jobId = "B";
        String nextJobId = "C";

        say(jobId, nextJobId);

//        synchronized (this) {
//            this.notifyAll();
//        }

    }

    public void sayC() {
        String jobId = "C";
        String nextJobId = "A";

        say(jobId, nextJobId);

//        synchronized (this) {
//            this.notifyAll();
//        }

    }

    @Override
    public List<String> getOutput() {
        return output;
    }

    void say(String jobId, String nextJobId) {
        for(int n = 0; n < times; ) {
            synchronized (this) {
                ILOG.debug("{} search lock, signal: {}, is match: {}", jobId, signal, StringUtils.equals(signal, jobId));
                if (StringUtils.equals(signal, jobId)) {
                    output.add(jobId);
                    this.signal = nextJobId;
                    n++;
                    ILOG.debug("complete {} job and notify other start work, index: {}, value: {}", jobId, n, jobId);
                    this.notifyAll();

                } else {
                    try {
                        this.wait();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        }
    }

}
