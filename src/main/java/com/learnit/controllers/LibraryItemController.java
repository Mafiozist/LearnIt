package com.learnit.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.learnit.MainWindow;
import com.learnit.database.data.tables.Book;
import com.learnit.datasets.Library;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

//Set data to library card appearance
public class LibraryItemController implements Initializable {

    @FXML private JFXButton delete;
    @FXML private ImageView imgView;
    @FXML private Label name;
    @FXML private VBox vBox;
    @FXML BorderPane borderPane;
    @FXML StackPane stackPane;

    private Book book;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setBook(Book book){
        this.book = book;
        name.setText(book.getName());
        name.textProperty().bind(book.getNameProperty());
        imgView.setImage(this.book.getTitleImg());
    }

    public void changeName(MouseEvent event) {
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Text("Введите новое название:"));
        TextField textField = new TextField(book.getName());
        layout.setBody(textField);
        JFXButton ok = new JFXButton("Сохранить");
        JFXButton cancel = new JFXButton("Отменить");
        layout.setActions(ok,cancel);

        JFXDialog jfxDialog = new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.CENTER);
        jfxDialog.show();

        ok.setOnMousePressed(pressed -> {
            if (pressed.isPrimaryButtonDown()) {
                book.setName(textField.getText());
                jfxDialog.close();
            }
        });

        cancel.setOnMousePressed(pressed -> {
            if(pressed.isPrimaryButtonDown()){
                jfxDialog.close();
            }
        });
    }
}
