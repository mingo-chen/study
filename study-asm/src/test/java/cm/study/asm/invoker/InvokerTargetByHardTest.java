package cm.study.asm.invoker;

import cm.study.asm.common.Target;
import org.testng.reporters.jq.Main;

import java.lang.reflect.Field;

import static org.testng.Assert.*;

public class InvokerTargetByHardTest implements Invoker {

    private Target target;

    @Override
    public Object call(Invocation var1) {
        FormInvocation var2 = (FormInvocation)var1;
        String var3 = var2.getMethodName();
        int var4 = var2.size();

        /*if (var3.equals("doSomething") && var4 == 4) {
            return this.target.doSomething((String)var2.get(0), (String)var2.get(1), (int)var2.get(2), (int)var2.get(3));
        } else */if (var3.equals("doSomething") && var4 == 1) {
            return this.target.doSomething((String)var2.get(0));
        } else if (var3.equals("otherThing") && var4 == 2) {
            this.target.otherThing((String)var2.get(0), (String)var2.get(1));
            return null;
        } else {
            throw new RuntimeException("method not exist!");
        }
    }

    public static void main(String[] args) throws Exception {
        InvokerTargetByHardTest test = new InvokerTargetByHardTest();
        Field target = test.getClass().getDeclaredField("target");
        target.setAccessible(true);
        target.set(test, new Target());

        FormInvocation param = new FormInvocation();
        param.setMethodName("otherThing");
        param.with("AAA", "cm");
        System.out.println("--> " + test.call(param));
    }
}