package com.learnit.controllers;

import com.jfoenix.controls.*;
import com.learnit.database.data.tables.Book;
import com.learnit.database.data.tables.Tag;
import com.learnit.datasets.TagHolder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.xml.transform.Source;
import java.lang.reflect.Array;
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

    private ArrayList<Tag> tags;
    private ArrayList<Book> books;
    private ObservableList<JFXCheckBox> observableList, changedObservableList;
    private FilteredList<JFXCheckBox> filteredList;

    public SelectDialogController(){
        observableList = FXCollections.observableList(new ArrayList<>());
        changedObservableList = FXCollections.observableList(new ArrayList<>());
    }
    public SelectDialogController(ArrayList<Tag> tags) {
        this();
        this.tags = tags;
    }

    public SelectDialogController(List<Book> books){
        this();
        this.books = (ArrayList<Book>) books;
    }


    //Повторяющийся код, пока не придумал как исправить
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tags = TagHolder.getInstance().getTags();

        if(tags != null) {
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
        }

        if(books != null) {
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
        }


        filteredList = new FilteredList<>(observableList);
        innerVBox.getChildren().addAll(filteredList);

        //If there is nothing but zero tags
        if(innerVBox.getChildren().size() == 0) {
                JFXDialogLayout layout = new JFXDialogLayout();
                layout.setHeading(new Text("Внимание!"));
                layout.setBody(new Text("Список пуст. Вы можете создать теги или книги через отдкльную вкладку."));

                JFXButton ok = new JFXButton("Ok");

                JFXDialog jfxDialog = new JFXDialog(stackPane, innerVBox, JFXDialog.DialogTransition.CENTER);
                jfxDialog.setContent(layout);
                jfxDialog.show();

                ok.setOnMousePressed(event -> {
                    if(event.isPrimaryButtonDown()) {
                        jfxDialog.close();
                    }
                });
                layout.setActions(ok);
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
        return tags;
    }

    public ArrayList<Book> getChangedBooks(){
        ArrayList<Book> books = new ArrayList<>();
        for (JFXCheckBox c: changedObservableList) {
            books.add((Book) c.getUserData());
        }
        return books;
    }
}
