package com.hao.mybatis;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Keeper on 2019-05-01
 * 自定义RowBounds物理分页插件
 */
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
)})
public class RowBoundsInterceptor implements Interceptor {
    public Object intercept(Invocation invocation) throws Throwable {
        Executor executor = (Executor) invocation.getTarget();
        Method method = invocation.getMethod();
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        //被代理方法的参数
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler resultHandler = (ResultHandler) args[3];
        CacheKey cacheKey = null;
        BoundSql boundSql = null;
        if(args.length == 4){
            boundSql = mappedStatement.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(mappedStatement,parameter,rowBounds,boundSql);
        }else{
            boundSql = (BoundSql) args[4];
            cacheKey = (CacheKey) args[5];
        }
        //不是默认的，则说明使用RowBound进行分页，将逻辑分页改为物理分页
        if(!(rowBounds.getOffset() >= 0 && rowBounds.getLimit() == 2147483647)){
            String pageSql = boundSql.getSql() + " limit ?,?";
            //参数二次处理，暂时不处理Collection
            Map<String,Object> paramsterMap = new HashMap<String, Object>();
            if(parameter instanceof Map){
                paramsterMap.putAll((Map<? extends String, ?>) parameter);
            }else{
                Class<?> pClazz =  parameter.getClass();
                Field[] fields = pClazz.getDeclaredFields();
                for(Field field : fields){
                    field.setAccessible(true);
                    paramsterMap.put(field.getName(),field.get(parameter));
                }
            }
            paramsterMap.put("offset",rowBounds.getOffset()-1);
            paramsterMap.put("limit",rowBounds.getLimit());

            parameter = paramsterMap;

            //重新构建参数和sql语句中的占位符
            List<ParameterMapping> mappingList = new ArrayList<ParameterMapping>();
            mappingList.addAll(boundSql.getParameterMappings());

            mappingList.add((new ParameterMapping.Builder(mappedStatement.getConfiguration(), "offset", Integer.class)).build());
            mappingList.add((new ParameterMapping.Builder(mappedStatement.getConfiguration(), "limit", Integer.class)).build());

            boundSql = new BoundSql(mappedStatement.getConfiguration(),pageSql,mappingList,parameter);
        }
        List result = executor.query(mappedStatement,parameter,RowBounds.DEFAULT,resultHandler,cacheKey,boundSql);
        return result;
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    public void setProperties(Properties properties) {

    }
}
