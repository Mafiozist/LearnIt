package com.learnit.controllers;

import com.learnit.MainWindow;
import com.learnit.database.data.tables.Card;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.Queue;
import java.util.ResourceBundle;

public class RevisingWindowController implements Initializable {
    @FXML
    BorderPane root;
    @FXML
    ToggleGroup revisingState;
    @FXML
    Queue<Card> cards;

    public RevisingWindowController(){
        root = new BorderPane();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            CardReviseFaceController cardReviseFaceController = new CardReviseFaceController(root);
            cardReviseFaceController.setData(new Card());

            fxmlLoader.setController(cardReviseFaceController);
            fxmlLoader.setLocation(MainWindow.class.getResource("CardReviseFace.fxml"));
            BorderPane bp = fxmlLoader.load();
            root.setCenter(bp);
        } catch (IOException ex){
            ex.printStackTrace(); // TODO: 12.05.2022 alert can't load interface
        }

        revisingState.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {

        });
    }




}
