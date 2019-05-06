package com.hao.mebatis.v1.session;

import java.io.InputStream;

/**
 * Created by Keeper on 2019-05-06
 */
public class SqlSessionFactoryBuilder {
    public static SqlSessionFactory build(InputStream inputStream){
        return new SqlSessionFactory(new Configuration());
    }
}
