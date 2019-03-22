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
        FormInvocation form = (FormInvocation) invocation;

        if(form.getName().equals("doSomething")) {
            if(form.size() == 1) {
                return target.doSomething(form.get("id"));
            } else if (form.size() == 3) {
                return target.doSomething(form.get("id"), form.get("name"),
                        Integer.valueOf(form.get("age")), Integer.valueOf(form.get("size")));
            } else {
                throw new RuntimeException("method not exist!");
            }

        } else if (form.getName().equals("otherThing")) {
            if (form.size() == 2) {
                target.otherThing(form.get("id"), form.get("name"));
                return null;

            } else {
                throw new RuntimeException("method not exist!");
            }
        } else{
            throw new RuntimeException("method not exist!");
        }
    }
}
