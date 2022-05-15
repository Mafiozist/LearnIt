package com.learnit.controllers;

import com.jfoenix.controls.JFXRadioButton;
import com.learnit.MainWindow;
import com.learnit.database.data.tables.Card;
import com.learnit.datasets.CardHolder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
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
    private BorderPane root;
    @FXML
    private ToggleGroup revisingState;
    @FXML
    private JFXRadioButton all,booksOnly,tagsOnly;
    @FXML
    private Queue<Card> cards;

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
            RadioButton rb = (RadioButton) t1.getToggleGroup().getSelectedToggle();
            switch (rb.getText()) {
                case "Все":
                    //cards.addAll(FXCollections.observableList(CardHolder.getCards()));

                    break;


                case "Книги":
                    // TODO: 12.05.2022 there is feature for filtering cards by books 
                    
                    break;


                case "Тэги":
                    // TODO: 12.05.2022 there is feature for filtering cards by tags
                    
                    break;

            }
        });
    }




}
