package com.practice.chat.client.controllers;

import com.practice.chat.client.Client;
import com.practice.chat.requestmessage.ModifyProfileRequest;
import com.practice.chat.user.User;
import com.practice.chat.utils.ImageUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

public class EditDataController {
    @FXML
    private TextField textFieldName;                   //姓名
    @FXML
    private ChoiceBox<String> choiceBoxSex;                    //性别
    @FXML
    private ChoiceBox<Integer> choiceBoxYear;                   //出生年
    @FXML
    private ChoiceBox<Integer> choiceBoxMonth;                  //出生月
    @FXML
    private ChoiceBox<Integer> choiceBoxDay;                    //出生日
    @FXML
    private TextField textFieldSignature;              //个性签名
    @FXML
    private Text textNotice;                           //隐藏弹窗
    @FXML
    private ImageView imageView;                       //头像

    private byte[] icon;

    private User user;

    public void setUser(User user) {
        this.user = user;

        imageView.setImage(ImageUtil.binaryStreamToImage(user.getIcon()));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String birthday = user.getBirthday();
        try {
            Date date = simpleDateFormat.parse(birthday);
            String year = String.format("%ty", date);
            String month = String.format("%tM", date);
            String day = String.format("%td", date);

            choiceBoxSex.getSelectionModel().select(user.getSex());

            choiceBoxYear.getSelectionModel().select(Integer.parseInt(year));
            choiceBoxMonth.getSelectionModel().select(Integer.parseInt(month));

            calculateDayOfMonth();
            choiceBoxDay.getSelectionModel().select(Integer.parseInt(day));

            textFieldSignature.setText(user.getSignature());
            textFieldName.setText(user.getNickname());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize() {
        choiceBoxSex.getItems().addAll("男", "女");//设置姓别

        for(int i = 1970; i <= 2023; ++i) {
            choiceBoxYear.getItems().add(i);
        }
        for (int i = 1; i <= 12; ++i) {
            choiceBoxMonth.getItems().add(i);
        }

        choiceBoxMonth.setOnAction(e -> calculateDayOfMonth());
    }

    @FXML
    private void onEditDataButtonClicked() {
        String name = textFieldName.getText();

        if (name.length() < 1 || name.length() > 10) {
            textNotice.setText("昵称长度必须在1-10个字");
        } else {
            textNotice.setText("");

            user.setIcon(icon);
            user.setNickname(name);
            user.setSignature(textFieldSignature.getText());
            user.setSex(choiceBoxSex.getSelectionModel().getSelectedItem());

            Integer year = choiceBoxYear.getSelectionModel().getSelectedItem();
            Integer month = choiceBoxMonth.getSelectionModel().getSelectedItem();
            Integer day = choiceBoxDay.getSelectionModel().getSelectedItem();
            user.setBirthday(year + "-" + month + "-" + day);
            System.out.println(user.getBirthday());

            try {
                Client.connect();

                ModifyProfileRequest request = new ModifyProfileRequest(Client.getInputStream(), Client.getOutputStream(),
                        user.getNumber(), user);
                request.sendMessageToServer();

                request.getResult(false);

                Client.disconnect();
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
            alert.setContentText("修改成功");
            alert.showAndWait();
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

    private void calculateDayOfMonth() {
        if(choiceBoxYear.getValue() != null && choiceBoxMonth.getValue() != null) {
            int year = choiceBoxYear.getValue();
            int month = choiceBoxMonth.getValue();

            int[] monthOfAYear = {
                    31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
            };

            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                ++monthOfAYear[1];
            }

            choiceBoxDay.getItems().clear();
            for (int i = 0; i < monthOfAYear[month - 1]; ++i)
                choiceBoxDay.getItems().add(i + 1);
        }
    }
}
