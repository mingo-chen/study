package cm.study.asm.demo;

import java.net.URL;
import java.net.URLClassLoader;

public class AsmClassLoader extends URLClassLoader {

    private URL[] urls;

    public AsmClassLoader(URL[] urls) {
        super(urls, getSystemClassLoader());
        this.urls = urls;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
//        return defineClass(null, byteCodes, 0, byteCodes.length);
        Class<?> aClass = findLoadedClass(name);
        if (aClass != null) {
            return aClass;
        }

        if(!"cm.study.asm.demo".startsWith(name)) {
            return super.loadClass(name);
        }

        return null;
    }
}
