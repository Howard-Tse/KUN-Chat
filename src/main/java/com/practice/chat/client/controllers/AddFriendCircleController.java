package com.practice.chat.client.controllers;

import com.practice.chat.user.User;
import com.practice.chat.utils.ImageUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AddFriendCircleController {
    @FXML
    private Button addPicture;
    @FXML
    private ImageView mainPicture;
    @FXML
    private TextField mainText;
    @FXML
    private Button show;

    private byte[] photo;

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    void onChoosePhotoButtonClicked() {
        Stage thisStage = (Stage) mainPicture.getScene().getWindow();
        FileChooser chooser = new FileChooser();//文件选择器
        chooser.setTitle("选择头像");

        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"), //所有格式类型过滤器
                new FileChooser.ExtensionFilter("JPG", "*.jpg"), //.jpg图片格式类型过滤器
                new FileChooser.ExtensionFilter("PNG", "*.png") //.png图片格式类型过滤器
        );

        File file = chooser.showOpenDialog(thisStage); //从电脑文件中自主选择头像
        if (file != null) {
            photo = ImageUtil.fileToBinaryStream(file);
            mainPicture.setImage(ImageUtil.binaryStreamToImage(photo));
        }
    }

    @FXML
    void onShareButtonClicked() {

    }
}
