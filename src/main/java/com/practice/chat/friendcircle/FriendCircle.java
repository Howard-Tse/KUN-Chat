package com.practice.chat.friendcircle;

import java.io.Serializable;

public class FriendCircle implements Serializable {//实体类，给表格插入数据。
    private String senderNumber; //发送者的账号
    private byte[] sharedPhoto; //分享的图片
    private String sharedContentText; //分享内容
    private String shareTime; //分享时间

    public FriendCircle(String senderNumber, byte[] sharedPhoto, String sharedContentText, String shareTime) {
        this.senderNumber = senderNumber;
        this.sharedPhoto = sharedPhoto;
        this.sharedContentText = sharedContentText;
        this.shareTime = shareTime;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public byte[] getSharedPhoto() {
        return sharedPhoto;
    }

    public void setSharedPhoto(byte[] sharedPhoto) {
        this.sharedPhoto = sharedPhoto;
    }

    public String getSharedContentText() {
        return sharedContentText;
    }

    public void setSharedContentText(String sharedContentText) {
        this.sharedContentText = sharedContentText;
    }

    public String getShareTime() {
        return shareTime;
    }

    public void setShareTime(String shareTime) {
        this.shareTime = shareTime;
    }
}
