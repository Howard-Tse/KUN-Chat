package com.practice.chat.chatlog;

import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

public class ChatLogShow {
    private StringProperty content; //发送的消息内容
    private ImageView SenderProfilePhoto; //发送者的头像

    public ChatLogShow(StringProperty content, ImageView senderProfilePhoto) {
        this.content = content;
        SenderProfilePhoto = senderProfilePhoto;
    }

    public String getContent() {
        return content.get();
    }

    public StringProperty contentProperty() {
        return content;
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    public ImageView getSenderProfilePhoto() {
        return SenderProfilePhoto;
    }

    public void setSenderProfilePhoto(ImageView senderProfilePhoto) {
        SenderProfilePhoto = senderProfilePhoto;
    }
}
