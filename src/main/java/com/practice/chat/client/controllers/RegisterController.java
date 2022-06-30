package com.practice.chat.client.controllers;
import com.practice.chat.client.Client;
import com.practice.chat.requestmessage.RegisterRequest;
import com.practice.chat.utils.ImageUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class RegisterController {
    @FXML
    private TextField textFieldName;                   //姓名
    @FXML
    private TextField textFieldSetPassword;            //密码
    @FXML
    private TextField textFieldSetReEnterPassword;     //确认密码
    @FXML
    private ChoiceBox<String> choiceBoxSex;                    //性别
    @FXML
    private ChoiceBox<Integer> choiceBoxYear;                   //出生年
    @FXML
    private ChoiceBox<Integer> choiceBoxMonth;                  //出生月
    @FXML
    private ChoiceBox<Integer> choiceBoxDay;                    //出生日

    @FXML
    private ImageView imageView;
    @FXML
    private Text textNotice;

    private byte[] icon; //头像

    public RegisterController() throws IOException {
        DataInputStream inputStream = new DataInputStream(
                Client.class.getResourceAsStream("images/profile_photo2.jpg"));
        icon = inputStream.readAllBytes();
    }

    @FXML
    private void initialize() {
        choiceBoxSex.getItems().addAll("男", "女");//设置姓别

        for (int i = 1970; i <= 2023; ++i) {
            choiceBoxYear.getItems().add(i);
        }
        for (int i = 1; i <= 12; ++i) {
            choiceBoxMonth.getItems().add(i);
        }
        imageView.setImage(ImageUtil.binaryStreamToImage(icon));
        choiceBoxMonth.setOnAction(e -> {
            int year = choiceBoxYear.getValue();
            int month = choiceBoxMonth.getValue();

            int[] monthOfAYear = {
                    31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
            };

            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                ++monthOfAYear[1];
            }

            choiceBoxDay.getItems().clear();
            for (int j = 0; j < monthOfAYear[month - 1]; ++j)
                choiceBoxDay.getItems().add(j + 1);
        });
    }

    @FXML
    private void onRegisterButtonClicked() throws IOException {
        String name = textFieldName.getText();
        String password = textFieldSetPassword.getText();
        String reenterPassword = textFieldSetReEnterPassword.getText();
        String sex = choiceBoxSex.getValue();
        Integer year = choiceBoxYear.getValue();
        Integer month = choiceBoxMonth.getValue();
        Integer day = choiceBoxDay.getValue();

        if (name.length() == 0 || name.length() > 10) {
            textNotice.setText("昵称长度必须在1-10个字");
        } else if (password.length() < 6) {
            textNotice.setText("密码长度必须大于等于6位");
        } else if (!reenterPassword.equals(password)) {
            textNotice.setText("两次输入的密码不一致");
        } else if (sex == null) {
            textNotice.setText("请选择性别");
        } else if (year == null || month == null || day == null) {
            textNotice.setText("请选择出生日期");
        } else {
            textNotice.setText("");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Client.class.getResource("RegisterComplete.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            ((RegisterCompleteController) loader.getController()).setStage((Stage) textFieldName.getScene().getWindow());

            Client.connect();
            RegisterRequest registerRequest = new RegisterRequest(Client.getInputStream(), Client.getOutputStream(),
                    password, name, year + "-" + month + "-" + day, sex, icon, "");
            registerRequest.sendMessageToServer();

            String number = registerRequest.getResult(false);
            ((RegisterCompleteController) loader.getController()).setNumber(number);
            stage.initOwner(imageView.getScene().getWindow());
            stage.setScene(scene);
            stage.showAndWait();
            Client.disconnect();
        }
    }

    @FXML
    private void onChooseImageButtonClicked() {
        Stage thisStage = (Stage) textFieldName.getScene().getWindow();
        FileChooser chooser = new FileChooser();//文件选择器
        chooser.setTitle("选择头像");

        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"), //所有格式类型过滤器
                new FileChooser.ExtensionFilter("JPG", "*.jpg"), //.jpg图片格式类型过滤器
                new FileChooser.ExtensionFilter("PNG", "*.png") //.png图片格式类型过滤器
        );

        File file = chooser.showOpenDialog(thisStage); //从电脑文件中自主选择头像
        if (file != null) {
            icon = ImageUtil.fileToBinaryStream(file);
            imageView.setImage(ImageUtil.binaryStreamToImage(icon));
        }
    }
}


