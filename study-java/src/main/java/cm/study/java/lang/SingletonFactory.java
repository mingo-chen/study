package cm.study.java.lang;

public enum  SingletonFactory {

    XXXHolder(new XXX()),
    YYYHolder(new Object()),
    ;

    private Object instance;

    private SingletonFactory(Object instance) {
        this.instance = instance;
    }

    public <T> T getInstance() {
        return (T)instance;
    }
}
