package cm.study.asm.setter;

import cm.study.asm.common.DateUtil;
import org.objectweb.asm.Type;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

public class AsmSetterTest {

    @Test
    public void testPayload() {
        UserInfo target = new UserInfo();
        Map<String, Object> data = new HashMap<>();
        data.put("id", 123L);
        data.put("nickname", "cm");
        data.put("sex", true);
        data.put("age", 30);
        data.put("birthday", DateUtil.fromString("1988-06-12 12:00:00", DateUtil.YYYYMMDDHHmmss));
        data.put("weight", 74.5);

        AsmSetter<UserInfo> setter = new AsmSetter();
        setter.payload(target, data);

//        System.out.println("--> " + target);
    }

    @Test
    public void testRawPayload() {
        UserInfo target = new UserInfo();
        Map<String, String> data = new HashMap<>();
        data.put("id", "123");
        data.put("nickname", "cm");
        data.put("sex", "true");
        data.put("age", "30");
        data.put("birthday", "1988-06-12 12:00:00");
        data.put("weight", "74.5");

        AsmSetter<UserInfo> setter = new AsmSetter();
        setter.rawPayload(target, data);

//        System.out.println("--> " + target);
    }

    @Test
    public void test_byteCode() {
        Class<?> fieldType = long.class;
        String descriptor = Type.getMethodDescriptor(Type.getType(void.class), Type.getType(fieldType));
        System.out.println("---> " + descriptor);
    }

    @Benchmark
    @BenchmarkMode(value = {Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void test_payload_bench_mark() {
        testPayload();
    }

    @Benchmark
    @BenchmarkMode(value = {Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void test_raw_payload_bench_mark() {
        testRawPayload();
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(AsmSetterTest.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}