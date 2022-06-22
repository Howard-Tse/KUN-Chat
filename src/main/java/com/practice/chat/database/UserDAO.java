package com.practice.chat.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface UserDAO {
    User findUserByNumber(String number) throws SQLException;
    List<User> findAllUserByNickname(String nickname);
    List<User> fuzzyFindAllUserByNickName(String key);
    void addUser(User user);
    void deleteUser(User user);
    void updateUser(String number, User newInfo);
    void deleteFriend(User user, String number);
    List<User> executeSQL(String sql) throws SQLException;
    List<String> searchUserFriends(User user);

}
