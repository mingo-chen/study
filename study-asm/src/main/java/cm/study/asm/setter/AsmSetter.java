package cm.study.asm.setter;

import cm.study.asm.common.AsmKit;
import cm.study.asm.common.ReflectUtil;
import cm.study.asm.demo.AsmClassLoader;
import org.objectweb.asm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

public class AsmSetter<T> implements Setter<T> {

    private static Logger ILOG = LoggerFactory.getLogger(AsmSetter.class);

    private static Map<Class<?>, Setter> setterCache = new HashMap<>();

    @Override
    public void payload(T carrier, Map<String, Object> data) {
        Setter asmSetter = setterCache.get(carrier.getClass());
        if (asmSetter == null) {
            asmSetter = buildAsmSetter(carrier);
            setterCache.put(carrier.getClass(), asmSetter);
        }

        asmSetter.payload(carrier, data);
    }

    @Override
    public void rawPayload(T carrier, Map<String, String> data) {
        Setter asmSetter = setterCache.get(carrier.getClass());
        if (asmSetter == null) {
            asmSetter = buildAsmSetter(carrier);
            setterCache.put(carrier.getClass(), asmSetter);
        }

        asmSetter.rawPayload(carrier, data);
    }

    Setter buildAsmSetter(T carrier) {
        ClassWriter cw = new ClassWriter(0);
        String className = getDelegateName(carrier.getClass());
        String signature = "<T:Ljava/lang/Object;>Ljava/lang/Object;Lcm/study/asm/setter/Setter<TT;>;";

        cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER,
                className.replace(".", "/"),
                signature,
                Type.getInternalName(Object.class),
                new String[]{Type.getInternalName(Setter.class)});

        try {
            int startLine = 16;
            startLine = addDefaultConstructor(cw, startLine);
            startLine = addPayloadMethod(cw, carrier, startLine + 10);
            startLine = addRawPayloadMethod(cw, carrier, startLine + 10);
            ILOG.info("total byte code line: {}", startLine);

        } catch (Exception e) {
            ILOG.error("build byte code error", e);
        }

        cw.visitEnd();

        try {
            AsmClassLoader loader = new AsmClassLoader(cw.toByteArray(), className);
            Class<?> realClazz = loader.findClass(className);
            return (Setter) realClazz.newInstance();
        } catch (Exception e) {
            ILOG.error("class loader error", e);
        }

