package cm.study.java.core.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public class ReduceByAtom implements Reduce {

    private AtomicInteger value;

    public ReduceByAtom() {
        value = new AtomicInteger(0);
    }

    @Override
    public int increment() {
        return value.incrementAndGet();
    }

    @Override
    public int getV() {
        return value.get();
    }

    @Override
    public String toString() {
        return "ReduceStudy{" +
               "value=" + value +
               '}';
    }
}
