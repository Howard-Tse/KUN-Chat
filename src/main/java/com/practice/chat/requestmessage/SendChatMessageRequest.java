package com.practice.chat.requestmessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeoutException;

public class SendChatMessageRequest extends RequestMessage {
    private String senderNumber, receiverNumber, content;
    private final RequestId requestId = RequestId.SendChatMessageRequest;

    public SendChatMessageRequest(String senderNumber, String receiverNumber, String content) {
        this(null, null, senderNumber, receiverNumber, content);
    }

    public SendChatMessageRequest(ObjectInputStream inputStream, ObjectOutputStream outputStream, String senderNumber, String receiverNumber, String content) {
        super(inputStream, outputStream);
        this.senderNumber = senderNumber;
        this.receiverNumber = receiverNumber;
        this.content = content;
    }

    @Override
    public void sendMessageToServer() throws IOException {
        outputStream.writeObject(requestId.getValue());
        outputStream.writeObject(senderNumber);
        outputStream.writeObject(receiverNumber);
        outputStream.writeObject(content);
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
