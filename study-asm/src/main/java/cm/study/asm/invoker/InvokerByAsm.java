package cm.study.asm.invoker;

import cm.study.asm.common.ReflectUtil;
import cm.study.asm.common.Target;
import cm.study.asm.demo.AsmClassLoader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class InvokerByAsm {

    private Map<Class<?>, Invoker> invokerMap = new HashMap<>();

    public Invoker wrapper(Object instance) {
        Class<?> target = instance.getClass();
        Invoker invoker = invokerMap.get(target);
        if (invoker != null) {
            return invoker;
        }

        Invoker wrapperTarget = _gen(target, instance);
        if (null != wrapperTarget) {
            invokerMap.put(target, wrapperTarget);
        }
        return wrapperTarget;
    }

    Invoker _gen(Class<?> clazz, Object instance) {
        try {
            ClassWriter clazzWriter = _genByteCode(clazz);

            String className = clazz.getName() + "Wrapper";  // clazz + Wrapper
            byte[] clazzData = clazzWriter.toByteArray();

            Class<?> wrapperClazz = new AsmClassLoader(clazzData, className).findClass(className);
            Invoker wrapper = (Invoker)wrapperClazz.newInstance();

            Field proxy = wrapperClazz.getDeclaredField("target");
            proxy.setAccessible(true);

            proxy.set(wrapper, instance);

            return wrapper;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    ClassWriter _genByteCode(Class<?> clazz) {
        ClassWriter writer = new ClassWriter(0);
        String className = ReflectUtil.typeToString(clazz) + "Wrapper";
        writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null,
                ReflectUtil.typeToString(Object.class),
                new String[]{ReflectUtil.typeToString(Invoker.class)});

        writer.visitEnd();

        return writer;
    }

}
