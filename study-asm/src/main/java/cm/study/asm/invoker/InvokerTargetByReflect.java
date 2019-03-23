package cm.study.asm.invoker;

import cm.study.asm.common.ReflectUtil;
import cm.study.asm.common.Target;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class InvokerTargetByReflect implements Invoker {

    private Target target;

    private Map<String, Method> methodMap = new HashMap<>();

    public InvokerTargetByReflect() {

    }

    public InvokerTargetByReflect(Target target) {
        this.target = target;
        Method[] methods = target.getClass().getDeclaredMethods();

        for (Method method : methods) {
            // 简单点: key = methodName + "_" + size(params)
            String key = String.format("%s_%d", method.getName(), method.getParameterTypes().length);
            methodMap.put(key, method);
        }
    }

    @Override
    public Object call(Invocation invocation) {
        FormInvocation form = (FormInvocation) invocation;

        String methodId = String.format("%s_%d", form.getMethodName(), form.size());
        Method method = methodMap.get(methodId);
        if (null == method) {
            throw new RuntimeException("method not exist!");
        }

        try {
            return method.invoke(target, form.getParams());
        } catch (Exception e) {
            throw new RuntimeException("method invoke error");
        }
    }
}
