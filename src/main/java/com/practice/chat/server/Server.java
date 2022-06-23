package com.practice.chat.server;

import com.practice.chat.ChatInterface;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements ChatInterface {
    private static ServerSocket serverSocket;
    static {
        try {
            serverSocket = new ServerSocket(Port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            while(true) {
                Logger logger = Logger.getInstance();
                Socket socket = serverSocket.accept();
                System.out.println("accept " + socket.getInetAddress().getHostAddress());

                DataInputStream fromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());

                String message = fromClient.readUTF();
                System.out.println("receive message: " + message);

                handleMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handleMessage(String message) {
        System.out.println("handle message: " + message);
    }
}
