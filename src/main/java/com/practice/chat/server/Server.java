package com.practice.chat.server;

import com.practice.chat.ChatInterface;
import com.practice.chat.chatlog.ChatLog;
import com.practice.chat.chatlog.ChatLogImpl;
import com.practice.chat.dao.ChatLogDAO;
import com.practice.chat.dao.FriendCircleDAO;
import com.practice.chat.dao.UserDAO;
import com.practice.chat.friendcircle.FriendCircle;
import com.practice.chat.friendcircle.FriendCircleImpl;
import com.practice.chat.requestmessage.*;
import com.practice.chat.user.User;
import com.practice.chat.user.UserDAOImpl;
import com.practice.chat.utils.NumberUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements ChatInterface {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger("Server");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            logger.log(Level.INFO, "服务器启动中");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                logger.log(Level.INFO, "等待用户连接中");
                ObjectOutputStream toClient;
                ObjectInputStream fromClient;

                try (Socket socket = serverSocket.accept()) {
                    logger.log(Level.INFO, "接受" + socket.getInetAddress().getHostAddress());

                    toClient = new ObjectOutputStream(socket.getOutputStream());
                    logger.log(Level.INFO, "构造输出流");

                    fromClient = new ObjectInputStream(socket.getInputStream());
                    logger.log(Level.INFO, "构造输入流");

                    logger.log(Level.INFO, "开始获取客户端请求");
                    RequestMessage requestMessage = RequestMessage.receiveMessageFromClient(fromClient);
                    logger.log(Level.INFO, "获取到" + requestMessage.getRequestId().toString());

                    switch (requestMessage.getRequestId()) {
                        case LoginRequest -> handleLogin(requestMessage, toClient);
                        case RegisterRequest -> handleRegister(requestMessage, toClient);
                        case GetMyFriendListRequest -> handleGetFriendList(requestMessage, toClient);
                        case UpdateSignatureRequest -> handleUpdateSignature(requestMessage, toClient);
                        case AddFriendRequest -> handleAddFriend(requestMessage, toClient);
                        case GetFriendRequestsRequest -> handleGetFriendRequests(requestMessage, toClient);
                        case GetChatLogRequest -> handleGetChatLog(requestMessage, toClient);
                        case SendChatMessageRequest -> handleSendChatMessage(requestMessage, toClient);
                        case ModifyProfileRequest -> handleModifyProfile(requestMessage, toClient);
                        case GetFriendCircleRequest -> handleGetFriendCircle(requestMessage, toClient);
                        case AddFriendCircleRequest -> handleAddFriendCircle(requestMessage, toClient);
                        case HandleFriendRequest -> handleFriendRequest(requestMessage, toClient);
                    }
                    logger.log(Level.INFO, "请求处理成功");
                    logger.log(Level.INFO, "用户断开连接");
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleLogin(RequestMessage requestMessage, ObjectOutputStream outputStream) throws SQLException {
        Logger logger = Logger.getLogger("handler");
        LoginRequest loginRequest = (LoginRequest) requestMessage;
        UserDAO userDAO = new UserDAOImpl();

        User user = userDAO.findUserByNumber(loginRequest.getNumber());
        if (user != null)
            System.out.println(user);

        System.out.println("登录请求账号密码: " + loginRequest.getNumber() + " " + loginRequest.getPassword());

        try {
            if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
                outputStream.writeObject(false);
                logger.log(Level.INFO, "写入空用户对象");
            } else {
                outputStream.writeObject(user);
                logger.log(Level.INFO, "写入用户对象");
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRegister(RequestMessage requestMessage, ObjectOutputStream outputStream) throws SQLException {
        RegisterRequest registerRequest = (RegisterRequest) requestMessage;
        UserDAO userDAO = new UserDAOImpl();
        User user = new User(registerRequest.getUser());
        if (registerRequest.getUser().getIcon() == null) {
            System.out.println("头像为空");
        }

        String newNumber = NumberUtil.generateNewNumber();

        user.setNumber(newNumber);
        System.out.println(user);

        userDAO.addUser(user);

        try {
            outputStream.writeObject(newNumber);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleGetFriendList(RequestMessage requestMessage, ObjectOutputStream outputStream) throws SQLException {
        GetMyFriendListRequest request = (GetMyFriendListRequest) requestMessage;
        UserDAO userDAO = new UserDAOImpl();

        List<User> friends = userDAO.searchUserFriends(request.getNumber());
        try {
            outputStream.writeObject(friends.size());
            for (User friend : friends) {
                outputStream.writeObject(friend);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleUpdateSignature(RequestMessage requestMessage, ObjectOutputStream outputStream) throws SQLException {
        UpdateSignatureRequest request = (UpdateSignatureRequest) requestMessage;
        UserDAO userDAO = new UserDAOImpl();

        userDAO.updateUserSignature(request.getNumber(), request.getSignature());

        try {
            outputStream.writeObject(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleAddFriend(RequestMessage requestMessage, ObjectOutputStream outputStream) throws SQLException {
        AddFriendRequest request = (AddFriendRequest) requestMessage;
        UserDAO userDAO = new UserDAOImpl();

        User sender = userDAO.findUserByNumber(request.getSenderNumber());
        User receiver = userDAO.findUserByNumber(request.getReceiverNumber());

        try {
            if (sender == null || receiver == null) {
                outputStream.writeObject(false);
            } else {
                userDAO.addFriend(request.getSenderNumber(), request.getReceiverNumber());
                outputStream.writeObject(true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleGetFriendRequests(RequestMessage requestMessage, ObjectOutputStream outputStream) throws SQLException {
        GetMyFriendRequestsRequest request = (GetMyFriendRequestsRequest) requestMessage;
        UserDAO userDAO = new UserDAOImpl();

        List<User> friendRequests = userDAO.getFriendRequestsByReceiverNumber(request.getNumber());
        try {
            outputStream.writeObject(friendRequests.size());
            for (User user : friendRequests) {
                outputStream.writeObject(user);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleGetChatLog(RequestMessage requestMessage, ObjectOutputStream outputStream) throws SQLException {
        GetChatLogRequest request = (GetChatLogRequest) requestMessage;
        ChatLogDAO chatLogDAO = new ChatLogImpl();

        List<ChatLog> chatLogs = chatLogDAO.getChatLogs(request.getSenderNumber(), request.getReceiverNumber());

        try {
            outputStream.writeObject(chatLogs.size());
            for (ChatLog chatLog : chatLogs) {
                outputStream.writeObject(chatLog);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleSendChatMessage(RequestMessage requestMessage, ObjectOutputStream outputStream) throws SQLException {
        SendChatMessageRequest request = (SendChatMessageRequest) requestMessage;
        ChatLogDAO chatLogDAO = new ChatLogImpl();

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        chatLogDAO.addChatLog(new ChatLog(0, request.getSenderNumber(), request.getReceiverNumber(),
                request.getContent(), formatter.format(date)));

        try {
            outputStream.writeObject(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleModifyProfile(RequestMessage requestMessage, ObjectOutputStream outputStream) throws SQLException {
        ModifyProfileRequest request = (ModifyProfileRequest) requestMessage;
        UserDAO userDAO = new UserDAOImpl();

        userDAO.updateUser(request.getNumber(), request.getNewInfo());

        try {
            outputStream.writeObject(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleGetFriendCircle(RequestMessage requestMessage, ObjectOutputStream outputStream) throws SQLException {
        GetFriendCircleRequest request = (GetFriendCircleRequest) requestMessage;
        FriendCircleDAO friendCircleDAO = new FriendCircleImpl();

        List<FriendCircle> list = friendCircleDAO.findFriendCircleBySenderNumber(request.getSenderNumber());
        try {
            outputStream.writeObject(list.size());
            for (FriendCircle friendCircle : list) {
                outputStream.writeObject(friendCircle);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleAddFriendCircle(RequestMessage requestMessage, ObjectOutputStream outputStream) throws SQLException {
        AddFriendCircleRequest request = (AddFriendCircleRequest) requestMessage;
        FriendCircleDAO friendCircleDAO = new FriendCircleImpl();

        friendCircleDAO.addFriendCircle(request.getFriendCircle());

        try {
            outputStream.writeObject(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleFriendRequest(RequestMessage requestMessage, ObjectOutputStream outputStream) throws SQLException {
        HandleFriendRequestsRequest request = (HandleFriendRequestsRequest) requestMessage;
        UserDAO userDAO = new UserDAOImpl();

        if(request.isAgree()) {
            userDAO.agreeAddFriend(request.getSenderNumber(), request.getReceiverNumber());
        } else {
            userDAO.disagreeAddFriend(request.getSenderNumber(), request.getReceiverNumber());
        }

        try {
            outputStream.writeObject(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
