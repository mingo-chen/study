package cm.study.common.io;

import static org.testng.Assert.*;

/**
 * Created by chenming on 2017/9/16.
 */
public class FileNIOTest {

    public static void main(String[] args) {
        System.out.println(FileNIO.read("/Users/chenming/workspace/github/study/common/src/main/resources/log4j.xml"));
    }
}