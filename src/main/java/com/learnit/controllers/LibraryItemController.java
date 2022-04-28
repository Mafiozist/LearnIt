package com.learnit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

//Set data to library card appearance
public class LibraryItemController {
    @FXML
    public ImageView imgView;
    @FXML
    public Label name;
    @FXML
    public VBox lCard;

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setImg(Image img) {
        this.imgView.setImage(img);
    }
}
