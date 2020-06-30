package com.hao.mebatis.v2.session;

import com.hao.mebatis.v2.test.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Keeper on 2019-05-06
 */
public class Executor {
    public <T> List<T> query(String statementId, Object parameters){
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            Properties properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("/db.properties"));
            Class.forName(properties.get("driver")+"");
            connection = DriverManager.getConnection(properties.get("url")+"",properties.get("username")+"",properties.get("password")+"");

            Properties properties2 = new Properties();
            properties2.load(this.getClass().getResourceAsStream("/mapper.properties"));
            pstm = connection.prepareStatement(properties2.get(statementId)+"");
            Object[] args = (Object[]) parameters;
            pstm.setInt(1, (Integer) args[0]);
            rs = pstm.executeQuery();
            List<User> users = new ArrayList<User>();
            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));;
                user.setName(rs.getString("name"));
                user.setAge(rs.getInt("age"));
                users.add(user);
            }
            return (List<T>) users;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(rs!=null){
                    rs.close();
                }
                if(pstm!=null){
                    pstm.close();
                }
                if(connection!=null){
                    connection.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }

        }
        return null;
    }
}
