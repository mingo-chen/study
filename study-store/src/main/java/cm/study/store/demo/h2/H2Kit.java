package cm.study.store.demo.h2;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.h2.jdbcx.JdbcConnectionPool;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class H2Kit {

    public static void initH2() {
        String dbPath = "./study-store/src/test/resources/h2db";
        JdbcConnectionPool h2Pool = JdbcConnectionPool.create("jdbc:h2:" + dbPath, "sa", "");
        h2Pool.setMaxConnections(5);

        try {
            Connection conn = h2Pool.getConnection();
            // 执行DDL SQL
            Statement statement = conn.createStatement();
            List<String> initDdlSqls = loadInitDDLSql();
            for (String sql : initDdlSqls) {
                System.out.println("--> " + sql);
                statement.execute(sql);
            }

            List<String> initDataSqls = loadInitDataSql();
            for (String sql : initDataSqls) {
                System.out.println("--> " + sql);
                statement.execute(sql);
            }

            // check
//            DatabaseMetaData metaData = conn.getMetaData();
//            ResultSet tables = metaData.getTables(null, null, null, null);
//            System.out.println("==> " + tables);
//            while (tables.next()) {
//                System.out.println("==> " + tables);
//            }
//
//            tables.close();
            statement.close();
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();

        } finally {
        }
    }

    public static List<String> loadInitDDLSql() {
        String initSql = "/h2_init.sql";
        return loadSql(initSql);
    }

    public static List<String> loadInitDataSql() {
        String initSql = "/init_data.sql";
        return loadSql(initSql);
    }

    public static List<String> loadSql(String sqlPath) {
        InputStream inputStream = H2Kit.class.getResourceAsStream(sqlPath);

        try {
            List<String> lines = IOUtils.readLines(inputStream, "UTF-8");
            String all = StringUtils.join(lines, ">#<");
            String format = all.replace(">#<", "\n");
            return Arrays.asList(StringUtils.split(format, ";"));

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
