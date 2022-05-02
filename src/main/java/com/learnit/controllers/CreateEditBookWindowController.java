package com.learnit.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.learnit.MainWindow;
import com.learnit.database.data.tables.Book;
import com.learnit.database.data.tables.Tag;
import com.learnit.datasets.TagHolder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CreateEditBookWindowController implements Initializable {
    @FXML
    public HTMLEditor htmlEditor;
    @FXML
    public BorderPane borderPane; //for simple information alignment
    @FXML
    public StackPane stackPane; //for Jfx dialogs
    LibraryWindowController controller; //for yes button

    Book book;
    int[] IdChangedTags;
    Node node;
    ToolBar bar;
    Button tagButton;

    public CreateEditBookWindowController(){
        tagButton = new Button();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WebView webview = (WebView) htmlEditor.lookup("WebView");
        GridPane.setHgrow(webview, Priority.ALWAYS);
        GridPane.setVgrow(webview, Priority.ALWAYS);
        
        tagButton.setOnMousePressed(event -> {
            if(event.isPrimaryButtonDown())  {
                StackPane layout;
                try {
                    //Нужно что-то сделать с повторяющимся кодом - это плохо, но времени уже мало...
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(MainWindow.class.getResource("TagChangeDialog.fxml"));
                    layout = fxmlLoader.load();
                    TagChangeWindowController controller = fxmlLoader.getController();

                    //Костыль
                    JFXButton button = new JFXButton("Save");
                    VBox vBox = (VBox) layout.lookup("#vBox");
                    vBox.getChildren().add(button);
                    /////////

                    JFXDialog jfxDialog = new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.TOP);
                    jfxDialog.show();

                    button.setOnMousePressed(closeEvent -> {

                        IdChangedTags = TagHolder.getInstance().getTagsIdsByNames(controller.getNamesChangedTags());

                        //System.out.println(tagsIds.length);
                        jfxDialog.close();
                    });

                } catch (NullPointerException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        //Adding tag button to html editor for adding tags to current book
        // TODO: 30.04.2022 need to add another button to html editor for changing title image and another one for creating card from selected text
        bar = null;
        node = htmlEditor.lookup(".top-toolbar");

        if (node instanceof ToolBar) {
            bar = (ToolBar) node;
            System.out.println( "Size before layout pass: "+bar.getItems().size());

            if (bar != null) {
                try {
                    ImageView imageView = new ImageView(new Image( String.valueOf(MainWindow.class.getResource("images/tag.png").toURI()) ));
                    imageView.setFitHeight(16);
                    imageView.setFitWidth(16);
                    tagButton.setGraphic(imageView);
                } catch (IllegalArgumentException | URISyntaxException ex){
                    ex.printStackTrace();
                }

                //Костыль на отображение собственных кнопок
                htmlEditor.setOnMouseClicked(event -> {
                    System.out.println( "Size before adding: "+bar.getItems().size());
                    if(bar.getItems().size() == 17) bar.getItems().add(tagButton); //костыль
                    System.out.println( "Size after layout pass: "+bar.getItems().size());
                });

            }
        }

    }

    public void setHtmlEditorText(String htmlText) {
        htmlEditor.setHtmlText(htmlText);
    }

    public void setBook(Book book){
        this.book = book;
        setHtmlEditorText(book.getHtmlText());
    }

}
