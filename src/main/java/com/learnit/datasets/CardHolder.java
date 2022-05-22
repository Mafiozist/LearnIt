package com.learnit.datasets;

import com.learnit.MyUtils;
import com.learnit.database.connection.OfflineDatabaseConnection;
import com.learnit.database.data.tables.Book;
import com.learnit.database.data.tables.Card;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

//The base class for all further realization of i
public class CardHolder {
    private static CardHolder cardHolder;
    private ObservableList<Card> cards;
    private String getCardsQry = String.format("SELECT cid,question,answer,nextRepetition,baseinterval FROM %s;","cards");
    private final String[] cardTables = new String[] {"repetition_stats", "book-card", "card-tag", "cards"}; //right order do matter

    private SimpleIntegerProperty cardSize;


    private CardHolder(){
        try {
            ArrayList<Card> cardArrayList = new ArrayList<>();
            Connection connection = OfflineDatabaseConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getCardsQry);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Card card = new Card();
                card.setId(resultSet.getInt(1)).setQuestion(resultSet.getString(2))
                        .setAnswer(resultSet.getString(3)).setNextDate(resultSet.getDate(4))
                        .setBaseInterval(resultSet.getFloat(5));
                cardArrayList.add(card);
            }

            cards = FXCollections.observableList(cardArrayList);
            cardSize = new SimpleIntegerProperty(cards.size());
            resultSet.close();
        } catch (NullPointerException|SQLException exception){
            exception.printStackTrace();
        }
    }

    public ObservableList<Card> getCards() {
        return cards;
    }

    public static CardHolder getInstance(){
        if(cardHolder == null) cardHolder = new CardHolder();
        return cardHolder;
    }
    // TODO: 12.05.2022 get card by bookname or id
    // TODO: 12.05.2022 get card by tagname or id
    // TODO: 12.05.2022 remove card and update card

    public CardHolder addCard(Card card, Book book){
        if(MyUtils.executeQuery(String.format(Locale.ROOT,"INSERT INTO cards(question, answer, baseinterval) VALUES('%s','%s', %.2f );",card.getQuestion(), card.getAnswer(), 1.3f))){
            //if card was added there is need to make reference in many-to-many table from book to card

            int id = MyUtils.getLastAddedId("cards","cid");
            card.setId(id);

            if(MyUtils.executeQuery(String.format(Locale.ROOT,"INSERT INTO `book-card` VALUES(%d,%d);",book.getId(),card.getId()))){
                System.out.println("Card added and reference are connected");
                cards.add(card);
                cardSize.set(cards.size());
            }
        }
        return this;
    }

    public CardHolder removeCardWithStatistics(Card card){

        for (String table: cardTables) {
            MyUtils.executeQuery(String.format(Locale.ROOT,"DELETE FROM `%s` WHERE cid='%d'",table,card.getId()));
        }

        return this;
    }

    public CardHolder removeCard(Card card){
        MyUtils.executeQuery(String.format(Locale.ROOT,"UPDATE TABLE `cards` SET isdeleted =`1` WHERE cid='%d'",card.getId()));
        return this;
    }
    public CardHolder updateCard(Card card){
        //MyUtils.executeQuery(String.format(Locale.ROOT,"UPDATE TABLE `cards`(//////////////////////) SET WHERE cid='%d'",card.getId()));
        return this;
    }

    public SimpleIntegerProperty getCardSizeProperty() {
        return cardSize;
    }
}
