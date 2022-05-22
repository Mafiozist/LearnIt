package com.learnit.database.data.tables;

import java.util.ArrayList;
import java.util.Date;

public class Card {
    private int id;
    private String question;
    private String answer;
    private double baseInterval;
    private Date nextRepetition;

    private ArrayList<Tag> tags;
    private Book book;

    public Card(){
        tags = new ArrayList<>();
        book = null;
        id = -1;
        question = String.format("<html dir=\"ltr\"><head></head><body contenteditable=\"true\">%s</body></html>", "В чем смысл бытия?");
        answer = String.format("<html dir=\"ltr\"><head></head><body contenteditable=\"true\">%s</body></html>", "Ответ придется искать самому.");
        nextRepetition = new Date();
    }

    public Card setAnswer(String answer) {
        this.answer = answer;
        return this;
    }

    public Card setQuestion(String question) {
        this.question = question;
        return this;
    }

    public String getQuestion() {
        return question;
    }
    public String getAnswer() {
        return answer;
    }
    public Card setId(int id){
        this.id = id;
        return this;
    }
    public int getId(){
        return id;
    }
    public Card setNextDate (Date date){
        nextRepetition = date;
        return this;
    }
    public Date getNextRepetition() {
        return nextRepetition;
    }
    public Card setBaseInterval(float interval) {
        baseInterval = (double) interval;
        return this;
    }
}
