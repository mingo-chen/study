package cm.study.java.lang.reactive.imitate;

public class SinkImpl<T, T_OUT> implements Sink<T> {

    protected Sink<T_OUT> downstream;

    public SinkImpl(Sink<T_OUT> downstream) {
        this.downstream = downstream;
    }

    @Override
    public void accept(T each) {
//        data.add(each);
        System.out.println("----> empty accept: " + each);
    }
}