        return null;
    }

    static String getDelegateName(Class<?> type) {
        return "cm.study.asm.setter.AsmSetter$" + type.getSimpleName();
    }

    static int addDefaultConstructor(ClassWriter cw, int startLine) throws Exception {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(startLine++, l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        return startLine;
    }

    static <S> int addPayloadMethod(ClassWriter cw, S carrier, int startLine) {
        Class<?> type = carrier.getClass();
        Map<String, Class<?>> fieldTypes = Reflects.getFieldTypes(type);

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC,
                "payload",
                "(Ljava/lang/Object;Ljava/util/Map;)V",
                "(TT;Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;)V", null);
        mv.visitCode();

        int lineNum = startLine;
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(lineNum++, l0);

        // UserInfo userInfo = (UserInfo) carrier;
        // 把T类型转成carrier真实类型
        mv.visitVarInsn(ALOAD, 1);
        mv.visitTypeInsn(CHECKCAST, Type.getInternalName(type));
        mv.visitVarInsn(ASTORE, 3); // 转换后的结果存在3寄存器

        Label nextBlock = null;
        boolean first = true;

        for (Map.Entry<String, Class<?>> entry : fieldTypes.entrySet()) {
            Class<?> fieldType = entry.getValue();
            if (nextBlock == null) { // 第一次进入if
                nextBlock = new Label();
                mv.visitLabel(nextBlock);
                mv.visitLineNumber(lineNum++, nextBlock);

            } else {
                // 同一个Frame
                mv.visitLabel(nextBlock);
                mv.visitLineNumber(lineNum++, nextBlock);

                if (first) {
                    mv.visitFrame(F_APPEND, 1, new Object[]{Type.getInternalName(type)}, 0, null);
                    first = false;

                } else {
                    mv.visitFrame(F_SAME, 0, null, 0, null);
                }
            }

            mv.visitVarInsn(ALOAD, 2); // 加载map
            mv.visitLdcInsn(entry.getKey());
            // if(map.containsKey(key))
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "containsKey", "(Ljava/lang/Object;)Z", true);

            {
                // 不包含key block
                nextBlock = new Label();
                mv.visitJumpInsn(IFEQ, nextBlock);
            }

            {
                // 包含key block
                Label l3 = new Label();
                mv.visitLabel(l3);
                mv.visitLineNumber(lineNum++, l3);

                mv.visitVarInsn(ALOAD, 3);
                mv.visitVarInsn(ALOAD, 2);
                mv.visitLdcInsn(entry.getKey());
                mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);

                // map返回的都是Object
                // 如果是基本类型先转换成包装类型, 再调用包装类型的xxx.value()方法获取基本类型值
                if(fieldType.isPrimitive()) {
                    AsmKit.unbox(mv, fieldType);
                } else {
                    mv.visitTypeInsn(CHECKCAST, Type.getInternalName(fieldType));
                }

                // invoke set method
                String methodName = ReflectUtil.getSetterMethodName(entry.getKey());
                String descriptor = Type.getMethodDescriptor(Type.getType(void.class), Type.getType(fieldType));
                mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(type), methodName, descriptor, false);
            }
        }

        if (nextBlock != null) {
            mv.visitLabel(nextBlock);
            mv.visitLineNumber(lineNum++, nextBlock);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        }
        mv.visitInsn(RETURN);

        mv.visitMaxs(3, 4);
        mv.visitEnd();

        return lineNum;
    }

    static <S> int addRawPayloadMethod(ClassWriter cw, S carrier, int startLine) {
        Class<?> type = carrier.getClass();
        Map<String, Class<?>> fieldTypes = Reflects.getFieldTypes(type);

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC,
                "rawPayload",
                "(Ljava/lang/Object;Ljava/util/Map;)V",
                "(TT;Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;)V", null);
        mv.visitCode();

        int lineNum = startLine;
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(lineNum++, l0);

        // UserInfo userInfo = (UserInfo) carrier;
        // 把T类型转成carrier真实类型
        mv.visitVarInsn(ALOAD, 1);
        mv.visitTypeInsn(CHECKCAST, Type.getInternalName(type));
        mv.visitVarInsn(ASTORE, 3); // 转换后的结果存在3寄存器

        Label nextBlock = null;
        boolean first = true;

        for (Map.Entry<String, Class<?>> entry : fieldTypes.entrySet()) {
            Class<?> fieldType = entry.getValue();
            if (nextBlock == null) { // 第一次进入if
                nextBlock = new Label();
                mv.visitLabel(nextBlock);
                mv.visitLineNumber(lineNum++, nextBlock);

            } else {
                // 同一个Frame
                mv.visitLabel(nextBlock);
                mv.visitLineNumber(lineNum++, nextBlock);

                if (first) {
                    mv.visitFrame(F_APPEND, 1, new Object[]{Type.getInternalName(type)}, 0, null);
                    first = false;

                } else {
                    mv.visitFrame(F_SAME, 0, null, 0, null);
                }
            }

            mv.visitVarInsn(ALOAD, 2); // 加载map
            mv.visitLdcInsn(entry.getKey());
            // if(map.containsKey(key))
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "containsKey", "(Ljava/lang/Object;)Z", true);

            {
                // 不包含key block
                nextBlock = new Label();
                mv.visitJumpInsn(IFEQ, nextBlock);
            }

            {
                // 包含key block
                Label l3 = new Label();
                mv.visitLabel(l3);
                mv.visitLineNumber(lineNum++, l3);

                mv.visitVarInsn(ALOAD, 3);
                mv.visitVarInsn(ALOAD, 2);
                mv.visitLdcInsn(entry.getKey());
                mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);

                // map返回的都是Object, 先转成String,
                mv.visitTypeInsn(CHECKCAST, "java/lang/String");
                AsmKit.cast(mv, fieldType);

                // invoke set method
                String methodName = ReflectUtil.getSetterMethodName(entry.getKey());
                String descriptor = Type.getMethodDescriptor(Type.getType(void.class), Type.getType(fieldType));
                mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(type), methodName, descriptor, false);
            }
        }

        if (nextBlock != null) {
            mv.visitLabel(nextBlock);
            mv.visitLineNumber(lineNum++, nextBlock);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        }
        mv.visitInsn(RETURN);

        mv.visitMaxs(3, 4);
        mv.visitEnd();

        return lineNum;
    }
}
