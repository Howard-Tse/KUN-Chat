package com.practice.chat.requestmessage;

import javafx.scene.control.Alert;

import java.io.*;
import java.util.concurrent.TimeoutException;

public class LoginRequest extends RequestMessage {
    private String number;
    private String password;
    private final RequestId requestId = RequestId.LoginRequest;

    public LoginRequest(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this(inputStream, outputStream, "", "");
    }

    public LoginRequest(String number, String password) {
        this(null, null, number, password);
    }

    public LoginRequest(ObjectInputStream inputStream, ObjectOutputStream outputStream,
                 String number, String password) {
        super(inputStream, outputStream);
        this.number = number;
        this.password = password;
    }

    @Override
    public void sendMessageToServer() {
        try {
            outputStream.writeObject(requestId.getValue());
            outputStream.writeObject(number);
            outputStream.writeObject(password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T getResult(boolean wait) throws TimeoutException {
        try {
            if(wait) waitForRespond();
            Object o = inputStream.readObject();
            return (T) o;
        } catch (TimeoutException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setContentText("连接服务器超时");
            alert.show();
            throw new TimeoutException();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
