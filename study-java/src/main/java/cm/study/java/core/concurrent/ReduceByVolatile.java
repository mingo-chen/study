package cm.study.java.core.concurrent;

public class ReduceByVolatile implements Reduce {

    /**
     * 通过volatile不可以多线程并发安全
     * 只能多线程可见和防止代码重排
     */
    private volatile int value;

    public int increment() {
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
