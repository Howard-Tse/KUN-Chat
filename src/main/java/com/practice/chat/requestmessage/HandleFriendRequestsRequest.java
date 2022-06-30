package com.practice.chat.requestmessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeoutException;

public class HandleFriendRequestsRequest extends RequestMessage {
    private String senderNumber, receiverNumber;
    private boolean agree;
    private final RequestId requestId = RequestId.HandleFriendRequest;

    public HandleFriendRequestsRequest(String senderNumber, String receiverNumber, boolean agree) {
        this.senderNumber = senderNumber;
        this.receiverNumber = receiverNumber;
        this.agree = agree;
    }

    public HandleFriendRequestsRequest(ObjectInputStream inputStream, ObjectOutputStream outputStream, String senderNumber, String receiverNumber, boolean agree) {
        super(inputStream, outputStream);
        this.senderNumber = senderNumber;
        this.receiverNumber = receiverNumber;
        this.agree = agree;
    }

    @Override
    public void sendMessageToServer() throws IOException {
        outputStream.writeObject(requestId.getValue());
        outputStream.writeObject(senderNumber);
        outputStream.writeObject(receiverNumber);
        outputStream.writeObject(agree);
    }

    @Override
    public <T> T getResult(boolean wait) throws TimeoutException {
        if (wait) waitForRespond();
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

    public boolean isAgree() {
        return agree;
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }
}
