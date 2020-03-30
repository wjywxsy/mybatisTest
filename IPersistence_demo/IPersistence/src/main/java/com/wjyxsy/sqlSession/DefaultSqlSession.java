package com.wjyxsy.sqlSession;

import com.wjyxsy.pojo.Configuration;
import com.wjyxsy.pojo.MappedStatement;
import com.wjyxsy.utils.SqlType;

import java.lang.reflect.*;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    private SimpleExecutor simpleExecutor = new SimpleExecutor();

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {
        List<Object> list = simpleExecutor.query(configuration, configuration.getMappedStatementMap().get(statementId), params);
        return (List<E>) list;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {

        List<Object> objects = this.selectList(statementId, params);
        if (objects.size() == 1) {
            return (T) objects.get(0);
        } else {
            throw new RuntimeException("查询结果为空或者返回结果过多");
        }
    }

    @Override
    public int update(String statementId, Object... params) throws Exception {
        return simpleExecutor.update(configuration, configuration.getMappedStatementMap().get(statementId), params);
    }

    @Override
    public int insert(String statementId, Object... params) throws Exception {
        return update(statementId, params);
    }

    @Override
    public int delete(String statementId, Object... params) throws Exception {
        return update(statementId, params);
    }

    @Override
    public <T> T getMapper(Class<?> aClass) {

        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{aClass}, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
                // 1. statementId : namespace(class名)+id(方法名)
                // 2. 判断返回类型 调用selectList 或 selectOne()
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String statementId = className + "." + methodName;

                MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
                if (mappedStatement == null) {
                    return null;
                }
                String sqlType = mappedStatement.getSqlType();

                if (SqlType.select.equals(sqlType)) {
                    Type genericReturnType = method.getGenericReturnType();
                    if (genericReturnType instanceof ParameterizedType) {
                        List<Object> objects = selectList(statementId, args);
                        return objects;
                    }
                    return selectOne(statementId, args);
                }
                if (SqlType.insert.equals(sqlType)) {
                    return insert(statementId, args);
                }
                if (SqlType.update.equals(sqlType)) {
                    return update(statementId, args);
                }
                if (SqlType.delete.equals(sqlType)) {
                    return delete(statementId, args);
                }

                return null;
            }
        });
        return (T) proxyInstance;
    }
}
