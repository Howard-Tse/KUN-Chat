package com.practice.chat.requestmessage;

public enum RequestId {
    Error(-1),
    LoginRequest(0),
    RegisterRequest(1),
    GetMyFriendListRequest(2),
    GetChatLogRequest(3),
    SendChatMessageRequest(4),
    GetFriendCircleRequest(5),
    UpdateSignatureRequest(6),
    AddFriendRequest(7),
    GetFriendRequestsRequest(8),
    ModifyProfileRequest(9),
    AddFriendCircleRequest(10),
    HandleFriendRequest(11);

    private final int value;

    RequestId(int value) {
        this.value = value;
    }

    public static RequestId valueOf(int num) {
        return switch (num) {
            case 0 -> LoginRequest;
            case 1 -> RegisterRequest;
            case 2 -> GetMyFriendListRequest;
            case 3 -> GetChatLogRequest;
            case 4 -> SendChatMessageRequest;
            case 5 -> GetFriendCircleRequest;
            case 6 -> UpdateSignatureRequest;
            case 7 -> AddFriendRequest;
            case 8 -> GetFriendRequestsRequest;
            case 9 -> ModifyProfileRequest;
            case 10 -> AddFriendCircleRequest;
            case 11 -> HandleFriendRequest;
            default -> Error;
        };
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return switch (value) {
            case 0 -> "登录请求";
            case 1 -> "注册请求";
            case 2 -> "获取好友列表请求";
            case 3 -> "获取聊天记录请求";
            case 4 -> "发送消息请求";
            case 5 -> "获取朋友圈请求";
            case 6 -> "更改个性签名请求";
            case 7 -> "添加好友请求";
            case 8 -> "获取好友请求的请求";
            case 9 -> "修改个人资料请求";
            case 10 -> "添加朋友圈请求";
            case 11 -> "处理好友申请的请求";
            default -> "错误";
        };
    }
}
