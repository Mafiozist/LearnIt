package com.learnit.controllers;

import com.jfoenix.transitions.JFXFillTransition;
import com.learnit.database.data.tables.Book;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

//Set data to library card appearance
public class LibraryItemController implements Initializable {
    @FXML
    public ImageView imgView;
    @FXML
    public Label name;
    @FXML
    public VBox lCard;
    public Book book;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lCard.setOnMouseEntered(event -> {
            JFXFillTransition fillTransition = new JFXFillTransition();
            fillTransition.setRegion(lCard);
            fillTransition.setFromValue(Color.WHITE);
            fillTransition.setToValue(Color.AQUA);
            fillTransition.setDuration(Duration.millis(500));
            fillTransition.play();
        });

        lCard.setOnMouseEntered(event -> {
            JFXFillTransition fillTransition = new JFXFillTransition();
            fillTransition.setRegion(lCard);
            fillTransition.setFromValue(Color.AQUA);
            fillTransition.setToValue(Color.WHITE);
            fillTransition.setDuration(Duration.millis(500));
            fillTransition.play();
        });
    }

    public void setBook(Book book){
        this.book = book;
        name.setText(this.book.getName());
        imgView.setImage(this.book.getTitleImg());
    }
}
