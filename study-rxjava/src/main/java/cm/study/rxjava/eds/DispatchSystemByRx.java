package cm.study.rxjava.eds;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Created by chenming on 2017/2/28.
 */
public class DispatchSystemByRx {

    public static final Random RAND = new Random();

    private static final Logger ILOG = LoggerFactory.getLogger( DispatchSystemByRx.class );

    /** 总电梯台数 */
    public static final int TOTAL_ELEVATORS = 3;

    /** 总楼层 */
    public static final int TOTAL_FLOORS = 7;


    public void run() {
        Observable.create((emitter) -> {
                    for(;;) {
                        if(RAND.nextInt(100) < 50) {
                            People people = PeopleFactory.build(TOTAL_FLOORS);
                            emitter.onNext(people);
                            ILOG.info("来了一个向上的人:{}", people);
                        } else {
                            ILOG.debug("没有人来");
                        }

                        // 遍历
                        EdsUtils.sleep(100);
                    }

                })

                .observeOn(Schedulers.newThread())

                .subscribe((people) ->
                    ILOG.info("对来人进入处理: {}", people)
                );


    }

    public static void main(String[] args) {
        DispatchSystemByRx systemByRx = new DispatchSystemByRx();
        systemByRx.run();
    }

}
