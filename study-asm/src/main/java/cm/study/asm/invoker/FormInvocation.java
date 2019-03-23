package cm.study.asm.invoker;

import java.util.Arrays;

public class FormInvocation implements Invocation {

    private String apiName;

    private String methodName;

    private Object[] params;

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public void with(Object... args) {
        params = args;
    }

    public int size() {
        if (this.params == null) {
            return 0;
        } else {
            return this.params.length;
        }
    }

    public <T> T get(int index) {
        return (T) this.params[index];
    }

    @Override
    public String toString() {
        return "FormInvocation{" +
               "apiName='" + apiName + '\'' +
               ", methodName='" + methodName + '\'' +
               ", params=" + Arrays.toString(params) +
               '}';
    }
}
