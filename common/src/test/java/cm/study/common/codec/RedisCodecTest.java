package cm.study.common.codec;

import cm.study.common.model.People;
import cm.study.common.model.Pet;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * -XX:-UseSplitVerifier
 * Created by chenming on 2017/3/25.
 */
public class RedisCodecTest {

    public static void main(String[] args) {
        People people = new People(123456, "cm", 30, 1.71, true);
        people.setLikes(new String[] {"编程", "电影", "冥想"});
        people.setBankIds(Arrays.asList(123456l, 4567890l, 987654321l));
        Map<String, Object> exts = new HashMap<>();
        exts.put("wife", "ljx");
        exts.put("money", 12345);
        people.setExt(exts);

        Pet dog = new Pet("二哈", 3.21f, 3);
        dog.setFoods(new Long[]{123l, 345l, 567l});
        people.setPet(dog);

        String encode = RedisCodec.with(people).encode();
        System.out.println("-->" + encode);

//        String json = JSON.toJSON(people);
//        System.out.println("-->" + json);
//        System.out.println("rc length:"+encode.length()+", json length:" + json.length());
//
        People fromDecode = RedisCodec.with(encode).decode(Arrays.asList(People.class));
        System.out.println("-->" + fromDecode);

    }

    @Test
    public void test_encode() throws Exception {
        People people = new People(123456, "cm", 30, 1.71, false);
        String encode = RedisCodec.with(people).encode();
        System.out.println("-->" + encode);
    }


    @Test
    public void test_decode() throws Exception {
        People people = new People(123456, "cm", 30, 1.71, false);
        String encode = RedisCodec.with(people).encode();

        People fromDecode = RedisCodec.with(encode).decode(Arrays.asList(People.class));
        System.out.println("-->" + fromDecode);
    }
}