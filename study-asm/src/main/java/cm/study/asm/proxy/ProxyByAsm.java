package cm.study.asm.proxy;

import cm.study.asm.common.ReflectUtil;
import cm.study.asm.demo.AsmClassLoader;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ProxyByAsm {

    private Object instance;

    public ProxyByAsm(Object object) {
        instance = object;
    }

    public Object wrapper() {
        try {
            Class<?> clazz = instance.getClass();
            String className = clazz.getName() + "Dynamic";
            System.out.println("===> " + className);
            ClassWriter writer = _genClazz(clazz, StringUtils.replace(className, ".", "/"));

            Object proxy = checkAndGet(writer.toByteArray(), className);
            return proxy;

        } catch (Exception e) {
            e.printStackTrace();
            return instance;
        }
    }

    Object checkAndGet(byte[] clazzData, String className) throws Exception {
//        URL sourceUrl = new URL("file:" + path);
        AsmClassLoader loader = new AsmClassLoader(clazzData, className);
        Class<?> realClazz = loader.findClass(className);
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
            int stacks = 0;  // load了多少个变量
            int locals = 0;  // 本地变量有多少个, 包含返回值

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
            locals++;
            stacks++;
            visitor.visitFieldInsn(Opcodes.GETFIELD, className, "target", Type.getDescriptor(clazz));
            Class<?>[] paramTypes = method.getParameterTypes();
            for(int idx = 0; idx < paramTypes.length; idx++) {
                Class<?> type = paramTypes[idx];
                visitor.visitVarInsn(ReflectUtil.getLoadFlag(type), idx+1);
                locals++;
                stacks++;
            }

            visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ReflectUtil.typeToString(clazz), method.getName(), desc, false);

            if (method.getReturnType() != void.class) {
                visitor.visitVarInsn(ReflectUtil.getStoreFlag(method.getReturnType()), paramTypes.length+1);
                locals++;
            }

            // after real invoke
            visitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            visitor.visitLdcInsn("start clean work...");
            visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

            if (method.getReturnType() != void.class) {
                visitor.visitVarInsn(ReflectUtil.getLoadFlag(method.getReturnType()), paramTypes.length+1);
                visitor.visitInsn(ReflectUtil.getReturnFlag(method.getReturnType()));

            } else {
                visitor.visitInsn(Opcodes.RETURN);
            }

            visitor.visitMaxs(stacks, locals);
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
        }

        sb.append(")");
        sb.append(ReflectUtil.getShortName(method.getReturnType()));

        return sb.toString();
    }
}
