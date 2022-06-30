package com.practice.chat.requestmessage;

import com.practice.chat.user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class GetMyFriendListRequest extends RequestMessage {
    String number;
    final RequestId requestId = RequestId.GetMyFriendListRequest;

    public GetMyFriendListRequest(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this(inputStream, outputStream, "");
    }

    public GetMyFriendListRequest(String number) {
        this(null, null, number);
    }

    public GetMyFriendListRequest(ObjectInputStream inputStream, ObjectOutputStream outputStream,
                                  String number) {
        super(inputStream, outputStream);
        this.number = number;
    }

    @Override
    public void sendMessageToServer() throws IOException {
        outputStream.writeObject(requestId.getValue());
        outputStream.writeObject(number);
    }

    @Override
    public <T> T getResult(boolean wait) {
        try {
            if(wait) waitForRespond();
            Integer numberOfFriend = (Integer) inputStream.readObject();
            List<User> friendList = new ArrayList<>();
            for (int i = 0; i < numberOfFriend; ++i) {
                friendList.add((User) inputStream.readObject());
            }
            return (T) friendList;
        } catch (TimeoutException | IOException | ClassNotFoundException e) {
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
}
