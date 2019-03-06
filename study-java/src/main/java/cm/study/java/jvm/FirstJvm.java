package cm.study.java.jvm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 观察GC日志,
 * 使用G1GC参数
 * -Xmx64m -Xms64m -server -verbose:gc -Xloggc:/tmp/log/gc.log
 * -XX:+UseG1GC -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -XX:+PrintAdaptiveSizePolicy -XX:+PrintReferenceGC -XX:+ParallelRefProcEnabled -XX:+PrintTenuringDistribution -XX:+PrintCommandLineFlags
 *
 * 使用ParallelGC, 默认就是
 * -Xmx64m -Xms64m -server -verbose:gc -Xloggc:/tmp/log/gc.log
 * -XX:+UseParallelGC -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -XX:+PrintTenuringDistribution -XX:+PrintCommandLineFlags
 */
public class FirstJvm {
    private static Logger ILOG = LoggerFactory.getLogger(FirstJvm.class);
    
    public static void main(String[] args) {

        for(int i = 0; ;i++) {
            byte[] bs = new byte[1024]; // 一直创建1M的对象
            ILOG.info("allocate 1m object: {}", i);
        }
    }
}
