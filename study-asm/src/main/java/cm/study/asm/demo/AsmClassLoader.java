package cm.study.asm.demo;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLClassLoader;

public class AsmClassLoader extends ClassLoader {

    private URLClassLoader real;

    private static final String path = "/Users/ahcming/workspace/github/study/study-asm/target/classes";

    public AsmClassLoader(byte[] byteCode, String className) throws Exception {
        writeDisk(byteCode, className);
        URL[] urls = new URL[]{new URL("file:" + path)};
        real = new URLClassLoader(urls, getSystemClassLoader());
    }

    public void writeDisk(byte[] byteData, String className) {
        try {
            File source = new File(path + "/" + StringUtils.replace(className, ".", "/") + ".class");
            FileOutputStream out = new FileOutputStream(source);
            out.write(byteData);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        return real.loadClass(name);
    }
}
