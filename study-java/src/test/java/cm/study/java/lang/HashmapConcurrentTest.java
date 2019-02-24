package cm.study.java.lang;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class HashmapConcurrentTest extends Thread {

    private static Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    private static AtomicInteger at = new AtomicInteger(0);

    @Override
    public void run() {
        while(at.get()<1000000){
            map.put(at.get(), at.get());
            int count = at.incrementAndGet();
            if (count % 10000 == 0) {
                System.out.println("------ " + count);
            }
        }
    }

    public static void main(String[] args) {
        for(int i = 0; i < 10; i++) {
            HashmapConcurrentTest test = new HashmapConcurrentTest();
            test.start();
        }
    }
}
