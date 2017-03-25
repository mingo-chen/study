package cm.study.rxjava;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by chenming on 2017/2/22.
 */
public class FlowableStudy {

    public static void first() {
        Flowable.fromArray("Hello", "world").contains("world").subscribe(System.out::println);
    }

    public static void main(String[] args) {
        first();
    }
}
