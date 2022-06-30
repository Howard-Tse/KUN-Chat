package com.practice.chat.requestmessage;

import com.practice.chat.user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class GetMyFriendRequestsRequest extends RequestMessage {
    private String number;
    private final RequestId requestId = RequestId.GetFriendRequestsRequest;

    public GetMyFriendRequestsRequest(String number) {
        this(null, null, number);
    }

    public GetMyFriendRequestsRequest(ObjectInputStream inputStream, ObjectOutputStream outputStream, String number) {
        super(inputStream, outputStream);
        this.number = number;
    }

    @Override
    public void sendMessageToServer() throws IOException {
        outputStream.writeObject(requestId.getValue());
        outputStream.writeObject(number);
    }

    @Override
    public <T> T getResult(boolean wait) throws TimeoutException {
        try {
            List<User> friendRequests = new ArrayList<>();

            Integer size = (Integer) inputStream.readObject();
            for (int i = 0; i < size; ++i) {
                friendRequests.add((User) inputStream.readObject());
            }

            return (T) friendRequests;
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
}
