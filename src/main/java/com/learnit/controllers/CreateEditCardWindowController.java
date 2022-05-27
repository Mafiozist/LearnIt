package com.learnit.controllers;

import com.jfoenix.controls.JFXButton;
import com.learnit.MyUtils;
import com.learnit.database.data.tables.Book;
import com.learnit.database.data.tables.Card;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateEditCardWindowController implements Initializable {
    @FXML private BorderPane root;
    @FXML private HTMLEditor answer,question;
    @FXML private WebView answerView,questionView;
    @FXML private JFXButton save;
    private Card card;
    private Book book;

    public CreateEditCardWindowController() {
        answer = new HTMLEditor();
        question = new HTMLEditor();
        answerView = new WebView();
        questionView = new WebView();

    }

    public CreateEditCardWindowController(Card card) {
        this();
        this.card = card;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        answer.setOnMouseExited(mousePressed-> answerView.getEngine().loadContent(MyUtils.getCentredHtml(answer.getHtmlText())));
        question.setOnMouseExited(mousePressed-> questionView.getEngine().loadContent(MyUtils.getCentredHtml(question.getHtmlText())));

        save.setOnMousePressed(event -> {
            if(event.isPrimaryButtonDown()) {
                card.setAnswer(answer.getHtmlText());
                card.setQuestion(question.getHtmlText());
                root.getScene().getWindow().hide();
            }
        });
    }



    public void setData(Card card, Book book){ //The book needs to get ids and make reference between book and cards
        this.card = card;
        this.book = book;

        question.setHtmlText(card.getQuestion());
        answer.setHtmlText(card.getAnswer());
        questionView.getEngine().loadContent(MyUtils.getCentredHtml(card.getQuestion()));
        answerView.getEngine().loadContent(MyUtils.getCentredHtml(card.getAnswer()));
    }

    public String getQuestion() {
        return question.getHtmlText();
    }

    public String getAnswer() {
        return answer.getHtmlText();
    }
}
