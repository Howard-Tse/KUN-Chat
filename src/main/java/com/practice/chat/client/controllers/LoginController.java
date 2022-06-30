package com.practice.chat.client.controllers;

import com.practice.chat.client.Client;
import com.practice.chat.requestmessage.LoginRequest;
import com.practice.chat.user.User;
import com.practice.chat.utils.TextUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class LoginController {
    @FXML
    private TextField textFieldAccount;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label labelTip;

    @FXML
    void onLoginButtonClicked() {
        String number = textFieldAccount.getText();
        String password = passwordField.getText();

        if (!TextUtil.isAccountNumber(number)) {
            labelTip.setText("账号必须是10位且全为纯数字");
        } else if (password.length() < 6) {
            labelTip.setText("密码必须大于等于6位");
        } else {
            labelTip.setText("");

            Stage mainStage = (Stage) textFieldAccount.getScene().getWindow();
            try {
                Client.connect();

                LoginRequest loginRequest = new LoginRequest(Client.getInputStream(), Client.getOutputStream(),
                        number, password);
                loginRequest.sendMessageToServer();

                Object o = loginRequest.getResult(false);
                Client.disconnect();
                if (o instanceof User) {
                    User user = (User) o;
                    FXMLLoader loader = new FXMLLoader(Client.class.getResource("FriendList.fxml"));
                    Scene scene = new Scene(loader.load());
                    ((FriendListController) loader.getController()).setUser((user));
                    mainStage.setScene(scene);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("登录信息");
                    alert.setContentText("登录失败，用户名或密码输入错误");
                    alert.showAndWait();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException ignored) {
            }
        }
    }

    @FXML
    void onRegisterButtonClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("Register.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setTitle("注册");
        stage.setScene(scene);
        stage.show();
    }

}
