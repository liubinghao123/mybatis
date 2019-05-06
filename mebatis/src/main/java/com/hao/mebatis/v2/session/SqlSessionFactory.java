package com.hao.mebatis.v2.session;

import com.hao.mebatis.v2.session.defaults.DefaultSqlSession;

/**
 * Created by Keeper on 2019-05-06
 */
public class SqlSessionFactory {
    private final Configuration configuration;

    public SqlSessionFactory(Configuration configuration){
        this.configuration = configuration;
    }

    public SqlSession openSession(){
        return new DefaultSqlSession(this.configuration,new Executor());
    }
}
