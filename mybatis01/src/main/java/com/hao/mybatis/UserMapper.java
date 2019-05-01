package com.hao.mybatis;

import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Keeper on 2019-04-28
 */
public interface UserMapper {
    User selectUser(int id);
    List<User> selectList(User user);
    List<User> selectListForRowBounds(User user , RowBounds rowBounds);
    void updateUser(User user);
}
