package com.practice.chat.requestmessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeoutException;

public class UpdateSignatureRequest extends RequestMessage{
    private String number;
    private String signature;
    private final RequestId requestId = RequestId.UpdateSignatureRequest;

    public UpdateSignatureRequest(String number, String signature) {
        this(null, null, number, signature);
    }

    public UpdateSignatureRequest(ObjectInputStream inputStream, ObjectOutputStream outputStream,
                                  String number, String signature) {
        super(inputStream, outputStream);
        this.number = number;
        this.signature = signature;
    }

    @Override
    public void sendMessageToServer() throws IOException {
        outputStream.writeObject(requestId.getValue());
        outputStream.writeObject(number);
        outputStream.writeObject(signature);
    }

    @Override
    public <T> T getResult(boolean wait) throws TimeoutException {
        if(wait) waitForRespond();
        try {
            Boolean r = (boolean) inputStream.readObject();
            return (T) r;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
