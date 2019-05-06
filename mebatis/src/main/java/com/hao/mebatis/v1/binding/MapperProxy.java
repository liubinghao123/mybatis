package com.hao.mebatis.v1.binding;

import com.hao.mebatis.v1.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Keeper on 2019-05-06
 */
public class MapperProxy implements InvocationHandler {
    private SqlSession sqlSession;
    public MapperProxy(SqlSession sqlSession){
        this.sqlSession = sqlSession;
    }
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String statementId = method.getDeclaringClass().getName() + "." + method.getName();
        return sqlSession.selectOne(statementId,args);
    }
}
