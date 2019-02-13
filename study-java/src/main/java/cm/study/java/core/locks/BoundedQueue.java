package cm.study.java.core.locks;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueue {
    private Integer[] items;//定义为数组，在创建对象时就确定容量
    private Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    private int count;
    private int addIndex, removeIndex;

    public BoundedQueue(int size) {
        items = new Integer[size];
    }

    public void add(Integer object) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                notFull.await();
            }

            int idx = (++addIndex) % items.length;
            if (items[idx] != null) {
                throw new RuntimeException("xxx");
            }

            items[idx] = object;
            count++;
//            System.out.println(Thread.currentThread() + " 插入一个元素，数组为：" + Arrays.toString(items));
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public Integer remove() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();
            }

            int idx = (++removeIndex) % items.length;
            Integer temp = items[idx];

            if (items[idx] == null) {
                throw new RuntimeException("xxx");
            }

            items[idx] = null;
//            System.out.println(Thread.currentThread() + " 移除一个元素，数组为：" + Arrays.toString(items));
            count--;
            notFull.signal();
            return temp;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(items) + ":" + addIndex + ":" + removeIndex;
    }
}
