package com.learnit.controllers;

import com.jfoenix.controls.JFXButton;
import com.learnit.MainWindow;
import com.learnit.MyUtils;
import com.learnit.database.data.tables.Card;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardReviseFaceController implements Initializable {
    @FXML
    private BorderPane cardViewFace;
    @FXML
    private WebView question;
    @FXML
    private JFXButton changeToBack;

    private CardReviseBackController backController;
    private SimpleObjectProperty<Node> cardUi;
    private Card card;

    public CardReviseFaceController(SimpleObjectProperty<Node> cardUi){
        cardViewFace = new BorderPane();
        backController = new CardReviseBackController(cardUi, this);
        question = new WebView();
        this.cardUi = cardUi;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeToBack.setOnMousePressed(event -> {
          if (event.isPrimaryButtonDown()) toBackside();
        });
        question.setOnMousePressed(mouseEvent->{
            if(mouseEvent.isPrimaryButtonDown()) toBackside();
        });

        question.contextMenuEnabledProperty().setValue(false);
        setData(card);

        //deleting scrool bars
        question.getEngine().getLoadWorker().stateProperty().addListener((o, old, state) -> {
            if (state == Worker.State.RUNNING || state == Worker.State.SUCCEEDED) question.getEngine().executeScript("document.body.style.overflow = 'hidden';");
        });
    }

    public void setData(Card card){
        this.card = card;
        question.getEngine().loadContent(MyUtils.getCentredHtml(card.getQuestion()));
    }

    public void toBackside(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            backController.setData(card);
            fxmlLoader.setController(backController);
            fxmlLoader.setLocation(MainWindow.class.getResource("CardReviseBack.fxml"));

            BorderPane bp = fxmlLoader.load(); //loading face of card
            //BorderPane borderPane = FXMLLoader.load(MainWindow.class.getResource("CardReviseBack.fxml")); //loading back of card
            cardUi.setValue(bp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
