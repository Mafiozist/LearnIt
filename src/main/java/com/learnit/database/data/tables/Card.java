package com.learnit.database.data.tables;

import java.util.Date;

public class Card {
    private int id;
    private String question;
    private String answer;
    private double baseInterval;
    private Date nextRepetition;
    private int[] booksIds, tagsIds;

    public Card(){
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
    public int[] getBooksIds() {
        return booksIds;
    }
    public Card setBooksIds(int[] booksIds) {
        this.booksIds = booksIds;
        return this;
    }
    public int[] getTagsIds() {
        return tagsIds;
    }
    public Card setTagsIds(int[] tagsIds) {
        this.tagsIds = tagsIds;
        return this;
    }
    public Card setBaseInterval(float interval) {
        baseInterval = (double) interval;
        return this;
    }
}
