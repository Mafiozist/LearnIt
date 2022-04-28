package com.learnit.datasets;

import com.learnit.datasets.TagHolder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//The base class for all further realization of i
public class CardHolder {
    private int id, dbId; // for possess cardHolders into revising groups
    private int width, height; //These attributes should be in common for all the instances
    private String question;
    private String text; //the main information for revising
    private TagHolder tags;

    public CardHolder(){
        id = new Random().nextInt();
        dbId= new Random().nextInt();
        //groupId = new Random().nextInt();
        width = 500;
        height = 500;
        question = "This is an initial question.";
        text = "This is an initial text. You always could change it.";

        try {
            tags = TagHolder.getInstance();

        } catch (SQLException ex){
            ex.printStackTrace();
        }

    }

    //Unique card id needs to identify a card
    public int getId() {
        return id;
    }

    public void setCardId(int cardId) {
        id = cardId;
    }

    //Unique card id for db operations
    public int getCardDbId() {
        return dbId;
    }

    public void setCardDbId(int cardDbId) {
        this.dbId = cardDbId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
