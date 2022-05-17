package com.learnit.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.learnit.MainWindow;
import com.learnit.database.data.tables.Book;
import com.learnit.database.data.tables.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateEditBookWindowController implements Initializable {
    @FXML
    private HTMLEditor htmlEditor;
    @FXML
    private BorderPane borderPane; //for simple information alignment
    @FXML
    private StackPane stackPane; //for Jfx dialogs
    LibraryWindowController controller; //for yes button

    private Book book;
    private int[] IdChangedTags; // the ids of tags that were changed at the book for using them to many-many table
    private Button tagButton,createCard;

    private static final String SELECT_TEXT_SCRIPT =
            "(function getSelectionText() {\n" +
                    "    var text = \"\";\n" +
                    "    if (window.getSelection) {\n" +
                    "        text = window.getSelection().toString();\n" +
                    "    } else if (document.selection && document.selection.type != \"Control\") {\n" +
                    "        text = document.selection.createRange().text;\n" +
                    "    }\n" +
                    "    if (window.getSelection) {\n" +
                    "      if (window.getSelection().empty) {  // Chrome\n" +
                    "        window.getSelection().empty();\n" +
                    "      } else if (window.getSelection().removeAllRanges) {  // Firefox\n" +
                    "        window.getSelection().removeAllRanges();\n" +
                    "      }\n" +
                    "    } else if (document.selection) {  // IE?\n" +
                    "      document.selection.empty();\n" +
                    "    }" +
                    "    return text;\n" +
                    "})()";
    private String selectedText;

    public CreateEditBookWindowController(){
        htmlEditor = new HTMLEditor();
        tagButton = new Button();
        createCard = new Button();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WebView webview = (WebView) htmlEditor.lookup("WebView");
        GridPane.setHgrow(webview, Priority.ALWAYS);
        GridPane.setVgrow(webview, Priority.ALWAYS);


        createCard.setOnMousePressed(pressedEvent->{
            if(pressedEvent.isPrimaryButtonDown()) {
                Object obj = webview.getEngine().executeScript("window.getSelection().toString()");

                if (obj instanceof String) {
                    selectedText = (String) obj;
                }
            }

            Card card = new Card();
            card.setAnswer(selectedText);
            openCardEditorDialog(card, book);
        });

        tagButton.setOnMousePressed(event -> {
            if(event.isPrimaryButtonDown())  {
                StackPane layout;
                try {
                    //Нужно что-то сделать с повторяющимся кодом - это плохо, но времени уже мало...
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(MainWindow.class.getResource("SelectDialog.fxml"));
                    layout = fxmlLoader.load();
                    SelectDialogController controller = fxmlLoader.getController();

                    //Костыль
                    JFXButton button = new JFXButton("Save");
                    VBox vBox = (VBox) layout.lookup("#vBox");
                    vBox.getChildren().add(button);
                    /////////

                    JFXDialog jfxDialog = new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.TOP);
                    jfxDialog.show();

                    button.setOnMousePressed(closeEvent -> {
                        //IdChangedTags = TagHolder.getInstance().getTagsIdsByNames(controller.getNamesChangedTags());
                        //System.out.println(tagsIds.length);
                        jfxDialog.close();
                    });

                } catch (NullPointerException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        //Adding tag button to html editor for adding tags to current book
        // TODO: 30.04.2022 need to add another button to html editor for changing title image
        ToolBar bar;
        Node node = htmlEditor.lookup(".top-toolbar");

        if (node instanceof ToolBar) {
            bar = (ToolBar) node;
            System.out.println( "Size before layout pass: "+bar.getItems().size());

            try {
                ImageView imageView = new ImageView(new Image( String.valueOf(MainWindow.class.getResource("images/tag.png").toURI()) ));
                imageView.setFitHeight(16);
                imageView.setFitWidth(16);
                tagButton.setGraphic(imageView);

                ImageView img = new ImageView(new Image( String.valueOf(MainWindow.class.getResource("images/AddCard.png").toURI()) ));
                img.setFitHeight(16);
                img.setFitWidth(16);
                createCard.setGraphic(img);

            } catch (IllegalArgumentException | URISyntaxException ex){
                ex.printStackTrace();
            }

            //Костыль на отображение собственных кнопок
            ToolBar finalBar = bar;
            htmlEditor.setOnMouseClicked(event -> { //костыль
                if(finalBar.getItems().size() == 17) finalBar.getItems().addAll(tagButton, createCard);
            });
        }

    }

    public void setHtmlEditorText(String htmlText) {
        htmlEditor.setHtmlText(htmlText);
    }

    public void setBook(Book book){
        this.book = book;
        setHtmlEditorText(book.getHtmlText());
    }

    public void openCardEditorDialog(Card card, Book book){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainWindow.class.getResource("CreateEditCardWindow.fxml"));
        //Doing some stuff with controller....

        Stage stage = new Stage();
        try {
            BorderPane bp = fxmlLoader.load();
            CreateEditCardWindowController controller = fxmlLoader.getController();
            controller.setData(card,book);
            Scene scene = new Scene(bp);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public HTMLEditor getHtmlEditor() {
        return htmlEditor;
    }
}
