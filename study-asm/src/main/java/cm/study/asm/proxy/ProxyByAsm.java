package cm.study.asm.proxy;

import cm.study.asm.demo.AsmClassLoader;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.*;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;

public class ProxyByAsm {

    private Object instance;

    public ProxyByAsm(Object object) {
        instance = object;
    }

    public Object wrapper() {
        try {
            Class<?> clazz = instance.getClass();
            String className = StringUtils.replace(clazz.getName(), ".", "/") + "Dynamic";
            ClassWriter writer = _genClazz(clazz, className);

            String path = "/Users/ahcming/workspace/github/study/study-asm/target/classes";
            output(writer, path, className);

            Object proxy = checkAndGet(path, className);
            return proxy;

        } catch (Exception e) {
            e.printStackTrace();
            return instance;
        }

    }

    public void output(ClassWriter clazzWriter, String path, String className) throws Exception {
        byte[] clazzData = clazzWriter.toByteArray();

        File source = new File(path + "/" + className + ".class");
        FileOutputStream out = new FileOutputStream(source);
        out.write(clazzData);
        out.close();
    }

    Object checkAndGet(String path, String className) throws Exception {
        URL sourceUrl = new URL("file:" + path);
        AsmClassLoader loader = new AsmClassLoader(new URL[]{sourceUrl});
        Class<?> realClazz = loader.findClass(StringUtils.replace(className, "/", "."));
        System.out.println("output --> " + realClazz);
        Method[] methods = realClazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("method: " + method);
        }

        Object ins = realClazz.newInstance();
        Field field = realClazz.getDeclaredField("target");
        field.setAccessible(true);
        field.set(ins, instance);
        return ins;
    }

    ClassWriter _genClazz(Class<?> clazz, String className) {
        ClassWriter clazzWriter = new ClassWriter(0);
        clazzWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className,
                null, ReflectUtil.typeToString(clazz), null);

        FieldVisitor fieldVisitor = clazzWriter.visitField(Opcodes.ACC_PRIVATE,"target",
//                ReflectUtil.typeToString(clazz), null, null);
                Type.getDescriptor(clazz), null, null);
        fieldVisitor.visitEnd();

        // 增加构造方法
        MethodVisitor constructor = clazzWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V",null, null);
        constructor.visitCode();
        constructor.visitVarInsn(Opcodes.ALOAD, 0);
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, ReflectUtil.typeToString(clazz), "<init>", "()V", false);
        constructor.visitInsn(Opcodes.RETURN);
        constructor.visitMaxs(1, 1);
        constructor.visitEnd();

        for (Method method : clazz.getDeclaredMethods()) {
            String desc = getMethodDesc(method);
            String signature = null;
            String[] exceptions = null;
            MethodVisitor visitor = clazzWriter.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), desc, signature, exceptions);
            visitor.visitCode();

            // before real invoke
            visitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            visitor.visitLdcInsn("start init work...");
            visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

            // real invoke
            visitor.visitVarInsn(Opcodes.ALOAD, 0);
            visitor.visitFieldInsn(Opcodes.GETFIELD, className, "target", Type.getDescriptor(clazz));
            visitor.visitVarInsn(Opcodes.ALOAD, 1);
            visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ReflectUtil.typeToString(clazz), method.getName(), desc, false);

            // after real invoke
            visitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            visitor.visitLdcInsn("start clean work...");
            visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            visitor.visitInsn(Opcodes.RETURN);

            visitor.visitMaxs(3, 2);
            visitor.visitEnd();
        }

        clazzWriter.visitEnd();
        return clazzWriter;
    }

    public static String getMethodDesc(Method method) {
        StringBuilder sb = new StringBuilder("(");

        for(int idx = 0; idx < method.getParameterTypes().length; idx++) {
            Class<?> clazz = method.getParameterTypes()[idx];
            sb.append(ReflectUtil.getShortName(clazz));

            if (idx == method.getParameterTypes().length - 1) {
                sb.append(";");
            } else {
                sb.append(",");
            }
        }

        sb.append(")");
        sb.append(ReflectUtil.getShortName(method.getReturnType()));

        return sb.toString();
    }
}
