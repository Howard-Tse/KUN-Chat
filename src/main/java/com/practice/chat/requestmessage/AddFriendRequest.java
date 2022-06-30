package com.practice.chat.requestmessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeoutException;

public class AddFriendRequest extends RequestMessage {
    private String senderNumber, receiverNumber;
    private final RequestId requestId = RequestId.AddFriendRequest;

    public AddFriendRequest(String senderNumber, String receiverNumber) {
        this(null, null, senderNumber, receiverNumber);
    }

    public AddFriendRequest(ObjectInputStream inputStream, ObjectOutputStream outputStream, String senderNumber, String receiverNumber) {
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
            Boolean b = (Boolean) inputStream.readObject();
            return (T) b;
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
