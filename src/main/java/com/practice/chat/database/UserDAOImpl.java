package com.practice.chat.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {


    @Override
    public User findUserByNumber(String number) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        ResultSet set = helper.getStatement().executeQuery("select * from student");
        User user = null;
        if(set.first()) {
            user = new User();
            set.getString(1);
        }

        return user;
    }

    @Override
    public List<User> findAllUserByNickname(String nickname) {
        return null;
    }

    @Override
    public List<User> fuzzyFindAllUserByNickName(String key) {
        return null;
    }

    @Override
    public void addUser(User user) {

    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public void updateUser(String number, User newInfo) {

    }

    @Override
    public void deleteFriend(User user, String number) {

    }

    @Override
    public List<User> executeSQL(String sql) throws SQLException {
        List<User> users = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance();

        ResultSet set = helper.getStatement().executeQuery(sql);
        while(set.next()) {

        }

        return users;
    }

    @Override
    public List<String> searchUserFriends(User user) {
        return null;
    }
}
