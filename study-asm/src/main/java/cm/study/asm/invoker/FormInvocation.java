package cm.study.asm.invoker;

import java.util.LinkedHashMap;
import java.util.Map;

public class FormInvocation implements Invocation {

    private String name;

    private Map<String, String> params = new LinkedHashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void with(String key, String value) {
        params.put(key, value);
    }

    public int size() {
        return params.size();
    }

    public String get(String key) {
        return params.get(key);
    }

    public Map<String, String> gets() {
        return params;
    }

    @Override
    public String toString() {
        return "FormInvocation{" +
               "name='" + name + '\'' +
               ", params=" + params +
               '}';
    }
}
