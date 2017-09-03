package cm.study.akka.game;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chenming on 2017/7/19.
 */
public class Shudu extends UntypedActor {

    private static Logger ILOG = LoggerFactory.getLogger(Shudu.class);

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof String) {
            ILOG.info("receive message: {}", message);

            ILOG.info("start to do something!");

            ActorRef sender = getSender();
            if (sender != null) {
//                sender.tell("done", getSelf());
                sender.tell(new Exception("Boom!!!"), getSelf());
            } else {
                ILOG.warn("sender is null, can't notify result to her!");
            }
        } else {
            unhandled(message);
        }
    }
}
