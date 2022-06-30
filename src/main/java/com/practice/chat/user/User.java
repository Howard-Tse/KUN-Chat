package com.practice.chat.user;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class User implements Serializable {
    private String number; //账号
    private String nickname; //昵称
    private String sex; //性别
    private String password; //登录密码
    private byte[] icon; //用户头像
    private String birthday; //生日
    private String signature; //个性签名

    public User() {
        this("", "", "", "", null, "", "");
    }

    public User(String number, String nickname, String sex, String password, byte[] icon, String birthday, String signature) {
        this.number = number;
        this.nickname = nickname;
        this.sex = sex;
        this.password = password;
        this.icon = icon;
        this.birthday = birthday;
        this.signature = signature;
    }

    public User(User user) {
        this(user.number, user.nickname, user.sex, user.password, user.icon, user.birthday, user.signature);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "账号: " + number + "\n" +
                "密码: " + password + "\n" +
                "昵称: " + nickname + "\n" +
                "生日: " + birthday + "\n" +
                "性别: " + sex;
    }

    public UserProperty toUserProperty() {
        return new UserProperty(
                new SimpleStringProperty(number),
                new SimpleStringProperty(nickname),
                new SimpleStringProperty(sex),
                new SimpleStringProperty(password),
                icon,
                new SimpleStringProperty(birthday)
        );
    }
}
