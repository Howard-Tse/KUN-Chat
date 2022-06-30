package com.practice.chat.requestmessage;

import com.practice.chat.chatlog.ChatLog;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class GetChatLogRequest extends RequestMessage {
    private String senderNumber, receiverNumber;
    private final RequestId requestId = RequestId.GetChatLogRequest;

    public GetChatLogRequest(String senderNumber, String receiverNumber) {
        this(null, null, senderNumber, receiverNumber);
    }

    public GetChatLogRequest(ObjectInputStream inputStream, ObjectOutputStream outputStream, String senderNumber, String receiverNumber) {
        super(inputStream, outputStream);
        this.senderNumber = senderNumber;
        this.receiverNumber = receiverNumber;
    }

    @Override
    public void sendMessageToServer() throws IOException {
        outputStream.writeObject(requestId.getValue());
        outputStream.writeObject(senderNumber);
        outputStream.writeObject(receiverNumber);
    }

    @Override
    public <T> T getResult(boolean wait) throws TimeoutException {
        if (wait) waitForRespond();

        try {
            List<ChatLog> chatLogs = new ArrayList<>();
            Integer size = (Integer) inputStream.readObject();
            for (int i = 0; i < size; ++i) {
                chatLogs.add((ChatLog) inputStream.readObject());
            }
            return (T) chatLogs;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public RequestId getRequestId() {
        return requestId;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }
}
