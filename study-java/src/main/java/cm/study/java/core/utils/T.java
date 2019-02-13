package cm.study.java.core.utils;

import java.util.concurrent.TimeUnit;

public class T {

    public static void sleep(TimeUnit unit, int duration) {
        try {
            unit.sleep(duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
