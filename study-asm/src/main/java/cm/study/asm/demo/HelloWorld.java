package cm.study.asm.demo;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.URL;

public class HelloWorld {

    public void sayHello() {
        ClassWriter clazzWriter = new ClassWriter(0);
        String className = "cm/HelloX$A";
        clazzWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className,
                null, "java/lang/Object", null);

        MethodVisitor initVisitor = clazzWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        initVisitor.visitCode();
        initVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        initVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        initVisitor.visitInsn(Opcodes.RETURN);
        initVisitor.visitMaxs(1, 1);
        initVisitor.visitEnd();

        MethodVisitor helloVisitor = clazzWriter.visitMethod(Opcodes.ACC_PUBLIC, "sayHello", "()V", null, null);
        helloVisitor.visitCode();
        helloVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        helloVisitor.visitLdcInsn("hello world!");
        helloVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        helloVisitor.visitInsn(Opcodes.RETURN);
        helloVisitor.visitMaxs(2, 1);
        helloVisitor.visitEnd();

        clazzWriter.visitEnd();

        byte[] clazzData = clazzWriter.toByteArray();
        try {
            File source = new File("/Users/ahcming/workspace/github/study/study-asm/target/classes/cm/HelloX$A.class");
            FileOutputStream out = new FileOutputStream(source);
            out.write(clazzData);
            out.close();

            URL sourceUrl = new URL("file:/Users/ahcming/workspace/github/study/study-asm/target/classes");
//            URL sourceUrl = source.toURI().toURL();
            AsmClassLoader loader = new AsmClassLoader(new URL[]{sourceUrl});
            for(URL url : loader.getURLs()) {
                System.out.println("url: " + url);
            }

            Class<?> realClazz = loader.findClass("cm.HelloX$A");
            System.out.println("----> " + realClazz);
            Method[] methods = realClazz.getMethods();

            for (Method method : methods) {
                System.out.println("--> " + method);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static void main(String[] args) {
        HelloWorld me = new HelloWorld();
        me.sayHello();
    }
}
