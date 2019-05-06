package com.hao.mebatis.v2.test;

import com.hao.mebatis.v2.session.SqlSession;
import com.hao.mebatis.v2.session.SqlSessionFactory;
import com.hao.mebatis.v2.session.SqlSessionFactoryBuilder;

/**
 * Created by Keeper on 2019-05-06
 */
public class MebatisTest {
    public static void main(String[] args) {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuilder.build(MebatisTest.class.getResourceAsStream(""));
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.selectUserById(1);
        System.out.println(user);
    }
}
