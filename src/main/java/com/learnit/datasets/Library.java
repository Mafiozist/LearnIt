package com.learnit.datasets;

import com.learnit.database.data.tables.Books;
import com.learnit.database.data.tables.Tags;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

//it's use for containing and changing appearance of books
//parameters for appearance loads from disk directly and users can change them
public class Library {
    ArrayList<Books> books;
    ObservableList<Tags> tags;

    public Library(){
       books = new ArrayList<>();
        tags = FXCollections.emptyObservableList();
    }

}
