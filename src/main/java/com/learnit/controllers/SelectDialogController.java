package com.learnit.controllers;

import com.jfoenix.controls.*;
import com.learnit.MyUtils;
import com.learnit.database.data.tables.Book;
import com.learnit.database.data.tables.Tag;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;

public class SelectDialogController implements Initializable {
    @FXML
    private VBox vBox, innerVBox;
    @FXML
    private TextField textField;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private StackPane stackPane;

    private ObservableList<Tag> tags;
    private ObservableList<Book> books;
    private ObservableList<JFXCheckBox> observableList, changedObservableList;
    private FilteredList<JFXCheckBox> filteredList;

    private Book currentBook;

    public SelectDialogController(){
        observableList = FXCollections.observableList(new ArrayList<>());
        changedObservableList = FXCollections.observableList(new ArrayList<>());
        vBox = new VBox();
        innerVBox = new VBox();
        textField = new TextField();
        scrollPane = new ScrollPane();
        stackPane = new StackPane();
    }
    public SelectDialogController(ObservableList<Tag> tags) {
        this();
        this.tags = tags;
    }

    public SelectDialogController(List<Book> books){
        this();
        //this.books =  books;
    }

    //Повторяющийся код, пока не придумал как исправить
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(tags != null) {
            addTagsToUi(tags);
        }

        if(books != null) {
            addBooksToUi(books);
        }

        textField.textProperty().addListener((observableValue, old, current) -> {
            Predicate<JFXCheckBox> contains = i-> i.getText().toLowerCase().contains(current.toLowerCase(Locale.ROOT));

            filteredList.setPredicate(contains);
            if(current.isEmpty() || current.isBlank()) filteredList.setPredicate(null);
            innerVBox.getChildren().clear();
            innerVBox.getChildren().addAll(filteredList);
        });

    }

    public ArrayList<Tag> getChangedTags(){
        ArrayList<Tag> tags = new ArrayList<>();
        for (JFXCheckBox c: changedObservableList) {
            tags.add((Tag) c.getUserData());
        }
        //System.out.println("Changed tags: " + tags);
        return tags;
    }

    public ArrayList<Book> getChangedBooks(){
        ArrayList<Book> books = new ArrayList<>();
        for (JFXCheckBox c: changedObservableList) {
            books.add((Book) c.getUserData());
        }
        //System.out.println("Changed books: " + books);
        return books;
    }

    public SelectDialogController setBooks(ObservableList<Book> books) {
        this.books = books;
        addBooksToUi(books);
        return this;
    }

    public SelectDialogController setTags(ObservableList<Tag> tags){
        this.tags = tags;
        addTagsToUi(tags);
        return this;
    }

    private void addTagsToUi(ObservableList<Tag> tags){
        observableList.clear();
        for (Tag tag : tags) {
            JFXCheckBox jfxCheckBox = new JFXCheckBox(tag.getName());
            jfxCheckBox.setUserData(tag);

            jfxCheckBox.setOnMouseClicked(event -> {
                if (changedObservableList.contains(jfxCheckBox)) changedObservableList.remove(jfxCheckBox);
                else {
                    changedObservableList.add(jfxCheckBox);
                }
            });

            jfxCheckBox.setPadding(new Insets(5));
            observableList.add(jfxCheckBox);
        }

        //If there is nothing but zero tags
        if(observableList.size() == 0) {
            MyUtils.openMessageDialog(stackPane, "Внимание!", "Список пуст. Вы можете создать теги или книги через отдкльную вкладку.");
        }
        filteredList = new FilteredList<>(observableList);
        innerVBox.getChildren().addAll(filteredList);
    }

    private void addBooksToUi(ObservableList<Book> books){
        observableList.clear();
        for (Book book : books) {
            JFXCheckBox jfxCheckBox = new JFXCheckBox(book.getName());
            jfxCheckBox.setUserData(book);

            jfxCheckBox.setOnMouseClicked(event -> {
                if (changedObservableList.contains(jfxCheckBox)) changedObservableList.remove(jfxCheckBox);
                else {
                    changedObservableList.add(jfxCheckBox);
                }
            });

            jfxCheckBox.setPadding(new Insets(5));
            observableList.add(jfxCheckBox);
        }

        //If there is nothing but zero tags
        if(observableList.size() == 0) {
            MyUtils.openMessageDialog(stackPane, "Внимание!", "Список пуст. Вы можете создать теги или книги через отдкльную вкладку.");
        }
        filteredList = new FilteredList<>(observableList);
        innerVBox.getChildren().addAll(filteredList);
    }

    public void updateUi() {
        if(currentBook!= null){
            for (JFXCheckBox checkBox : filteredList) {
                checkBox.selectedProperty().setValue(currentBook.getTags().contains((Tag) checkBox.getUserData()));
            }
        }

    }

    public void setCurrentBook(Book currentBook) {
        this.currentBook = currentBook;
    }
}
