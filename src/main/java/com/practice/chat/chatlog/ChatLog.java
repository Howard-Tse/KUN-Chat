package com.practice.chat.chatlog;

import java.io.Serializable;

public class ChatLog implements Serializable {
    private int chatLogID; //聊天记录编号
    private String senderNumber;    //发送方账号
    private String receiverNumber;  //接收方账号
    private String content; //发送消息的内容
    private String sendTime; //发送日期

    public ChatLog() {
        this(0, "", "", "", "");
    }

    public ChatLog(int chatLogID, String senderNumber, String receiverNumber, String content, String sendTime) {
        this.chatLogID = chatLogID;
        this.senderNumber = senderNumber;
        this.receiverNumber = receiverNumber;
        this.content = content;
        this.sendTime = sendTime;
    }

    public int getChatLogID() {
        return chatLogID;
    }

    public void setChatLogID(int chatLogID) {
        this.chatLogID = chatLogID;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
