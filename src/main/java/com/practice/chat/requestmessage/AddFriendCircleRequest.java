package com.practice.chat.requestmessage;

import com.practice.chat.friendcircle.FriendCircle;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeoutException;

public class AddFriendCircleRequest extends RequestMessage {
    private FriendCircle friendCircle;
    private final RequestId requestId = RequestId.AddFriendCircleRequest;

    public AddFriendCircleRequest(FriendCircle friendCircle) {
        this.friendCircle = friendCircle;
    }

    public AddFriendCircleRequest(ObjectInputStream inputStream, ObjectOutputStream outputStream, FriendCircle friendCircle) {
        super(inputStream, outputStream);
        this.friendCircle = friendCircle;
    }

    @Override
    public void sendMessageToServer() throws IOException {
        outputStream.writeObject(requestId.getValue());
        outputStream.writeObject(friendCircle);
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

    public FriendCircle getFriendCircle() {
        return friendCircle;
    }

    public void setFriendCircle(FriendCircle friendCircle) {
        this.friendCircle = friendCircle;
    }
}
