package cm.study.asm.invoker;

import cm.study.asm.common.Target;

public class InvokerTargetByHard implements Invoker {

    private Target target;

    public InvokerTargetByHard() {

    }

    public InvokerTargetByHard(Target target) {
        this.target = target;
    }

    @Override
    public Object call(Invocation invocation) {
        FormInvocation form = (FormInvocation) invocation;  // L0
        String methodName = form.getMethodName();           // L1
        int size = form.size();                             // L2

        if (methodName.equals("doSomething") && size == 4) { // L4
            return target.doSomething(form.get(0), form.get(1), form.get(2), form.get(3));
        }

        if(methodName.equals("doSomething") && size == 1) { // L3
            return target.doSomething(form.get(0));
        }

        if (methodName.equals("otherThing") && size == 2) {
            target.otherThing(form.get(0), form.get(1));
            return null;
        }

        throw new RuntimeException("method not exist!");

    }
}
