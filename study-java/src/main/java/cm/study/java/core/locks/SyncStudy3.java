package cm.study.java.core.locks;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SyncStudy3 implements SyncTask {

    private static Logger ILOG = LoggerFactory.getLogger(SyncStudy3.class);

    private List<String> output = new ArrayList<>();
    private int times;

    private AtomicReference<String> signalRef = new AtomicReference<>("A");

    public SyncStudy3(int times) {
        this.times = times;
    }

    @Override
    public void sayA() {
        String jobId = "A";
        String nextJobId = "B";

        say(jobId, nextJobId);
    }

    @Override
    public void sayB() {
        String jobId = "B";
        String nextJobId = "C";

        say(jobId, nextJobId);
    }

    @Override
    public void sayC() {
        String jobId = "C";
        String nextJobId = "A";

        say(jobId, nextJobId);
    }

    void say(String jobId, String nextJobId) {
        for(int n = 0; n < times; ) {
            String signal = signalRef.get();
            ILOG.debug("{} search lock, signal: {}, is match: {}", jobId, signal, StringUtils.equals(signal, jobId));
            if (StringUtils.equals(signal, jobId)) {
                output.add(jobId);
                signalRef.compareAndSet(jobId, nextJobId);
                n++;
                ILOG.debug("complete {} job and notify other start work, index: {}, value: {}", jobId, n, jobId);

            }

        }
    }

    @Override
    public List<String> getOutput() {
        return output;
    }
}
