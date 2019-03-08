package cm.study.java.lang;

public class CharsetStudy {

    public void codec() {
        char zhchar = '人';
        char unicodeChar = 20154;

        System.out.printf("--> %s, %s \n", zhchar, unicodeChar);
    }

    public static void main(String[] args) {
        CharsetStudy study = new CharsetStudy();
        study.codec();

    }
}
