package cm.study.asm.demo;

import com.sun.webkit.network.URLs;
import ext.test4j.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.net.URL;

import static org.testng.Assert.*;

public class AsmClassLoaderTest {

    @Test
    public void testFindClass() throws Exception {
//        URL url = new URL("file:/tmp/cm");
//        URL url = new URL("file:/Users/ahcming/workspace/github/study/study-asm/target/classes/cm");

//        AsmClassLoader classLoader = new AsmClassLoader(new URL[]{url});
//        Class<?> clazz = classLoader.findClass("cm.study.asm.demo.HelloWorld");
//        System.out.println("--> " + clazz);
    }

    @Test
    public void testFindClass2() throws Exception {
//        URL url = new URL("file:/Users/ahcming/workspace/github/study/study-asm/target/test-classes");
//        InputStream input = url.openStream();
//        System.out.println("==> " + IOUtils.readLines(input));
//
//        AsmClassLoader classLoader = new AsmClassLoader(new URL[]{url});
//        Class<?> clazz = classLoader.findClass("cm.study.asm.demo.HelloTest");
//        System.out.println("--> " + clazz);
    }
}