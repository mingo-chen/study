package cm.study.java.lang;

public class ThreadLocalStudy {

    private ThreadLocal<String> nameHolder = new ThreadLocal();
    private ThreadLocal<String> addressHolder = new ThreadLocal();

    public ThreadLocalStudy(String name, String address) {
        nameHolder.set(name);
        addressHolder.set(address);
    }

    @Override
    public String toString() {
        return "ThreadLocalStudy{" +
               "name=" + nameHolder.get() +
               ", address=" + addressHolder.get() +
               '}';
    }

    public static void main(String[] args) {
        ThreadLocalStudy study = new ThreadLocalStudy("cm", "ah");
        System.out.println("--> " + study);
    }
}
