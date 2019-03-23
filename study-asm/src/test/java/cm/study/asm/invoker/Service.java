package cm.study.asm.invoker;

import org.apache.commons.lang3.RandomUtils;

public class Service {

    public void save(int id, String name, String address) {
        System.out.printf("start save record, id=%d, name=%s, address=%s\n", id, name, address);
        System.out.printf("save record complete, id=%d, name=%s, address=%s\n", id, name, address);
    }

    public String get(int id) {
        System.out.printf("start get record, id=%d\n", id);
        String result = String.valueOf(RandomUtils.nextInt(100, 200));
        System.out.printf("get record complete, id=%d, result=%s\n", id, result);
        return result;
    }
}
