package cm.study.asm.common;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;

public class AsmKit {

    /**
     * 把基本类型进行装包
     * @param mv
     * @param type
     */
    public static void box(MethodVisitor mv, Class<?> type) {

    }

    /**
     * 对基本类型进行解包
     * @param mv
     * @param type
     */
    public static void unbox(MethodVisitor mv, Class<?> type) {
        Class<?> boxType = ReflectUtil.getWrapType(type);
        Method unboxMethod = ReflectUtil.getUnwrapMethod(type);

        mv.visitTypeInsn(CHECKCAST, Type.getInternalName(boxType));
        mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(boxType), unboxMethod.getName(), Type.getMethodDescriptor(unboxMethod), false);
    }

    public static void cast(MethodVisitor mv, Class<?> targetType) {
        if (targetType.isPrimitive()) {
            // 基本类型, 先换成对应的包装类型
            Class<?> boxType = ReflectUtil.getWrapType(targetType);
            mv.visitFieldInsn(GETSTATIC, Type.getInternalName(boxType), "TYPE", "Ljava/lang/Class;");
            // 通过Reflects#to方法把String类型的数据转成目标类型的数据
            mv.visitMethodInsn(INVOKESTATIC, "cm/study/asm/setter/Reflects", "to", "(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;", false);
            // 对结果进行强转
            mv.visitTypeInsn(CHECKCAST, Type.getInternalName(boxType));
            Method getValueMethod = ReflectUtil.getUnwrapMethod(targetType);
            // 调用包装类型的解包方法获取基本类型数据
            mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(boxType), getValueMethod.getName(), org.objectweb.asm.Type.getMethodDescriptor(getValueMethod), false);

        } else {
            // 获取目标类型
            mv.visitLdcInsn(Type.getType(Type.getDescriptor(targetType)));
            // 通过Reflects#to方法把String类型的数据转成目标类型的数据
            mv.visitMethodInsn(INVOKESTATIC, "cm/study/asm/setter/Reflects", "to", "(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;", false);
            // 把转换的结果转成目标类型
            mv.visitTypeInsn(CHECKCAST, Type.getInternalName(targetType));
        }

    }
}
