package cm.study.java.lang;

import java.sql.Connection;
import java.sql.DriverManager;

public class ClassLoaderStudy {

    public static void main(String[] args) {
        try {
//            Class<?> driver = Class.forName("com.mysql.cj.jdbc.Driver");
//            System.out.println("--> " + driver);

            String url = "jdbc:mysql://localhost:3306/cm-storylocker?characterEncoding=UTF-8";
            Connection connection = DriverManager.getConnection(url, "root", "password");
            System.out.println("--> " + connection);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
