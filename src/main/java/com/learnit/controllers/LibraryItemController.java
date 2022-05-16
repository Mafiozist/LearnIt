package com.learnit.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.learnit.MainWindow;
import com.learnit.database.data.tables.Book;
import com.learnit.datasets.Library;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

//Set data to library card appearance
public class LibraryItemController implements Initializable {

    @FXML private JFXButton delete;
    @FXML private ImageView imgView;
    @FXML private Label name;
    @FXML private VBox vBox;
    @FXML BorderPane borderPane;

    private Book book;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setBook(Book book){
        this.book = book;
        name.setText(this.book.getName());
        imgView.setImage(this.book.getTitleImg());
    }
}
