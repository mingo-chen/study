package cm.study.java.core.concurrent;

import cm.study.java.core.utils.UnsafeKit;

import java.util.concurrent.TimeUnit;

public class StudyUnsafe {

    public void action1() {
        System.out.println("---> start action 1");
        UnsafeKit.getUnsafe().park(false, 0);
        System.out.println("---> complete action 1");
    }

    public void action2() {
        System.out.println("--> complete action 2");
    }

    public static void main(String[] args) {
        Thread current = Thread.currentThread();
        StudyUnsafe studyUnsafe = new StudyUnsafe();
        studyUnsafe.action2();

        new Thread(() -> {
            try {
                System.out.println("--> wait xxx");
                TimeUnit.SECONDS.sleep(5);
                UnsafeKit.getUnsafe().unpark(current);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        studyUnsafe.action1();
    }
}
