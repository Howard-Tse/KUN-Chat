package com.practice.chat.chatlog;

import com.practice.chat.dao.ChatLogDAO;
import com.practice.chat.dbconnector.DBHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatLogImpl implements ChatLogDAO {

    @Override
    public List<ChatLog> getChatLogs(String sender, String receiver) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        String sql = """
                select *
                from TB_ChatLog
                where (SenderNumber = '%s' and ReceiverNumber = '%s') or (SenderNumber = '%s' and ReceiverNumber = '%s')
                """;
        sql = sql.formatted(sender, receiver, receiver, sender);

        ResultSet resultSet = helper.getStatement().executeQuery(sql);
        List<ChatLog> list = new ArrayList<>();

        while (resultSet.next()) {
            list.add(new ChatLog(resultSet.getInt(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)));
        }
        resultSet.close();

        return list;
    }

    @Override
    public void addChatLog(ChatLog chatLog) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        String sql = """
                insert into TB_ChatLog(SenderNumber,ReceiverNumber,Content,SendTime) values(
                '%s','%s','%s','%s')
                """;
        sql = sql.formatted(chatLog.getSenderNumber(), chatLog.getReceiverNumber(),
                chatLog.getContent(), chatLog.getSendTime());

        helper.getStatement().execute(sql);
    }

    @Override
    public void deleteChatLog(int chatLogId) throws SQLException {
        DBHelper helper = DBHelper.getInstance();

        String sql = """
                delete TB_ChatLog
                where ChatLogID = %d
                """;
        sql = sql.formatted(chatLogId);

        helper.getStatement().execute(sql);
    }
}
