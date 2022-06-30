package com.practice.chat.requestmessage;

import com.practice.chat.ChatInterface;
import com.practice.chat.friendcircle.FriendCircle;
import com.practice.chat.user.User;

import java.io.*;
import java.util.concurrent.TimeoutException;

public abstract class RequestMessage implements Serializable, ChatInterface {
    protected ObjectInputStream inputStream = null;
    protected ObjectOutputStream outputStream = null;

    public RequestMessage() {}

    public RequestMessage(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    abstract public void sendMessageToServer() throws IOException;

    abstract public <T> T getResult(boolean wait) throws TimeoutException;

    protected void waitForRespond() throws TimeoutException {
        long startTime, endTime;
        startTime = endTime = System.currentTimeMillis();
        System.out.println("等待服务器回应...");

        while (true) {
            try {
                if (endTime - startTime > CLIENT_WAIT_TIME || inputStream.available() > 0) break;
                endTime = System.currentTimeMillis();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (endTime - startTime > CLIENT_WAIT_TIME)
            throw new TimeoutException("连接超时");
    }

    public static RequestMessage receiveMessageFromClient(ObjectInputStream inputStream) {
        try {
            RequestId requestId = RequestId.valueOf((Integer) inputStream.readObject());
            RequestMessage requestMessage = null;

            switch (requestId) {
                case LoginRequest -> {
                    String number, password;
                    number = (String) inputStream.readObject();
                    password = (String) inputStream.readObject();
                    requestMessage = new LoginRequest(number, password);
                }
                case RegisterRequest -> {
                    String password, nickname, birthday, sex, signature;
                    byte[] icon;

                    password = (String) inputStream.readObject();
                    nickname = (String) inputStream.readObject();
                    birthday = (String) inputStream.readObject();
                    sex = (String) inputStream.readObject();
                    icon = (byte[]) inputStream.readObject();
                    signature = (String) inputStream.readObject();

                    requestMessage = new RegisterRequest(password, nickname, birthday, sex, icon, signature);
                }
                case GetMyFriendListRequest -> {
                    String number = (String) inputStream.readObject();
                    requestMessage = new GetMyFriendListRequest(number);
                }
                case UpdateSignatureRequest -> {
                    String number = (String) inputStream.readObject();
                    String signature = (String) inputStream.readObject();
                    requestMessage = new UpdateSignatureRequest(number, signature);
                }
                case AddFriendRequest -> {
                    String senderNumber = (String) inputStream.readObject();
                    String receiverNumber = (String) inputStream.readObject();
                    requestMessage = new AddFriendRequest(senderNumber, receiverNumber);
                }
                case GetFriendRequestsRequest -> {
                    String number = (String) inputStream.readObject();
                    requestMessage = new GetMyFriendRequestsRequest(number);
                }
                case GetChatLogRequest -> {
                    String senderNumber = (String) inputStream.readObject();
                    String receiverNumber = (String) inputStream.readObject();
                    requestMessage = new GetChatLogRequest(senderNumber, receiverNumber);
                }
                case SendChatMessageRequest -> {
                    String senderNumber = (String) inputStream.readObject();
                    String receiverNumber = (String) inputStream.readObject();
                    String content = (String) inputStream.readObject();
                    requestMessage = new SendChatMessageRequest(senderNumber, receiverNumber, content);
                }
                case ModifyProfileRequest -> {
                    String number = (String) inputStream.readObject();
                    User newInfo = (User) inputStream.readObject();
                    requestMessage = new ModifyProfileRequest(number, newInfo);
                }
                case GetFriendCircleRequest -> {
                    String number = (String) inputStream.readObject();
                    requestMessage = new GetFriendCircleRequest(number);
                }
                case AddFriendCircleRequest -> {
                    FriendCircle friendCircle = (FriendCircle) inputStream.readObject();
                    requestMessage = new AddFriendCircleRequest(friendCircle);
                }
                case HandleFriendRequest -> {
                    String senderNumber = (String) inputStream.readObject();
                    String receiverNumber = (String) inputStream.readObject();
                    Boolean agree = (Boolean) inputStream.readObject();
                    requestMessage = new HandleFriendRequestsRequest(senderNumber, receiverNumber, agree);
                }
            }

            return requestMessage;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    abstract public RequestId getRequestId();
}
