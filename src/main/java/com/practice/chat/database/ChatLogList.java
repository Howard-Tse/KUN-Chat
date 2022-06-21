package com.practice.chat.database;

import java.util.LinkedList;

public class ChatLogList extends LinkedList {
    private int chatLogIDSeed; //聊天记录ID的种子

    public ChatLogList() {
        this(1);
    }

    public ChatLogList(int seed) {
        this.chatLogIDSeed = seed;
    }

    public int getChatLogIDSeed() {
        return chatLogIDSeed;
    }

    public void setChatLogIDSeed(int chatLogIDSeed) {
        this.chatLogIDSeed = chatLogIDSeed;
    }
}
