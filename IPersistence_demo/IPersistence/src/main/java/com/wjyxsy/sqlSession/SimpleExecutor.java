package com.wjyxsy.sqlSession;

import com.wjyxsy.config.BoundSql;
import com.wjyxsy.pojo.Configuration;
import com.wjyxsy.pojo.MappedStatement;
import com.wjyxsy.utils.GenericTokenParser;
import com.wjyxsy.utils.ParameterMapping;
import com.wjyxsy.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {

    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {

        // 1.注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();

        // 2.转换SQL 对#{}进行解析存储 以?占位符代替，存储参数值
        String sql = mappedStatement.getSqlText();
        BoundSql boundSql = getBoundSql(sql);

        // 3.预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        // 4.获取参数
        String paramterType = mappedStatement.getParamterType();

        if (paramterType != null) {
            Class<?> paramterClass = getClassType(paramterType);
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                String content = parameterMapping.getContent();
                Field field = paramterClass.getDeclaredField(content);
                field.setAccessible(true);
                Object o = field.get(params[0]);

                preparedStatement.setObject(i+1,o);
            }
        }

        // 5.执行sql

        ResultSet resultSet = preparedStatement.executeQuery();

        // 6.封装结果集
        String resultType = mappedStatement.getResultType();
        Class<?> classType = getClassType(resultType);
        ArrayList<Object> objects = new ArrayList<>();

        while (resultSet.next()) {
            // 实例化返回参数
            Object o = classType.newInstance();
            // 数据库查询元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                // 字段名
                String columnName = metaData.getColumnName(i);
                // 字段值
                Object value = resultSet.getObject(columnName);

                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, classType);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, value);
            }
            objects.add(o);
        }

        return (List<E>) objects;
    }

    @Override
    public int update(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        Connection connection = configuration.getDataSource().getConnection();

        BoundSql boundSql = getBoundSql(mappedStatement.getSqlText());

        String sqlText = boundSql.getSqlText();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlText);

        String paramterType = mappedStatement.getParamterType();

        Class<?> parameterClass = getClassType(paramterType);

        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            String content = parameterMapping.getContent();
            Field declaredField = parameterClass.getDeclaredField(content);
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);
            preparedStatement.setObject(i+1,o);
        }
        int i = preparedStatement.executeUpdate();
        return i;
    }

    private Class<?> getClassType(String paramterType) throws ClassNotFoundException {
        if (paramterType != null) {
            return Class.forName(paramterType);
        }
        return null;
    }

    private BoundSql getBoundSql(String sql) {
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String sqlText = genericTokenParser.parse(sql);

        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        return new BoundSql(sqlText, parameterMappings);
    }
}
