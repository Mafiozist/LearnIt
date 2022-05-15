package com.learnit.controllers;

import com.jfoenix.controls.JFXButton;
import com.learnit.database.data.tables.Book;
import com.learnit.database.data.tables.Card;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateEditCardWindowController implements Initializable {
    @FXML
    private HTMLEditor answer,question;
    @FXML
    private WebView answerView,questionView;
    private Card card;
    private Book book;
    @FXML
    private JFXButton save;

    public CreateEditCardWindowController() {
    }

    public CreateEditCardWindowController(Card card) {
        this.card = card;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        answer.setOnMouseExited(mousePressed->{
            answerView.getEngine().loadContent(getCentredHtml(answer.getHtmlText()));
        });
        question.setOnMouseExited(mousePressed->{
            questionView.getEngine().loadContent(getCentredHtml(question.getHtmlText()));
        });
    }

    private String getCentredHtml(String html){
        Document document = Jsoup.parse(html);
        Element head = document.head();
            head.appendElement("style");
            head.select("style").
                    append("\nhtml,body {\n" +
                            "\theight: 100%;\n" +
                            "\twidth: 100%;\n}\n").
                    append(".container {\n" +
                            "\talign-items: center;\n" +
                            "\tdisplay: flex;\n" +
                            "\tjustify-content: center;\n" +
                            "\theight: 100%;\n" +
                            "\twidth: 100%;\n" +
                            "}\n");

        Element body = document.body();
        //crating the new elements to center the info
        Element divWrapper = new Element("div");
        Element div = new Element("div");

        divWrapper.addClass("container");
        div.addClass("content");
        div.appendChild(body);
        divWrapper.appendChild(div);



        //System.out.println(divWrapper.outerHtml());
        body.html(divWrapper.outerHtml());
        document.body().html(body.html());

        //System.out.println(document.html());
        return document.html();
    }

    public void setData(Card card, Book book){ //The book needs to get ids and make reference between book and cards
        this.card = card;
        this.book = book;
        question.setHtmlText(String.format("<html dir=\"ltr\"><head></head><body contenteditable=\"true\">%s</body></html>", card.getQuestion()));
        answer.setHtmlText(String.format("<html dir=\"ltr\"><head></head><body contenteditable=\"true\">%s</body></html>", card.getAnswer()));
    }
}
