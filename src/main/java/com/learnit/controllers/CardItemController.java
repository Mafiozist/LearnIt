package com.learnit.controllers;

import com.learnit.MainWindow;
import com.learnit.datasets.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

//This controller will be able to change data and apperience of card view
public class CardItemController implements Initializable {
    @FXML
    public VBox cardVBox;
    @FXML
    public GridPane tagsGridPane;
    @FXML
    public Label questionLabel;

    public CardItemController(){

    }

    //The method uses for setting data from card (Card.java) into CardItem.fxm by serializing
    public void setData(Card card) throws IOException {
        questionLabel.setText(card.getQuestion());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(MainWindow.class.getResource("TagItem.fxml"));
                HBox hBox = fxmlLoader.load();



                tagsGridPane.setAlignment(Pos.CENTER);
                //hBox.lookup("#tLabel").
                tagsGridPane.add(hBox, i, j);

            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
