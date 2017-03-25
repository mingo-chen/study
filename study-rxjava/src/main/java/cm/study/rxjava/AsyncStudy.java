package cm.study.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chenming on 2017/2/23.
 */
public class AsyncStudy {
    
    private static final Logger ILOG = LoggerFactory.getLogger( AsyncStudy.class );

    public static void main(String[] args) throws Exception {
        ILOG.info("--> start!!!");
        Observable.create(
                new ObservableOnSubscribe<Integer>() {

                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        System.out.println("Observable thread is: " + Thread.currentThread().getName());
                        emitter.onNext(1);
                    }

                }
            )
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe(new Consumer<Integer>() {
                @Override
                public void accept(@NonNull Integer integer) throws Exception {
                    ILOG.info("Observer thread is: {}", Thread.currentThread().getName());
                    ILOG.info("receive number: {}", integer);
                }
            });

        Thread.sleep(5000);
        ILOG.info("--> end!!!");
    }
}
