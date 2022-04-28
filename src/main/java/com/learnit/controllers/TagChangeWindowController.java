package com.learnit.controllers;

import com.jfoenix.controls.*;
import com.learnit.MainWindow;
import com.learnit.Settings;
import com.learnit.database.data.tables.Tags;
import com.learnit.datasets.TagHolder;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
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

    ObservableList<JFXCheckBox> observableList, changedObservableList;

    TagHolder tagHolder;

    public TagChangeWindowController(){
        observableList = FXCollections.observableList(new ArrayList<JFXCheckBox>());
        changedObservableList = FXCollections.observableList(new ArrayList<>());

        try {
            tagHolder = TagHolder.getInstance();
        } catch (SQLException ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (Tags tag: tagHolder.getTags()) {
            JFXCheckBox jfxCheckBox = new JFXCheckBox(tag.getName());


            jfxCheckBox.setOnMouseClicked(event -> {
                if(changedObservableList.contains(jfxCheckBox)) changedObservableList.remove(jfxCheckBox);
                else {changedObservableList.add(jfxCheckBox);}
            });

            jfxCheckBox.setPadding(new Insets(5));
            observableList.add(jfxCheckBox);
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

    public String[] getChangedTagsNames(){
        String[] changedTags = new String[changedObservableList.size()];
        int i = 0;
        for (JFXCheckBox c: changedObservableList) {
            changedTags[i++] = c.getText();
        }
        return changedTags;
    }
}
