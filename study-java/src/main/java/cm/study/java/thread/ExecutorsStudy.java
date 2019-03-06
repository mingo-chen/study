package cm.study.java.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorsStudy {

    private ExecutorService exec = Executors.newFixedThreadPool(2);

    public String doSth(Runnable cmd) throws Exception {
        Future<String> rt = exec.submit(cmd, "Succ");
        return rt.get();
    }

    public static void main(String[] args) throws Exception {
        Runnable task = () -> {
            System.out.println("-------");
        };

        ExecutorsStudy study = new ExecutorsStudy();
        System.out.println("--> " + study.doSth(task));
    }
}
