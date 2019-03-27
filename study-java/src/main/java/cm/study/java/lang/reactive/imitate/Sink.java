package cm.study.java.lang.reactive.imitate;

import java.util.Iterator;

/**
 * 代表数据
 */
public interface Sink<T> {

    void accept(T each);

//    Iterator<T> iterator();
}
