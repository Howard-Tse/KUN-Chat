package com.practice.chat.requestmessage;

import com.practice.chat.user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeoutException;

public class RegisterRequest extends RequestMessage{
    private User user;
    private final RequestId requestId = RequestId.RegisterRequest;

    public RegisterRequest(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this(inputStream, outputStream, "", "", "", "", null, "");
    }

    public RegisterRequest(String password, String nickname, String birthday, String sex, byte[] icon, String signature) {
        this(null, null, password, nickname, birthday, sex, icon, signature);
    }

    public RegisterRequest(ObjectInputStream inputStream, ObjectOutputStream outputStream,
                    String password, String nickname, String birthday, String sex, byte[] icon, String signature) {
        super(inputStream, outputStream);
        user = new User("", nickname, sex, password, icon, birthday, signature);
    }

    public RegisterRequest(User user) {
        this.user = new User(user);
    }

    @Override
    public void sendMessageToServer() {
        try {
            outputStream.writeObject(requestId.getValue());
            outputStream.writeObject(user.getPassword());
            outputStream.writeObject(user.getNickname());
            outputStream.writeObject(user.getBirthday());
            outputStream.writeObject(user.getSex());
            outputStream.writeObject(user.getIcon());
            outputStream.writeObject(user.getSignature());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T getResult(boolean wait) {
        try {
            if(wait) waitForRespond();
            return (T) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestId getRequestId() {
        return requestId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
