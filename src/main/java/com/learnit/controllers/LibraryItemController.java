package com.learnit.controllers;

import com.learnit.database.data.tables.Book;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

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

    }

    public void setBook(Book book){
        this.book = book;
        name.setText(this.book.getName());
        imgView.setImage(this.book.getTitleImg());
    }
}
