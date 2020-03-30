package com.wjyxsy.sqlSession;

import com.wjyxsy.pojo.Configuration;
import com.wjyxsy.pojo.MappedStatement;

import java.sql.SQLException;
import java.util.List;

public interface Executor {

    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, Exception;

    public int update(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

}
