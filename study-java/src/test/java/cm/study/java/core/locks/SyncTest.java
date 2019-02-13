package cm.study.java.core.locks;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import com.sun.tools.corba.se.idl.constExpr.Times;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SyncTest {

    private static Logger ILOG = LoggerFactory.getLogger(SyncTest.class);

    @Test
    public void testSayA() {
    }

    @Test
    public void testSayB() {
    }

    @Test
    public void testSayC() {
    }

    public static void main(String[] args) throws Exception {
        int stage = 1;
        int times = 20;
        long total = 0;

        int step = 50;

//        for(int x = 0; x < 1000; x++) {
//            long s1 = System.currentTimeMillis();
//            SyncTask syncStudy = getImplement(stage, times);
//            if(!test(syncStudy)) {
//                break;
//            }
//            long s2 = System.currentTimeMillis();
//
//            total += (s2 - s1);
//
//            if (x % step == 0) {
//                ILOG.info("process: {}, avg: {} ms", x, (total / step));
//                total = 0;
//            }
//        }
//
//        ILOG.info("process: {}, avg: {} ms", 1000, (total / step));

        SyncTask syncStudy = getImplement(stage, times);
        long s1 = System.currentTimeMillis();
        test(syncStudy);
        long s2 = System.currentTimeMillis();

        total += (s2 - s1);
        ILOG.info("process: {}, avg: {} ms", 1, total);
    }

    public static SyncTask getImplement(int stage, int times) {
        if(stage == 1) {
            return new SyncStudy1(times);
        } else if (stage == 2) {
            return new SyncStudy2(times);
        } else if (stage == 3) {
            return new SyncStudy3(times);
        } else {
            return null;
        }

    }

    public static boolean test(SyncTask syncTask) throws Exception {
        Thread t1 = new Thread(() -> syncTask.sayA());
        t1.start();

//        TimeUnit.SECONDS.sleep(1);
//        T.sleep(TimeUnit.MILLISECONDS, 100);

        Thread t2 = new Thread(() -> syncTask.sayB());
        t2.start();

        Thread t3 = new Thread(() -> syncTask.sayC());
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        boolean result = true;
        String flag1 = syncTask.getOutput().get(0);
        String flag2 = syncTask.getOutput().get(1);
        String flag3 = syncTask.getOutput().get(2);

        for (int i = 3; i < syncTask.getOutput().size(); i++) {
            if(i % 3 == 0) {
                if(!syncTask.getOutput().get(i).equals(flag1)) {
                    result = false;
                    break;
                }

            } else if (i % 3 == 1) {
                if(!syncTask.getOutput().get(i).equals(flag2)) {
                    result = false;
                    break;
                }

            } else {
                if(!syncTask.getOutput().get(i).equals(flag3)) {
                    result = false;
                    break;
                }

            }

        }

        ILOG.debug("output: {}, result: {}", syncTask.getOutput(), result);

        if (result != true) {
            ILOG.info("output: {}", syncTask.getOutput());
        }

        return result;
    }
}