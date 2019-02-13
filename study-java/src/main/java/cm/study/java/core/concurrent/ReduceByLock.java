package cm.study.java.core.concurrent;

import java.util.concurrent.locks.ReentrantLock;

public class ReduceByLock implements Reduce {

    private int value;

    private ReentrantLock lock = new ReentrantLock();

    @Override
    public int increment() {
        lock.lock();
        try {
            value = value + 1;
        } finally {
            lock.unlock();
        }

        return value;
    }

    @Override
    public int getV() {
        return value;
    }

    @Override
    public String toString() {
        return "ReduceStudy{" +
               "value=" + value +
               '}';
    }
}
