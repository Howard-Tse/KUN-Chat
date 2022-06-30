package com.practice.chat.client.controllers;

import com.practice.chat.client.Client;
import com.practice.chat.friendcircle.FriendCircle;
import com.practice.chat.requestmessage.GetFriendCircleRequest;
import com.practice.chat.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class FriendCircleController {
    @FXML
    private ImageView bk;
    @FXML
    private TableColumn<FriendCircle, ImageView> headPicture;
    @FXML
    private TableColumn<FriendCircle, ImageView> mainPicture;
    @FXML
    private TableColumn<FriendCircle, String> mainText;
    @FXML
    private TableView<FriendCircle> table;

    private User user;
    private ObservableList<FriendCircle> list;

    public void setUser(User user) {
        this.user = user;

        try {
            Client.connect();

            GetFriendCircleRequest request = new GetFriendCircleRequest(Client.getInputStream(), Client.getOutputStream(),
                    user.getNumber());
            request.sendMessageToServer();

            List<FriendCircle> friendCircles = request.getResult(false);

            Client.disconnect();

            list = FXCollections.observableArrayList(friendCircles);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize() {
        ObservableList<FriendCircle> cellDate = FXCollections.observableArrayList();//队列数据
        headPicture.setCellValueFactory(new PropertyValueFactory<>("headPicture"));
        mainText.setCellValueFactory(new PropertyValueFactory<>("mainText"));
        mainPicture.setCellValueFactory(new PropertyValueFactory<>("mainPicture"));

        //调用此函数连接数据库，插入数据


        table.setItems(cellDate);
    }

    @FXML
    private void onShareButtonClicked() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Client.class.getResource("AddFriendCircle.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((AddFriendCircleController) loader.getController()).setUser(user);
        stage.setTitle("发布我的朋友圈");
        stage.show();
    }
}
//点击按钮，事件触发器，接口FXML文件