package com.practice.chat.client;

import com.practice.chat.ChatInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Application implements ChatInterface {
    private static Socket socket = null;
    private static ObjectOutputStream outputStream = null;
    private static ObjectInputStream inputStream = null;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    public static void connect() throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new Socket(HOST, PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        }
    }

    public static void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            outputStream.close();
            inputStream.close();
            socket.close();

            outputStream = null;
            inputStream = null;
            socket = null;
        }
    }

    public static ObjectInputStream getInputStream() {
        if (socket != null && !socket.isClosed())
            return inputStream;
        else
            return null;
    }

    public static ObjectOutputStream getOutputStream() {
        if (socket != null && !socket.isClosed())
            return outputStream;
        else
            return null;
    }
}
