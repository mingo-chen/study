package cm.study.asm.invoker;

import cm.study.asm.common.ReflectUtil;
import cm.study.asm.demo.AsmClassLoader;
import org.objectweb.asm.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

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

    ClassWriter _genByteCode(Class<?> clazz) throws Exception {
        ClassWriter writer = new ClassWriter(0);
        String className = ReflectUtil.typeToString(clazz) + "Wrapper";
        writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null,
                ReflectUtil.typeToString(Object.class),
                new String[]{ReflectUtil.typeToString(Invoker.class)});

        // default constructor
        defaultConstructor(writer);

        // field
        addField(writer, clazz, "target");

        // method call
        addCallMethod(writer, clazz);

        writer.visitEnd();

        return writer;
    }

    void defaultConstructor(ClassWriter clazzWriter) {
        MethodVisitor constructor = clazzWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V",null, null);
        constructor.visitCode();
        constructor.visitVarInsn(Opcodes.ALOAD, 0);
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, ReflectUtil.typeToString(Object.class), "<init>", "()V", false);
        constructor.visitInsn(Opcodes.RETURN);
        constructor.visitMaxs(1, 1);
        constructor.visitEnd();
    }

    void addField(ClassWriter clazzWriter, Class<?> fieldType, String fieldName) {
        FieldVisitor fieldVisitor = clazzWriter.visitField(Opcodes.ACC_PRIVATE,fieldName,
                Type.getDescriptor(fieldType), null, null);
        fieldVisitor.visitEnd();
    }

    void addCallMethod(ClassWriter clazzWriter, Class<?> target) throws Exception {
        int stacks = 8;         // load了多少个变量
        int locals = 2;         // 本地变量有多少个, 包含返回值
        int codeLine = 29;     // 反编译得到的

        String desc = Type.getMethodDescriptor(Type.getType(Object.class), Type.getType(Invocation.class));
        MethodVisitor mv = clazzWriter.visitMethod(Opcodes.ACC_PUBLIC, "call", desc, null, null);
        mv.visitCode();

        {
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(codeLine, l0);

            // invocation cast formInvocation
            mv.visitVarInsn(Opcodes.ALOAD, 1);     // load args from param1
            mv.visitTypeInsn(Opcodes.CHECKCAST, Type.getInternalName(FormInvocation.class));
            mv.visitVarInsn(Opcodes.ASTORE, 2);    // from store in 2
            locals++;

            codeLine++;
        }

        {
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(codeLine, l1);

            mv.visitVarInsn(Opcodes.ALOAD, 2);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type.getInternalName(FormInvocation.class), "getMethodName", "()Ljava/lang/String;", false);
            mv.visitVarInsn(Opcodes.ASTORE, 3);  // methodName store in 3
            locals++;

            codeLine++;
        }

        {
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(codeLine, l2);

            mv.visitVarInsn(Opcodes.ALOAD, 2);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type.getInternalName(FormInvocation.class), "size", "()I", false);
            mv.visitVarInsn(Opcodes.ISTORE, 4);  // size store in 3

            locals++;

            codeLine++;
        }

        Method[] methods = target.getDeclaredMethods();
        Label nextBlock = null;
        boolean firstBlock = true;
        Set localVars = new LinkedHashSet();
        localVars.add(ReflectUtil.typeToString(FormInvocation.class));

        for (Method method : methods) {
            if (nextBlock == null) {
                nextBlock = new Label();
                mv.visitLabel(nextBlock);
                mv.visitLineNumber(codeLine++, nextBlock);

            } else {
                mv.visitLabel(nextBlock);
                mv.visitLineNumber(codeLine++, nextBlock);

                if (firstBlock) {
//                    System.out.println("---> " + localVars);
                    mv.visitFrame(Opcodes.F_APPEND, localVars.size(), localVars.toArray(new Object[0]), 0, null);
                    firstBlock = false;
                } else {
                    mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                }
            }

            {
                // methodName == method.getName
                mv.visitVarInsn(Opcodes.ALOAD, 3); // methodName
                mv.visitLdcInsn(method.getName());
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);

                localVars.add(ReflectUtil.typeToString(String.class));
            }

            nextBlock = new Label();
            mv.visitJumpInsn(Opcodes.IFEQ, nextBlock);

            {
                // size == method.getParameterTypes
                mv.visitVarInsn(Opcodes.ILOAD, 4); // size
                mv.visitInsn(Opcodes.ICONST_0 + method.getParameterTypes().length); // 最多支持6个参数的方法重载
                mv.visitJumpInsn(Opcodes.IF_ICMPNE, nextBlock);

                localVars.add(Opcodes.INTEGER);
            }

            {
                // 相等, 则执行目标方法
                Label callBlock = new Label();
                mv.visitLabel(callBlock);
                mv.visitLineNumber(codeLine++, callBlock);

                mv.visitVarInsn(Opcodes.ALOAD, 0);
                String myself = (target.getName() + "Wrapper").replace(".", "/");
                mv.visitFieldInsn(Opcodes.GETFIELD, myself, "target", Type.getDescriptor(target));

                for (int i = 0; i < method.getParameterTypes().length; i++) {
                    Class<?> paramType = method.getParameterTypes()[i];
                    mv.visitVarInsn(Opcodes.ALOAD, 2);
                    mv.visitInsn(Opcodes.ICONST_0 + i);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type.getInternalName(FormInvocation.class), "get", "(I)Ljava/lang/Object;", false);

//                    System.out.printf("unwrap param, index: %d, type: %s \n", i, paramType);

                    if (paramType.isPrimitive()) { // 拆包
                        Class<?> wrapType = ReflectUtil.getWrapType(paramType);
                        mv.visitTypeInsn(Opcodes.CHECKCAST, Type.getInternalName(wrapType));
                        Method unwrapMethod = ReflectUtil.getUnwrapMethod(paramType);
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type.getInternalName(wrapType),
                                unwrapMethod.getName(), Type.getMethodDescriptor(unwrapMethod), false);
                    } else {
                        mv.visitTypeInsn(Opcodes.CHECKCAST, Type.getInternalName(paramType));
                        localVars.add(ReflectUtil.typeToString(paramType));
                    }

                }

                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type.getInternalName(target), method.getName(), Type.getMethodDescriptor(method), false);
                Class<?> retType = method.getReturnType();
                if (retType.isPrimitive() && retType != void.class) { // 装包
                    //mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
                    Class<?> wrapType = ReflectUtil.getWrapType(retType);
                    Method wrapMethod = ReflectUtil.getWrapMethod(retType);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(wrapType),
                            wrapMethod.getName(), Type.getMethodDescriptor(wrapMethod), false);
                }

                {
                    // add return
                    Label returnLabel = new Label();
                    mv.visitLabel(returnLabel);
                    mv.visitLineNumber(codeLine++, returnLabel);

                    if (retType == void.class) {
                        mv.visitInsn(Opcodes.ACONST_NULL);
                    }

                    mv.visitInsn(Opcodes.ARETURN);
                }
            }
        }

        {
            // add throw Exception
            mv.visitLabel(nextBlock);
            mv.visitLineNumber(codeLine++, nextBlock);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

            mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(RuntimeException.class));
            mv.visitInsn(Opcodes.DUP);

            mv.visitLdcInsn("method not exist!");
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/RuntimeException", "<init>", "(Ljava/lang/String;)V", false);
            mv.visitInsn(Opcodes.ATHROW);
        }

        mv.visitMaxs(stacks, locals);
        mv.visitEnd();
    }
}
