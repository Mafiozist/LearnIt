package com.learnit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TagItemController implements Initializable {
    @FXML
    public HBox tHBox;
    @FXML
    public Label tLabel;
    @FXML
    public BorderPane borderLabelPane ,borderImgPane;
    @FXML
    public ImageView img;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tLabel = new Label("Welcome!");
    }
}
