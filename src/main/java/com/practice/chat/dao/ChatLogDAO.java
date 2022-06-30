package com.practice.chat.dao;

import com.practice.chat.chatlog.ChatLog;

import java.sql.SQLException;
import java.util.List;

public interface ChatLogDAO {
    List<ChatLog> getChatLogs(String sender, String receiver) throws SQLException;
    void addChatLog(ChatLog chatLog) throws SQLException;
    void deleteChatLog(int chatLogId) throws SQLException;

}
