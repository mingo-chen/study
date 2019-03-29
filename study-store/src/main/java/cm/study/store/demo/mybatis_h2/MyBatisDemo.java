package cm.study.store.demo.mybatis_h2;

import cm.study.store.demo.h2.H2Kit;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class MyBatisDemo {

    public static void main(String[] args) throws Exception {
        H2Kit.initH2();

        String config = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(config);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sessionFactory.openSession();

        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.selectUser(1);
            System.out.println("--> " + user);

        } finally {
            sqlSession.close();
        }
    }

}
