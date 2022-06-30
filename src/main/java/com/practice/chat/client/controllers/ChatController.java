package com.practice.chat.client.controllers;

import com.practice.chat.chatlog.ChatLog;
import com.practice.chat.client.Client;
import com.practice.chat.requestmessage.GetChatLogRequest;
import com.practice.chat.requestmessage.SendChatMessageRequest;
import com.practice.chat.user.User;
import com.practice.chat.utils.ImageUtil;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ChatController {
    @FXML
    private TreeView<String> treeView;
    @FXML
    private TextArea textArea;

    private TreeItem<String> root = new TreeItem<>();
    private User sender, receiver;
    private List<ChatLog> list = null;
    private ChatLog lastChatLog = null;
    private int indexOfLastChatLog = -1;

    private Stage thisStage;

    public void setThisStage(Stage thisStage) {
        this.thisStage = thisStage;

        Thread refreshChatLogThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }
                updateChatLog();
            }
        });

        refreshChatLogThread.start();
        thisStage.setOnCloseRequest(e -> refreshChatLogThread.stop());
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void updateChatLog() {
        if (sender != null && receiver != null) {
            try {
                Client.connect();

                GetChatLogRequest request = new GetChatLogRequest(Client.getInputStream(), Client.getOutputStream(),
                        sender.getNumber(), receiver.getNumber());
                request.sendMessageToServer();
                list = request.getResult(false);

                Client.disconnect();

                if (!list.isEmpty()) {
                    ChatLog listLastChatLog = list.get(list.size() - 1);
                    if (lastChatLog == null || listLastChatLog.getChatLogID() != lastChatLog.getChatLogID()) {
                        for (int i = indexOfLastChatLog + 1; i < list.size(); ++i) {
                            ImageView imageView;
                            if (list.get(i).getSenderNumber().equals(sender.getNumber()))
                                imageView = new ImageView(ImageUtil.binaryStreamToImage(sender.getIcon()));
                            else
                                imageView = new ImageView(ImageUtil.binaryStreamToImage(receiver.getIcon()));
                            imageView.setFitWidth(40);
                            imageView.setFitHeight(40);

                            ChatLog chatLog = list.get(i);

                            TreeItem<String> item = new TreeItem<>(chatLog.getSenderNumber() + "\n" +
                                    chatLog.getSendTime() + "\n" + chatLog.getContent(), imageView);
                            root.getChildren().add(item);
                        }

                        lastChatLog = listLastChatLog;
                        indexOfLastChatLog = list.size() - 1;

                        treeView.scrollTo(indexOfLastChatLog);
                    }
                }
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void initialize() {
        treeView.setCellFactory(stringTreeView -> new TreeCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (!empty) {
                    setText(item);
                    setGraphic(getTreeItem().getGraphic());

                    String[] strings = item.split("\n");
                    String number = strings[0];
                    if (number.equals(sender.getNumber())) {
                        setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    } else {
                        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                    }
                } else {
                    setText(null);
                    setGraphic(null);
                }
            }
        });

        treeView.setRoot(root);
        treeView.setShowRoot(false);

    }

    @FXML
    private void onSendMsgButtonClicked() {
        String content = textArea.getText();
        try {
            Client.connect();

            SendChatMessageRequest request = new SendChatMessageRequest(Client.getInputStream(), Client.getOutputStream(),
                    sender.getNumber(), receiver.getNumber(), content);
            request.sendMessageToServer();
            request.getResult(false);

            Client.disconnect();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

        textArea.clear();
        updateChatLog();
    }
}
