package cm.study.java.core.concurrent;

public class ReduceBySynchronized implements Reduce {

    private int value;

    /**
     * 通过synchronized可以多线程并发安全
     */
    public synchronized int increment() {
        value = value + 1;
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
