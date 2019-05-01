package com.hao.mybatis;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.BaseExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.*;

/**
 * Created by Keeper on 2019-05-01
 * 自定义RowBounds物理分页插件
 */
@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "query",
        args = {Statement.class , ResultHandler.class}
)})
public class PreparedStatementLoggerInterceptor implements Interceptor {
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        System.out.println("executor sql : " + boundSql.getSql());
        Date startTime = new Date();
        try{
            return invocation.proceed();
        }finally {
            Date endTime  = new Date();
            System.out.println("总共花费时间：" + (endTime.getTime() - startTime.getTime())+ "毫秒");
        }
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    public void setProperties(Properties properties) {

    }
}
