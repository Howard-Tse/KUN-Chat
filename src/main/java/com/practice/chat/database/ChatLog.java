package com.practice.chat.database;

import java.time.LocalDateTime;

public class ChatLog {
    private String chatLogID; //聊天记录编号
    private String senderNumber;    //发送方账号
    private String receiverNumber;  //接收方账号
    private String message; //发送的消息
    private LocalDateTime sendTime; //发送日期

    public ChatLog() {
        this("", "", "", "", LocalDateTime.of(1970, 1, 1, 0, 0));
    }

    public ChatLog(String chatLogID,String senderNumber, String receiverNumber, String message, LocalDateTime sendTime) {
        this.chatLogID = chatLogID;
        this.senderNumber = senderNumber;
        this.receiverNumber = receiverNumber;
        this.message = message;
        this.sendTime = sendTime;
    }

    public String getChatLogID() {
        return chatLogID;
    }

    public void setChatLogID(String chatLogID) {
        this.chatLogID = chatLogID;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }
}
