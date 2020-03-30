package com.wjyxsy.sqlSession;

import java.util.List;

public interface SqlSession {

    public <E> List<E> selectList(String statementId, Object... params) throws Exception;

    public <T> T selectOne(String statementId, Object... params) throws Exception;

    public int update(String statementId, Object... params) throws Exception;

    public int insert(String statementId, Object... params) throws Exception;

    public int delete(String statementId, Object... params) throws Exception;

    public <T> T getMapper(Class<?> aClass);

}
