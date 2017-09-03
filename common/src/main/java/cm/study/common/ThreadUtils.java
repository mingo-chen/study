package cm.study.common;

/**
 * Created by chenming on 2017/9/3.
 */
public class ThreadUtils {

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }

}
