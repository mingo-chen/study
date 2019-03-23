package cm.study.asm.invoker;

import cm.study.asm.common.Target;

import java.util.HashMap;
import java.util.Map;

public class InvokerFactory {

    private static Map<Class<?>, Invoker> invokerMap = new HashMap<>();

    /**
     * 实际中, 可能中 new Target() 可能是通过IOC之类容器注入
     */
    public static void scan(Class<?> type, Object instance) {
//        invokerMap.put(type, new InvokerTargetByHard((Target) instance));
//        invokerMap.put(type, new InvokerTargetByReflect((Target) instance));
        invokerMap.put(type, new InvokerByAsm().wrapper(instance));
    }

    public static Invoker get(Class<?> clazz) {
        return invokerMap.get(clazz);
    }

}
