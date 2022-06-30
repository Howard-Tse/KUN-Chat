package com.practice.chat.dao;

import com.practice.chat.user.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    User findUserByNumber(String number) throws SQLException;

    List<User> findAllUserByNickname(String nickname, boolean fuzzySearch) throws SQLException;

    void addUser(User user) throws SQLException;

    void deleteUser(String number) throws SQLException;

    void updateUser(String number, User newInfo) throws SQLException;

    void deleteFriend(String number1, String number2) throws SQLException;

    List<User> searchUserFriends(String number) throws SQLException;

    void updateUserSignature(String number, String signature) throws SQLException;

    void addFriend(String senderNumber, String receiverNumber) throws SQLException;

    void agreeAddFriend(String senderNumber, String receiverNumber) throws SQLException;

    void disagreeAddFriend(String senderNumber, String receiverNumber) throws SQLException;

    List<User> getFriendRequestsByReceiverNumber(String receiverNumber) throws SQLException;
}
