package com.hao.mybatis;

/**
 * Created by Keeper on 2019-04-28
 */
public interface UserMapper {
    User selectUser(int id);
    void updateUser(User user);
}
