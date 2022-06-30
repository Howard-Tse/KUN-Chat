package com.practice.chat.requestmessage;

import com.practice.chat.friendcircle.FriendCircle;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class GetFriendCircleRequest extends RequestMessage {
    private String senderNumber;
    private final RequestId requestId = RequestId.GetFriendCircleRequest;

    public GetFriendCircleRequest(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public GetFriendCircleRequest(ObjectInputStream inputStream, ObjectOutputStream outputStream, String senderNumber) {
        super(inputStream, outputStream);
        this.senderNumber = senderNumber;
    }

    @Override
    public void sendMessageToServer() throws IOException {
        outputStream.writeObject(requestId.getValue());
        outputStream.writeObject(senderNumber);
    }

    @Override
    public <T> T getResult(boolean wait) throws TimeoutException {
        try {
            Integer size = (Integer) inputStream.readObject();
            List<FriendCircle> list = new ArrayList<>();

            for (int i = 0; i < size; ++i) {
                list.add((FriendCircle) inputStream.readObject());
            }

            return (T) list;
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
}
