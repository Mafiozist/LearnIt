package com.learnit.controllers;


import com.learnit.MainWindow;
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
    TilePane tilePane;


    //I could use lookup to change settings from diff nodes if it will be needed
    public void initialize(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<CardHolder> cardHolders = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            cardHolders.add(new CardHolder());
        }

        for (int i = 0; i < cardHolders.size(); i++) {
            //loading card
            FXMLLoader fxmlCardLoader = new FXMLLoader();
            fxmlCardLoader.setLocation(MainWindow.class.getResource("CardItem.fxml"));

            //loading tag
            FXMLLoader fxmlTagLoader = new FXMLLoader(MainWindow.class.getResource("TagItem.fxml"));

            try {
                VBox vBox = fxmlCardLoader.load();
                CardItemController itemController = fxmlCardLoader.getController();

                //
                itemController.setData(cardHolders.get(i));

                tilePane.getChildren().add(vBox);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }



    }
}
