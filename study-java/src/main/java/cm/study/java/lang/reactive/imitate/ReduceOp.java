package cm.study.java.lang.reactive.imitate;

public class ReduceOp<T> implements Sink<T> {
    public T total = null;
    private Func2<T, T, T> reduceFunc;

    public ReduceOp(Func2<T, T, T> reduceFunc) {
        this.reduceFunc = reduceFunc;
    }

    @Override
    public void accept(T each) {
        if(total == null) {
            total = each;
        } else {
            total = reduceFunc.apply(total, each);
        }

        System.out.printf("---> reduce: %s, total: %s \n", each, total);
    }
}
