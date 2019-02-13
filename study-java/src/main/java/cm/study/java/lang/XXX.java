package cm.study.java.lang;

public class XXX {

    XXX() {
        System.out.println("---create xxx");
    }

    public static XXX getInstace() {
        return SingletonFactory.XXXHolder.getInstance();
    }

    private String name = "cm";

    public static void main(String[] args) {
        System.out.println("--> " + XXX.getInstace().name);
        System.out.println("--> " + XXX.getInstace().name);
        System.out.println("--> " + XXX.getInstace().name);
    }
}
