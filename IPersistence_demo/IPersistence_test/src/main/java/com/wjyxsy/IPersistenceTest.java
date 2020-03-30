package com.wjyxsy;

import com.wjyxsy.dao.UserDao;
import com.wjyxsy.entity.User;
import com.wjyxsy.io.Resources;
import com.wjyxsy.sqlSession.SqlSession;
import com.wjyxsy.sqlSession.SqlSessionFactory;
import com.wjyxsy.sqlSession.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class IPersistenceTest {

    @Test
    public void test () throws Exception {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");

        SqlSessionFactory sqlSessionFactory =
                new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();


        User user = new User();
        user.setId(4);
        user.setUsername("");
//        User userResult = sqlSession.selectOne("com.wjyxsy.dao.UserDao.selectOne", user);
//        System.out.println(userResult);

//        List<User> objects = sqlSession.selectList("com.wjyxsy.dao.UserDao.selectList", user);
//        for (User object : objects) {
//            System.out.println(object);
//        }

        UserDao userDao = sqlSession.getMapper(UserDao.class);
//        User users = userDao.selectOne(user);
//        System.out.println(users);

//        user.setUsername("赵六");
//        int i = userDao.insertUser(user);
//        user.setUsername("张三123");
        int i = userDao.deleteUser(user);
        System.out.println(i);

//        for (User object : users) {
//            System.out.println(object);
//        }
    }

    public static void main(String[] args) throws Exception {
        IPersistenceTest test = new IPersistenceTest();
        test.test();
    }
}
