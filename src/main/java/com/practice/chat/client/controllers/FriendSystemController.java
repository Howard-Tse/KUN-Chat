package com.practice.chat.client.controllers;

import com.practice.chat.client.Client;
import com.practice.chat.client.menus.FriendMenu;
import com.practice.chat.client.menus.FriendRequestMenu;
import com.practice.chat.requestmessage.AddFriendRequest;
import com.practice.chat.requestmessage.GetMyFriendRequestsRequest;
import com.practice.chat.requestmessage.HandleFriendRequestsRequest;
import com.practice.chat.user.User;
import com.practice.chat.utils.ImageUtil;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class FriendSystemController {
    @FXML
    private TextField textFriendID;  //好友账户
    @FXML
    private TreeView<String> treeView;
    private User user;
    private List<User> friendRequests;

    private TreeItem<String> root;

    public void setUser(User user) {
        this.user = user;

        try {
            Client.connect();

            GetMyFriendRequestsRequest request = new GetMyFriendRequestsRequest(
                    Client.getInputStream(), Client.getOutputStream(), user.getNumber());
            request.sendMessageToServer();

            friendRequests = request.getResult(false);

            Client.disconnect();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

        root = new TreeItem<>();
        for (User u : friendRequests) {
            ImageView imageView = new ImageView(ImageUtil.binaryStreamToImage(u.getIcon()));
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);

            TreeItem<String> item = new TreeItem<>(u.getNickname() + "(" + u.getNumber() + ")想与你成为好友",
                    imageView);
            root.getChildren().add(item);
        }
        treeView.setRoot(root);
        treeView.setShowRoot(false);
    }

    @FXML
    private void initialize() {
        FriendRequestMenu menu = FriendRequestMenu.getInstance();
        menu.getItems().get(0).setOnAction(e -> {
            int index = treeView.getSelectionModel().getSelectedIndex();

            try {
                Client.connect();

                HandleFriendRequestsRequest request =
                        new HandleFriendRequestsRequest(Client.getInputStream(), Client.getOutputStream(),
                                user.getNumber(), friendRequests.get(index).getNumber(), true);
                request.sendMessageToServer();

                request.getResult(false);

                Client.disconnect();
            } catch (IOException | TimeoutException ex) {
                throw new RuntimeException(ex);
            }

            root.getChildren().remove(index);
        });

        menu.getItems().get(1).setOnAction(e -> {
            int index = treeView.getSelectionModel().getSelectedIndex();

            try {
                Client.connect();

                HandleFriendRequestsRequest request =
                        new HandleFriendRequestsRequest(Client.getInputStream(), Client.getOutputStream(),
                                user.getNumber(), friendRequests.get(index).getNumber(), false);
                request.sendMessageToServer();

                request.getResult(false);

                Client.disconnect();
            } catch (IOException | TimeoutException ex) {
                throw new RuntimeException(ex);
            }
            root.getChildren().remove(index);
        });
        treeView.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.SECONDARY) {
                Node node = e.getPickResult().getIntersectedNode();
                menu.show(node, Side.BOTTOM, 0, 0);
            }
        });
    }

    @FXML
    private void onAddFriendButtonClicked() {
        String number = textFriendID.getText();

        try {
            Client.connect();

            AddFriendRequest request = new AddFriendRequest(
                    Client.getInputStream(), Client.getOutputStream(), user.getNumber(), number);
            request.sendMessageToServer();
            Boolean result = request.getResult(false);
            Alert alert;
            if (result) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("添加好友");
                alert.setContentText("请求发送成功");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("添加好友");
                alert.setContentText("账号输入错误，或已经是好友");
            }
            alert.show();

            Client.disconnect();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
