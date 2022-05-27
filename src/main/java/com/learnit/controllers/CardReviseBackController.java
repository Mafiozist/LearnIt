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
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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
    private SimpleObjectProperty<Node> cardUiProperty;
    private Card card;

    public CardReviseBackController(SimpleObjectProperty<Node> card, CardReviseFaceController faceController){ // this is root element of  RevisingWindowController
        this.cardUiProperty = card;
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

        //deleting scroll bars
        answer.getEngine().getLoadWorker().stateProperty().addListener((o, old, state) -> {
            if (state == Worker.State.RUNNING || state == Worker.State.SUCCEEDED) answer.getEngine().executeScript("document.body.style.overflow = 'hidden';");
        });

        repeat.setOnMouseClicked(pressed->{

                //hard or repeat -> setNextRevisingDate = Settings.learningPhaseMinutes[0]
                //Date date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                // if any card from revising stage will be pressed at again btn the stage is change to learning
                // and base multiplier reduses from revisingBaseMultiplier by .2 and minimum 1.3


                LocalDateTime localDateTime = LocalDateTime.now();
                localDateTime = localDateTime.plusSeconds(15);

                System.out.println("new date "+ localDateTime);

                //change learning state
                //add statistics to db
                card.setNextDate(convertToDateViaInstant(localDateTime));
                removeOldCard();
        });

        hard.setOnMousePressed(pressed->{
            if(pressed.isForwardButtonDown()){
                //hard or repeat -> setNextRevisingDate = Settings.learningPhaseMinutes[0]

                // if hard btn was pressed (new iterval=current* default hard interval modifier) and -.15 from revisingBaseMultiplier minimum 1.3
                removeOldCard();
            }
        });

        good.setOnMousePressed(pressed->{
            if(pressed.isForwardButtonDown()){
                //if pressed good -> setNextRevisingDate = Settings.learningPhaseMinutes[1]
                //if another pressed good -> setNextRevisingDate = Settings.learningPhaseMinutes[2]
                //And only after that the card moves to revising stage


                // if good was pressed (new iterval=current* revisingBaseMultiplier) and no effect to revisingBaseMultiplier
                removeOldCard();
            }
        });

        easy.setOnMousePressed(pressed->{
            if(pressed.isForwardButtonDown()){
                //easy btn -> next revising stage if person now at learning phase

                // if easy btn was pressed ( new iterval=current* revisingBaseMultiplier) and make an effect on revisingBaseMultiplier +0.15
                removeOldCard();
            }
        });


    }

    private void removeOldCard(){
        cardUiProperty.setValue(null);
        //RevisingWindowController.setiSUiNull(true);
    }

    public void toFace(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setController(faceController);
            fxmlLoader.setLocation(MainWindow.class.getResource("CardReviseFace.fxml"));
            BorderPane borderPane = fxmlLoader.load();

            cardUiProperty.setValue(borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LocalDateTime convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date.from(dateToConvert.
                atZone(ZoneId.systemDefault()).
                toInstant());
    }

    public void setData(Card card){
        this.card = card;
        answer.getEngine().loadContent(MyUtils.getCentredHtml(card.getAnswer()));
    }

    public void useCard(){



    }


    public void changeStage(){

    }


}
