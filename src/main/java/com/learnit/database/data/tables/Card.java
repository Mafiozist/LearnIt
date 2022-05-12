package com.learnit.database.data.tables;

public class Card {
    private String question;
    private String answer;

    public Card(){
        question = "Вопрос или не вопрос! Вот где зарыт крот!";
        answer = "Answer";
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
