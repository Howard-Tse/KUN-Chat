package com.practice.chat.user;

import javafx.beans.property.StringProperty;

public class UserProperty {
    private StringProperty number; //账号
    private StringProperty nickname; //昵称
    private StringProperty sex; //性别
    private StringProperty password; //登录密码
    private byte[] icon; //用户头像
    private StringProperty birthday; //生日

    public UserProperty(StringProperty number, StringProperty nickname, StringProperty sex,
                        StringProperty password, byte[] icon, StringProperty birthday) {
        this.number = number;
        this.nickname = nickname;
        this.sex = sex;
        this.password = password;
        this.icon = icon;
        this.birthday = birthday;
    }

    public String getNumber() {
        return number.get();
    }

    public StringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public String getNickname() {
        return nickname.get();
    }

    public StringProperty nicknameProperty() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname.set(nickname);
    }

    public String getSex() {
        return sex.get();
    }

    public StringProperty sexProperty() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex.set(sex);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public String getBirthday() {
        return birthday.get();
    }

    public StringProperty birthdayProperty() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday.set(birthday);
    }
}
