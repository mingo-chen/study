package cm.study.common.io;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by chenming on 2017/9/16.
 */
public class FileIOTest {

    @Test
    public void testRead() {
        System.out.println(FileIO.read("/Users/chenming/workspace/github/study/common/src/main/resources/log4j.xml"));
    }

    public static void main(String[] args) {
        System.out.println(FileIO.read("/Users/chenming/workspace/github/study/common/src/main/resources/log4j.xml"));
    }
}