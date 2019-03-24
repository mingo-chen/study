package cm.study.springboot.demo;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private Map<Integer, UserBean> UserBeanMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        UserBean UserBean1 = new UserBean(1, "tom1");
        UserBean UserBean2 = new UserBean(2, "tom2");
        UserBean UserBean3 = new UserBean(3, "tom3");
        UserBean UserBean4 = new UserBean(4, "tom4");
        UserBeanMap.put(1, UserBean1);
        UserBeanMap.put(2, UserBean2);
        UserBeanMap.put(3, UserBean3);
        UserBeanMap.put(4, UserBean4);
    }

    public Mono<String> test() {
        System.out.println("[TestService].....");
        return Mono.just("hello word");
    }

    public Flux<UserBean> getUserBeanList(Flux<Integer> ids) {
        return ids.flatMap(id -> Mono.justOrEmpty(this.UserBeanMap.get(id)));
    }

    public Mono<UserBean> getUserBeanById(int id) {
        return Mono.justOrEmpty(this.UserBeanMap.get(id));
    }

    public Flux<UserBean> createOrUpdate(Flux<UserBean> UserBeans) {
        return UserBeans.doOnNext(UserBean -> this.UserBeanMap.put(UserBean.getId(), UserBean));
    }

    public Mono<UserBean> createOrUpdate(UserBean UserBean) {
        this.UserBeanMap.put(UserBean.getId(), UserBean);
        return Mono.just(UserBean);
    }

    public Mono<UserBean> deleteById(int id) {
        return Mono.justOrEmpty(this.UserBeanMap.remove(id));
    }
}
