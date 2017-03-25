package cm.study.rxjava.eds;

/**
 * Created by chenming on 2017/2/27.
 */
public class EdsUtils {

    public static void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
