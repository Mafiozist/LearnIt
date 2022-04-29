package com.learnit.controllers;

import com.jfoenix.controls.*;
import com.learnit.database.data.tables.Tag;
import com.learnit.datasets.TagHolder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TagChangeWindowController implements Initializable {
    @FXML
    VBox vBox, innerVBox;
    @FXML
    TextField textField;
    @FXML
    ScrollPane scrollPane;
    @FXML
    StackPane stackPane;

    ArrayList<Tag> tags;
    ObservableList<JFXCheckBox> observableList, changedObservableList;

    public TagChangeWindowController(){
        observableList = FXCollections.observableList(new ArrayList<JFXCheckBox>());
        changedObservableList = FXCollections.observableList(new ArrayList<>());
    }
    public TagChangeWindowController(ArrayList<Tag> tags){
        super();
        this.tags = tags;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            for (Tag tag: tags) {
                JFXCheckBox jfxCheckBox = new JFXCheckBox(tag.getName());

                jfxCheckBox.setOnMouseClicked(event -> {
                    if(changedObservableList.contains(jfxCheckBox)) changedObservableList.remove(jfxCheckBox);
                    else {changedObservableList.add(jfxCheckBox);}
                });

                jfxCheckBox.setPadding(new Insets(5));
                observableList.add(jfxCheckBox);
            }
        } catch (NullPointerException exception){
            exception.printStackTrace();
        }


        innerVBox.getChildren().addAll(observableList);

        //If there is nothing but zero tags
        if(innerVBox.getChildren().size() == 0) {
                JFXDialogLayout layout = new JFXDialogLayout();
                layout.setHeading(new Text("Attention!"));
                layout.setBody(new Text("You didn't create any tag yet. Would you like to create one? \n" +
                                            "If you close this window you can create it any time from tag tab."));

                JFXButton yes = new JFXButton("Yes");
                JFXButton cancel = new JFXButton("Cancel");

                JFXDialog jfxDialog = new JFXDialog(stackPane, innerVBox, JFXDialog.DialogTransition.CENTER);
                jfxDialog.setContent(layout);
                jfxDialog.show();

                cancel.setOnMouseClicked(event -> {
                    jfxDialog.close();
                });

                yes.setOnMouseClicked(event -> {
                    jfxDialog.close();
                    stackPane.getScene().getWindow().hide();

                });
                layout.setActions(yes,cancel);
        }

        textField.textProperty().addListener((observableValue, old, current) -> {

        });

    }

    public String[] getNamesChangedTags(){
        String[] changedTags = new String[changedObservableList.size()];
        int i = 0;
        for (JFXCheckBox c: changedObservableList) {
            changedTags[i++] = c.getText();
        }
        return changedTags;
    }
}
