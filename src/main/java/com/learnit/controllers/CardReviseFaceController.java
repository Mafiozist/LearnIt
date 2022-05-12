package com.learnit.controllers;

import com.jfoenix.controls.JFXButton;
import com.learnit.MainWindow;
import com.learnit.database.data.tables.Card;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.Queue;
import java.util.ResourceBundle;

public class CardReviseFaceController implements Initializable {
    @FXML
    BorderPane cardViewFace;
    @FXML
    Label label;
    @FXML
    JFXButton changeToBack;

    CardReviseBackController backController;
    BorderPane root;
    Card card;

    public CardReviseFaceController(BorderPane root){
        cardViewFace = new BorderPane();
        label = new Label();
        backController = new CardReviseBackController(root, this);

        this.root = root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeToBack.setOnMousePressed(event -> {
          if (event.isPrimaryButtonDown()) toBackside();
        });

        label.setText(card.getQuestion());
    }

    public void setData(Card card){
        this.card = card;
        label.setText(card.getQuestion());
    }

    public void toBackside(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            backController.setData(card);
            fxmlLoader.setController(backController);
            fxmlLoader.setLocation(MainWindow.class.getResource("CardReviseBack.fxml"));

            BorderPane bp = fxmlLoader.load(); //loading face of card
            //BorderPane borderPane = FXMLLoader.load(MainWindow.class.getResource("CardReviseBack.fxml")); //loading back of card
            root.setCenter(bp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
