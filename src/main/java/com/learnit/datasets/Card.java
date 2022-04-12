package com.learnit.datasets;

import com.learnit.datasets.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//The base class for all further realization of i
public class Card {
    private int cardId, cardDbId, cardGroupId; // for possess cards into revising groups
    private int width, height; //These attributes should be in common for all the instances
    private String question;
    private String text; //the main information for revising
    private List<Tag> tags;

    public Card(){
        cardId = new Random().nextInt();
        cardDbId= new Random().nextInt();
        cardGroupId = new Random().nextInt();
        width = 500;
        height = 500;
        question = "This is an initial question.";
        text = "This is an initial text. You always could change it.";
        tags = new ArrayList<>();
    }

    //Unique card id needs to identify a card
    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    //Unique card id for db operations
    public int getCardDbId() {
        return cardDbId;
    }

    public void setCardDbId(int cardDbId) {
        this.cardDbId = cardDbId;
    }

    public int getCardGroupId() {
        return cardGroupId;
    }

    public void setCardGroupId(int cardGroupId) {
        this.cardGroupId = cardGroupId;
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

    public void setTags(Tag ... tags){
        this.tags.addAll(List.of(tags));
    }
    public List<Tag> getTags() {
        return tags;
    }
}
