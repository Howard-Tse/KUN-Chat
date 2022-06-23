package com.practice.chat.server;

public enum RequestId {
    Error(-1),
    LoginRequest(0),
    RegisterRequest(1),
    GetMyFriendList(2),
    ChatWithFriend(3);

    private final int value;

    RequestId(int value) {
        this.value = value;
    }

    public static RequestId valueOf(int num) {
        return switch (num) {
            case 0 -> LoginRequest;
            case 1 -> RegisterRequest;
            case 2 -> GetMyFriendList;
            case 3 -> ChatWithFriend;
            default -> Error;
        };
    }

    public int getValue() {
        return value;
    }

}
