package cm.study.asm.invoker;

import org.testng.annotations.Test;

import java.nio.channels.ServerSocketChannel;

import static org.testng.Assert.*;

public class InvokerByAsmTest {

    @Test
    public void testWrapper_get() {
        Service real = new Service();

        InvokerByAsm asm = new InvokerByAsm();
        Invoker invoker = asm.wrapper(real);

        FormInvocation params = new FormInvocation();
        params.setMethodName("get");
        params.with(1234);
        Object result = invoker.call(params);

        System.out.println("--> " + result);
    }

    @Test
    public void testWrapper_save() {
        Service real = new Service();

        InvokerByAsm asm = new InvokerByAsm();
        Invoker invoker = asm.wrapper(real);

        FormInvocation params = new FormInvocation();
        params.setMethodName("save");
        params.with(1234, "mz", "zhuhai");
        Object result = invoker.call(params);

        System.out.println("--> " + result);
    }
}