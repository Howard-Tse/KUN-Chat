package com.practice.chat.client;

import com.practice.chat.ChatInterface;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements ChatInterface {


    public static void main(String[] args) {
        try {
            Socket socket = new Socket(host, Port);
            DataInputStream fromServer = new DataInputStream(socket.getInputStream());
            DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());

            System.out.println(fromServer.readUTF());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
