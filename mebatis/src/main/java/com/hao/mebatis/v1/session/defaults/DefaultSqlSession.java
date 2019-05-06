package com.hao.mebatis.v1.session.defaults;

import com.hao.mebatis.v1.session.Configuration;
import com.hao.mebatis.v1.session.Executor;
import com.hao.mebatis.v1.session.SqlSession;

import java.util.List;

/**
 * Created by Keeper on 2019-05-06
 */
public class DefaultSqlSession implements SqlSession {
    private final Configuration configuration;
    private final Executor executor;

    public  DefaultSqlSession (Configuration configuration,Executor executor){
        this.configuration = configuration;
        this.executor = executor;
    }

    public <T> T getMapper(Class clazz) {
        return this.configuration.getMapper(clazz,this);
    }

    public <T> List<T> selectList(String statementId,Object parameters){
        return this.executor.query(statementId,parameters);
    }

    public <T> T selectOne(String statementId) {
        return selectOne(statementId,null);
    }

    public <T> T selectOne(String statementId,Object parameters){
        List<T> lists = this.selectList(statementId,parameters);
        if(lists!=null && lists.size() > 0){
            return lists.get(0);
        }
        return null;
    }
}
