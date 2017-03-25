package cm.study.rxjava;

import cm.study.rxjava.eds.EdsUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenming on 2017/3/1.
 */
public class ObservableBuildTest {

    @Test
    public void amb() throws Exception{
        Observable<Integer> source1 = Observable.just(1, 2, 3).delay(3, TimeUnit.SECONDS);
        Observable<Integer> source2 = Observable.just(4, 5, 6).delay(2, TimeUnit.SECONDS);
        Observable<Integer> source3 = Observable.just(7, 8, 9).delay(1, TimeUnit.SECONDS);

        Observable.amb(Arrays.asList(source1, source2, source3))
                .subscribe((integer) -> { System.out.println("--> " + integer); });

        Observable.ambArray(source1, source2, source3).subscribe(integer -> {System.out.println("--> " + integer);});

        Thread.sleep(6000);
    }

    @Test
    public void combineLatest() throws Exception {


    }

    @Test
    public void concat() throws Exception {
        Observable<Integer> source1 = Observable.just(1, 2, 3).delay(100, TimeUnit.MILLISECONDS);
        Observable<Integer> source2 = Observable.just(4, 5, 6, 7).delay(100, TimeUnit.MILLISECONDS);
        Observable<Integer> source3 = Observable.just(8, 9).delay(100, TimeUnit.MILLISECONDS);

        Observable.concatEager(Arrays.asList(source2, source1, source3)).subscribe(integer -> System.out.println(integer));

//        EdsUtils.sleep(50000);
        Thread.sleep(6000);
    }

    @Test
    public void create() throws Exception {
        Observable.create((ObservableEmitter<Integer>  emitter) -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
        }).subscribe(integer -> {
            System.out.println(integer);
        });


    }

    @Test
    public void just() throws Exception {
        Observable.just(1, 2, 3).subscribe((integer) -> {System.out.println(integer);});
    }
}
