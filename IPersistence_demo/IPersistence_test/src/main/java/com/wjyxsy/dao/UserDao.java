package com.wjyxsy.dao;

import com.wjyxsy.entity.User;

import java.util.List;

public interface UserDao {

    User selectOne(User user);

    List<User> selectList();

    int insertUser(User user);

    int updateUser(User user);

    int deleteUser(User user);
}
