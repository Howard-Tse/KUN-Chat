package com.practice.chat.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RegisterCompleteController {
    @FXML
    private TextField textFieldNumber;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setNumber(String number) {
        textFieldNumber.setText(number);
    }

    @FXML
    private void onGoToLoginButtonClicked() {
        ((Stage) textFieldNumber.getScene().getWindow()).close();
        stage.close();
    }
}
