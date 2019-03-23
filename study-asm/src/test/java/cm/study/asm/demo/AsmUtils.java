package cm.study.asm.demo;

import cm.study.asm.invoker.Invocation;
import org.objectweb.asm.Type;
import org.testng.annotations.Test;

public class AsmUtils {

    @Test
    public void test() {
        System.out.println(Type.getDescriptor(Invocation.class));
        System.out.println(Type.getDescriptor(Object.class));
        System.out.println(Type.getMethodDescriptor(Type.getType(Object.class), Type.getType(Invocation.class)));
    }
}
