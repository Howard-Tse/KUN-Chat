package com.practice.chat.user;

import com.practice.chat.dao.UserDAO;
import com.practice.chat.dbconnector.DBHelper;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    @Override
    public User findUserByNumber(String number) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        String sql = """
                select *
                from TB_User
                where Number = '%s'
                """;

        sql = sql.formatted(number);
        ResultSet set = helper.getStatement().executeQuery(sql);

        User user = null;
        if (set.next()) {
            InputStream inputStream = set.getBinaryStream(6);
            byte[] icon = null;
            if (inputStream != null) {
                try {
                    if (inputStream.available() > 0) {
                        icon = inputStream.readAllBytes();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String signature = set.getString(7);
            if (signature != null)
                signature = signature.trim();

            user = new User(set.getString(1), set.getString(2).trim(), set.getString(5),
                    set.getString(3).trim(), icon, set.getString(4), signature);
        }

        set.close();
        helper.closeAll();
        return user;
    }

    @Override
    public List<User> findAllUserByNickname(String nickname, boolean fuzzySearch) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        String sql = """
                select *
                from TB_User
                where Name = '%s'
                """;

        if (fuzzySearch)
            sql = sql.formatted("like", "'%" + nickname + "%'");
        else
            sql = sql.formatted("=", "'" + nickname + "'");

        ResultSet set = helper.getStatement().executeQuery(sql);

        List<User> list = new ArrayList<>();
        while (set.next()) {
            InputStream inputStream = set.getBinaryStream(6);
            byte[] icon = null;
            try {
                if (inputStream.available() > 0) {
                    icon = inputStream.readAllBytes();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            User user = new User(set.getString(1), set.getString(2), set.getString(5),
                    set.getString(3), icon, set.getString(4), set.getString(7));
            list.add(user);
        }
        set.close();
        helper.closeAll();
        return list;
    }

    @Override
    public void addUser(User user) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        String sql = """
                insert into TB_User(Number,Name,Password,Birthday,Sex,Icon) values(
                ?,?,?,?,?,?)
                """;
        try (PreparedStatement statement = helper.getConnection().prepareStatement(sql)) {
            statement.setString(1, user.getNumber());
            statement.setString(2, user.getNickname());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getBirthday());
            statement.setString(5, user.getSex());
            statement.setBinaryStream(6, new ByteArrayInputStream(user.getIcon()));
            statement.execute();
        }
        helper.closeAll();
    }

    @Override
    public void deleteUser(String number) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        String sql = """
                delete TB_User
                where Number = '%s'
                """;
        sql = sql.formatted(number);

        helper.getStatement().execute(sql);
        helper.closeAll();
    }

    @Override
    public void updateUser(String number, User newInfo) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        String sql = """
                update TB_User
                set Number = ?,Name = ?,Password = ?,Birthday = ?,Sex = ?,Icon = ?
                where Number = ?
                """;
        PreparedStatement preparedStatement = helper.preparedStatement(sql);
        preparedStatement.setString(1, newInfo.getNumber());
        preparedStatement.setString(2, newInfo.getNickname());
        preparedStatement.setString(3, newInfo.getPassword());
        preparedStatement.setString(4, newInfo.getBirthday());
        preparedStatement.setString(5, newInfo.getSex());
        preparedStatement.setBytes(6, newInfo.getIcon());
        preparedStatement.setString(7, number);

        helper.closeAll();
    }

    @Override
    public void deleteFriend(String number1, String number2) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        String sql = """
                delete TB_Friend
                where (number1 = '%s' and number2 = '%s') or (Number1 = '%s' and Number2 = '%s')
                """;
        sql = sql.formatted(number1, number2, number2, number1);

        helper.getStatement().execute(sql);
        helper.closeAll();
    }

    @Override
    public List<User> searchUserFriends(String number) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        String sql1 = """
                select number2
                from TB_Friend
                where number1 = '%s'
                """;
        sql1 = sql1.formatted(number);
        ResultSet set = helper.getStatement().executeQuery(sql1);

        List<String> friendNumberList = new ArrayList<>();
        while (set.next()) {
            friendNumberList.add(set.getString(1));
        }
        set.close();
        helper.closeAll();

        String sql2 = """
                select number1
                from TB_Friend
                where number2 = '%s'
                """;
        sql2 = sql2.formatted(number);
        try (ResultSet set1 = helper.getConnection().createStatement().executeQuery(sql2)) {
            while (set1.next()) {
                friendNumberList.add(set1.getString(1));
            }
        }
        helper.closeAll();

        List<User> friendList = new ArrayList<>();
        for (String n : friendNumberList) {
            friendList.add(findUserByNumber(n));
        }

        return friendList;
    }

    @Override
    public void updateUserSignature(String number, String signature) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        String sql = """
                update TB_User
                set Signature = '%s'
                where Number = '%s'
                """;
        sql = sql.formatted(signature, number);

        helper.getStatement().execute(sql);
    }

    @Override
    public void addFriend(String senderNumber, String receiverNumber) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        String sql = "insert into TB_FriendRequest(SenderNumber,ReceiverNumber) values('%s','%s')";
        sql = sql.formatted(senderNumber, receiverNumber);

        helper.getStatement().execute(sql);
    }

    @Override
    public void agreeAddFriend(String senderNumber, String receiverNumber) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        String sql = """
                delete TB_FriendRequest
                where SenderNumber = '%s' and ReceiverNumber = '%s'
                """;
        sql = sql.formatted(senderNumber, receiverNumber);

        helper.getStatement().execute(sql);

        sql = """
              insert into TB_Friend values('%s','%s')
              """;
        sql = sql.formatted(senderNumber, receiverNumber);

        helper.getStatement().execute(sql);

        helper.closeAll();
    }

    @Override
    public void disagreeAddFriend(String senderNumber, String receiverNumber) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        String sql = """
                delete TB_FriendRequest
                where SenderNumber = '%s' and ReceiverNumber = '%s'
                """;
        sql = sql.formatted(senderNumber, receiverNumber);

        helper.getStatement().execute(sql);
        helper.closeAll();
    }

    @Override
    public List<User> getFriendRequestsByReceiverNumber(String receiverNumber) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        String sql = """
                select SenderNumber
                from TB_FriendRequest
                where ReceiverNumber = '%s'
                """;
        sql = sql.formatted(receiverNumber);

        List<String> friendRequestsNumber = new ArrayList<>();
        ResultSet set = helper.getStatement().executeQuery(sql);
        while (set.next()) {
            friendRequestsNumber.add(set.getString(1));
        }
        set.close();
        helper.closeAll();

        List<User> friendRequests = new ArrayList<>();
        for (String number : friendRequestsNumber) {
            friendRequests.add(findUserByNumber(number));
        }
        return friendRequests;
    }
}
