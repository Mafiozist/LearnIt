package com.learnit.controllers;

import com.jfoenix.controls.JFXButton;
import com.learnit.MainWindow;
import com.learnit.MyUtils;
import com.learnit.database.data.tables.Card;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardReviseBackController implements Initializable {
    @FXML
    private BorderPane cardViewBack;
    @FXML
    private JFXButton repeat, hard, good, easy, changeToFace;
    @FXML
    private HBox buttonContainer;
    @FXML
    private WebView answer;

    private CardReviseFaceController faceController;
    private BorderPane root;
    private Card card;

    public CardReviseBackController(BorderPane root, CardReviseFaceController faceController){ // this is root element of  RevisingWindowController
        this.root = root;
        this.faceController = faceController;
        answer = new WebView();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeToFace.setOnMousePressed(mouseEvent->{
            if(mouseEvent.isPrimaryButtonDown()) toFace();
        });
        answer.setOnMousePressed(mouseEvent->{
            if(mouseEvent.isPrimaryButtonDown()) toFace();
        });
        answer.contextMenuEnabledProperty().setValue(false);
        setData(card);

        //deleting scrool bars
        answer.getEngine().getLoadWorker().stateProperty().addListener((o, old, state) -> {
            if (state == Worker.State.RUNNING || state == Worker.State.SUCCEEDED) answer.getEngine().executeScript("document.body.style.overflow = 'hidden';");
        });
    }

    public void toFace(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setController(faceController);
            fxmlLoader.setLocation(MainWindow.class.getResource("CardReviseFace.fxml"));
            BorderPane borderPane = fxmlLoader.load();
            root.setCenter(borderPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setData(Card card){
        this.card = card;
        answer.getEngine().loadContent(MyUtils.getCentredHtml(card.getAnswer()));
    }

    public void useCard(){



    }
}
