package com.practice.chat.dao;

import com.practice.chat.friendcircle.FriendCircle;

import java.sql.SQLException;
import java.util.List;

public interface FriendCircleDAO {
    List<FriendCircle> findFriendCircleBySenderNumber(String sender) throws SQLException;

    void addFriendCircle(FriendCircle friendCircle) throws SQLException;
}
