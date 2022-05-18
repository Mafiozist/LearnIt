package com.learnit.controllers;


import com.learnit.MainWindow;
import com.learnit.database.data.tables.Card;
import com.learnit.datasets.CardHolder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CardWindowController implements Initializable {
    @FXML
    private TilePane tilePane;


    //I could use lookup to change settings from diff nodes if it will be needed
    public void initialize(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Card> cardHolders = new ArrayList<>(CardHolder.getInstance().getCards());

        for (Card cardHolder : cardHolders) {
            //loading card
            FXMLLoader fxmlCardLoader = new FXMLLoader();
            fxmlCardLoader.setLocation(MainWindow.class.getResource("CardItem.fxml"));

            //loading tag
            FXMLLoader fxmlTagLoader = new FXMLLoader(MainWindow.class.getResource("TagItem.fxml"));

            try {
                VBox vBox = fxmlCardLoader.load();
                CardItemController itemController = fxmlCardLoader.getController();

                //
                itemController.setData(cardHolder);

                tilePane.getChildren().add(vBox);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        // TODO: 18.05.2022 remove from UI  
        // TODO: 18.05.2022 add card to ui 
        // TODO: 18.05.2022 open card, apply tags to it, apply books to it 

    }
}
