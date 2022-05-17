package com.learnit.controllers;

import com.jfoenix.controls.JFXRadioButton;
import com.learnit.MainWindow;
import com.learnit.MyUtils;
import com.learnit.database.data.tables.Card;
import com.learnit.datasets.CardHolder;
import com.learnit.datasets.Library;
import com.learnit.datasets.TagHolder;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class RevisingWindowController implements Initializable {
    @FXML private StackPane stackPane;
    @FXML private BorderPane root;
    @FXML private ToggleGroup revisingState;
    @FXML private JFXRadioButton all,booksOnly,tagsOnly;
    @FXML private Queue<Card> cards;

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
            /*if(CardHolder.getInstance().getCards().isEmpty()) {
                MyUtils.openMessageDialog(stackPane,"Внимание!","Нет карточек для работы.\n" +
                        "Вы можете добавить карточки прямо в библиотеке,\n" +
                        "из файла с которым работаете.");
                return;
            }*/

            switch (rb.getText()) {
                case "Все" ->

                        cards.addAll(FXCollections.observableList(CardHolder.getInstance().getCards()));

                case "Книги" ->
                        // TODO: 12.05.2022 there is feature for filtering cards by books


                        MyUtils.openBookSelectDialog(stackPane, Library.getInstance().getBooks());
                case "Тэги" ->
                        // TODO: 12.05.2022 there is feature for filtering cards by tags
                        MyUtils.openTagSelectDialog(stackPane, TagHolder.getInstance().getTags());
            }
        });
    }




}
