package com.practice.chat.requestmessage;

import com.practice.chat.user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeoutException;

public class ModifyProfileRequest extends RequestMessage {
    private String number;
    private User newInfo;
    private final RequestId requestId = RequestId.ModifyProfileRequest;

    public ModifyProfileRequest(String number, User newInfo) {
        this.number = number;
        this.newInfo = newInfo;
    }

    public ModifyProfileRequest(ObjectInputStream inputStream, ObjectOutputStream outputStream, String number, User newInfo) {
        super(inputStream, outputStream);
        this.number = number;
        this.newInfo = newInfo;
    }

    @Override
    public void sendMessageToServer() throws IOException {
        outputStream.writeObject(requestId.getValue());
        outputStream.writeObject(number);
        outputStream.writeObject(newInfo);
    }

    @Override
    public <T> T getResult(boolean wait) throws TimeoutException {
        try {
            return (T) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public RequestId getRequestId() {
        return requestId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getNewInfo() {
        return newInfo;
    }

    public void setNewInfo(User newInfo) {
        this.newInfo = newInfo;
    }
}
