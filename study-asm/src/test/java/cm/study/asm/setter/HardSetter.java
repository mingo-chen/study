package cm.study.asm.setter;

import java.util.Date;
import java.util.Map;

public class HardSetter<T> implements Setter<T> {

    public void payload(T carrier, Map<String, Object> data) {
        UserInfo userInfo = (UserInfo) carrier;
        if (data.containsKey("id")) {
            userInfo.setId((long) data.get("id"));
        }

        if (data.containsKey("nickname")) {
            userInfo.setNickname((String) data.get("nickname"));
        }

        if (data.containsKey("sex")) {
            userInfo.setSex((boolean) data.get("sex"));
        }

        if (data.containsKey("age")) {
            userInfo.setAge((int) data.get("age"));
        }

        if (data.containsKey("birthday")) {
            userInfo.setBirthday((Date) data.get("birthday"));
        }

        if (data.containsKey("weight")) {
            userInfo.setWeight((double) data.get("weight"));
        }
    }

    public void rawPayload(T carrier, Map<String, String> data) {
        UserInfo userInfo = (UserInfo) carrier;
        if (data.containsKey("id")) {
            userInfo.setId(Reflects.to(data.get("id"), long.class));
        }

        if (data.containsKey("nickname")) {
            userInfo.setNickname(Reflects.to(data.get("nickname"), String.class));
        }

        if (data.containsKey("sex")) {
            userInfo.setSex(Reflects.to(data.get("sex"), boolean.class));
        }

        if (data.containsKey("age")) {
            userInfo.setAge(Reflects.to(data.get("age"), int.class));
        }

        if (data.containsKey("birthday")) {
            userInfo.setBirthday(Reflects.to(data.get("birthday"), Date.class));
        }

        if (data.containsKey("weight")) {
            userInfo.setWeight(Reflects.to(data.get("weight"), double.class));
        }
    }
}
