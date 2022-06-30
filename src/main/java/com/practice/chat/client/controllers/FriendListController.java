package com.practice.chat.client.controllers;
import com.practice.chat.client.Client;
import com.practice.chat.client.menus.FriendMenu;
import com.practice.chat.client.menus.FriendRequestMenu;
import com.practice.chat.requestmessage.GetMyFriendListRequest;
import com.practice.chat.requestmessage.UpdateSignatureRequest;
import com.practice.chat.user.User;
import com.practice.chat.utils.ImageUtil;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class FriendListController {
    @FXML
    private ImageView imageView;
    @FXML
    private Text textFieldName;
    @FXML
    private TextField textFieldSignature;
    @FXML
    private TreeView<String> treeView;

    private User user;
    private List<User> friends;

    public void setUser(User user) {
        this.user = user;

        try {
            Client.connect();

            GetMyFriendListRequest request = new GetMyFriendListRequest(
                    Client.getInputStream(), Client.getOutputStream(), user.getNumber());
            request.sendMessageToServer();
            friends = request.getResult(false);

            Client.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Image image = ImageUtil.binaryStreamToImage(user.getIcon());
        if (image != null)
            imageView.setImage(image);

        TreeItem<String> root = new TreeItem<>("我的好友");
        for (User u : friends) {
            ImageView imageView = new ImageView(ImageUtil.binaryStreamToImage(u.getIcon()));
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);

            TreeItem<String> item = new TreeItem<>(u.getNickname() + "(" + u.getNumber() + ")", imageView);
            root.getChildren().add(item);
        }
        treeView.setRoot(root);
        treeView.setShowRoot(false);

        textFieldName.setText(user.getNickname());
        textFieldSignature.setText(user.getSignature());
    }

    @FXML
    void onFriendSystemButtonClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("FriendSystem.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        ((FriendSystemController) loader.getController()).setUser(user);
        stage.setTitle("联系人管理");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onProfileImageViewMouseClicked() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Client.class.getResource("EditData.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        EditDataController controller = loader.getController();
        controller.setUser(user);
        stage.setTitle("修改我的资料");
        stage.show();
    }

    @FXML
    private void onFriendCircleButtonClicked() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Client.class.getResource("FriendCircle.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((FriendCircleController) loader.getController()).setUser(user);
        stage.setTitle("朋友圈");
        stage.show();
    }

    @FXML
    private void initialize() {
        textFieldSignature.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                String signature = textFieldSignature.getText();
                try {
                    Client.connect();

                    UpdateSignatureRequest request = new UpdateSignatureRequest(
                            Client.getInputStream(), Client.getOutputStream(), user.getNumber(), signature);
                    request.sendMessageToServer();
                    request.getResult(false);

                    Client.disconnect();
                } catch (IOException | TimeoutException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        treeView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() >= 2) {
                int index = treeView.getSelectionModel().getSelectedIndex();
                User receiver = friends.get(index);

                openChatDialog(receiver);
            }

            if (e.getButton() == MouseButton.SECONDARY) {
                Node node = e.getPickResult().getIntersectedNode();
                FriendMenu menu = FriendMenu.getInstance();
                menu.show(node, Side.BOTTOM, 0, 0);
            }
        });

        FriendMenu menu = FriendMenu.getInstance();
        menu.getItems().get(0).setOnAction(e -> {
            int index = treeView.getSelectionModel().getSelectedIndex();
            User receiver = friends.get(index);

            openChatDialog(receiver);
        });

        menu.getItems().get(3).setOnAction(e -> {
            int index = treeView.getSelectionModel().getSelectedIndex();
            User selectedUser = friends.get(index);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("删除好友");
            alert.setContentText("确定要删除好友" + selectedUser.getNickname() + "(" + selectedUser.getNumber() + ")吗？");

            if (alert.showAndWait().get() == ButtonType.YES) {

            }
        });
    }

    void openChatDialog(User receiver) {
        FXMLLoader loader = new FXMLLoader(Client.class.getResource("Chat.fxml"));
        Stage stage = new Stage();
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        ((ChatController) loader.getController()).setSender(user);
        ((ChatController) loader.getController()).setReceiver(receiver);
        ((ChatController) loader.getController()).updateChatLog();
        ((ChatController) loader.getController()).setThisStage(stage);

        stage.setTitle(receiver.getNickname() + "(" + receiver.getNumber() + ")");
        stage.getIcons().add(ImageUtil.binaryStreamToImage(receiver.getIcon()));
        stage.show();
    }
}



