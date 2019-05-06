package com.hao.mebatis.v1.session;

import com.hao.mebatis.v1.binding.MapperProxy;
import com.hao.mebatis.v1.session.defaults.DefaultSqlSession;

import java.lang.reflect.Proxy;

/**
 * Created by Keeper on 2019-05-06
 */
public class Configuration {
    /**
     * 创建DefaultSession的代理对象
     * @param clazz
     * @param defaultSqlSession
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class clazz, DefaultSqlSession defaultSqlSession) {
        MapperProxy mapperProxy = new MapperProxy(defaultSqlSession);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},mapperProxy);
    }
}
