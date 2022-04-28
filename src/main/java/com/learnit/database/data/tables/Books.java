package com.learnit.database.data.tables;

import java.util.ArrayList;

public class Books {

    private static Books books;
    private ArrayList<Tags> tags;

    private Books(){

    }

    public Books getInstance(){
        if(books == null) books = new Books();
        return books;
    }


}
