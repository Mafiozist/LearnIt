package com.learnit.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.learnit.MainWindow;
import com.learnit.datasets.TagHolder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateEditBookWindowController implements Initializable {
    @FXML
    public HTMLEditor htmlEditor;
    @FXML
    public BorderPane borderPane; //for simple information alignment
    @FXML
    public StackPane stackPane; //for Jfx dialogs
    ScrollPane scrollPane;
    int[] tagsIds;

    Node node;
    ToolBar bar;
    Button tagButton;

    public CreateEditBookWindowController(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tagButton = new Button();
        tagButton.setOnMouseClicked(event -> {
            StackPane layout = null;
            try {

                //Нужно что-то сделать с повторяющимся кодом - это плохо
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(MainWindow.class.getResource("TagChangeDialog.fxml"));
                layout = fxmlLoader.load();
                TagChangeWindowController controller = fxmlLoader.getController();


                //Костыль
                JFXButton button = new JFXButton("Save");
                VBox vBox = (VBox) layout.lookup("#vBox");
                vBox.getChildren().add(button);
                /////////

                JFXDialog jfxDialog  = new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.TOP);
                jfxDialog.show();

                button.setOnMouseClicked(closeEvent->{

                    try {
                        tagsIds = TagHolder.getInstance().getTagsIdsByNames(controller.getChangedTagsNames());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    System.out.println(tagsIds.length);
                    jfxDialog.close();
                });

            } catch (NullPointerException | IOException ex){
                ex.printStackTrace();
            }

        });

        //Adding tag button to html editor for adding tags to current book
        bar = null;
        node = htmlEditor.lookup(".top-toolbar");

        if (node instanceof ToolBar) {
            bar = (ToolBar) node;
            System.out.println( "Size before layout pass: "+bar.getItems().size());
        }

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
