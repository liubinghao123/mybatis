package com.hao.mybatis;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;

/**
 * Created by Keeper on 2019-04-28
 */
public class SimpleTest {
   @Test
    public void getUserByOne(){
       InputStream is = this.getClass().getResourceAsStream("/mybatis-config.xml");
       SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
       SqlSession session = sqlSessionFactory.openSession();
       User user = session.selectOne("com.hao.mybatis.UserMapper.selectUser",1);
       session.close();
       System.out.println(user);
    }

    @Test
    public void getUserByOne2(){
        InputStream is = this.getClass().getResourceAsStream("/mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = userMapper.selectUser(1);
        session.close();
        System.out.println(user);
    }
    //不同会话不共用一级缓存
    @Test
    public void getUserByOneForCache(){
        InputStream is = this.getClass().getResourceAsStream("/mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = userMapper.selectUser(1);
        System.out.println(user);
        //第二次查询是否命中缓存
        SqlSession session2 = sqlSessionFactory.openSession();
        UserMapper userMapper2 = session2.getMapper(UserMapper.class);
        User user2 = userMapper2.selectUser(1);
        System.out.println(user2);
    }

    //相同会话共用一级缓存
    //将localCacheStorage作用域设置为STATEMENT相当于关闭了一级缓存
    @Test
    public void getUserByOneForCache2(){
        InputStream is = this.getClass().getResourceAsStream("/mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = userMapper.selectUser(1);
        System.out.println(user);
        //第二次查询是否命中缓存
        UserMapper userMapper2 = session.getMapper(UserMapper.class);
        User user2 = userMapper2.selectUser(1);
        System.out.println(user2);
    }
    //更新会让缓存的对象失效
    @Test
    public void cacheInvalid(){
        InputStream is = this.getClass().getResourceAsStream("/mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = userMapper.selectUser(1);
        System.out.println(user);
        //更新数据
        User updateUser = new User();
        updateUser.setId(1);
        updateUser.setName("陈冠希");
        userMapper.updateUser(updateUser);
        session.commit();
        //第二次查询是否命中缓存
        UserMapper userMapper2 = session.getMapper(UserMapper.class);
        User user2 = userMapper2.selectUser(1);
        System.out.println(user2);
    }
    //一级缓存导致的数据脏读
    @Test
    public void cacheInvalid2(){
        InputStream is = this.getClass().getResourceAsStream("/mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = userMapper.selectUser(1);
        System.out.println("会话1查询的信息"+user);

        //第二次查询是否命中缓存
        SqlSession session2 = sqlSessionFactory.openSession();
        UserMapper userMapper2 = session2.getMapper(UserMapper.class);
        //更新数据
        User updateUser = new User();
        updateUser.setId(1);
        updateUser.setName("谢霆锋2");
        userMapper2.updateUser(updateUser);
        session2.commit();
        System.out.println("会话2修改了数据"+ userMapper2.selectUser(1));

        user = userMapper.selectUser(1);
        System.out.println("会话1查询的信息"+user);
    }
    //开启二级缓存，可跨会话共享
    @Test
    public void getUserForSecondCache(){
        InputStream is = this.getClass().getResourceAsStream("/mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = userMapper.selectUser(1);
        //事务需要提交才能共享
        session.commit();
        System.out.println(user);

        //第二次查询是否命中缓存
        SqlSession session2 = sqlSessionFactory.openSession();
        UserMapper userMapper2 = session2.getMapper(UserMapper.class);
        User user2 = userMapper2.selectUser(1);
        System.out.println(user2);
    }

    @Test
    public  void updateUser(){
        InputStream is = SimpleTest.class.getResourceAsStream("/mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User updateUser = new User();
        updateUser.setId(1);
        updateUser.setName("谢霆锋2");
        userMapper.updateUser(updateUser);
        session.commit();
    }
    @Test
    public void selectUserByPage(){
        InputStream is = SimpleTest.class.getResourceAsStream("/mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        PageHelper.startPage(1,2);
        User user = new User();
        userMapper.selectList(user);
        session.commit();
    }

    @Test
    public void selectUserByPageForRowBounds(){
        InputStream is = SimpleTest.class.getResourceAsStream("/mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sqlSessionFactory.openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        RowBounds rowBounds = new RowBounds(1,2);
        User user = new User();
        userMapper.selectListForRowBounds(user,rowBounds);
        session.commit();
    }
}
