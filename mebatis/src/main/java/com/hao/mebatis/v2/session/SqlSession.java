package com.hao.mebatis.v2.session;

/**
 * Created by Keeper on 2019-05-06
 */
public interface SqlSession {
    <T> T getMapper(Class clazz);

    <T> T selectOne(String statementId);

    <T> T selectOne(String statementId, Object parameters);
}
