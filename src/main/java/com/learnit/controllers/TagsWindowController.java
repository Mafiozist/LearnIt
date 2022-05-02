package com.learnit.controllers;


import com.jfoenix.controls.JFXListView;
import com.learnit.MainWindow;
import com.learnit.database.data.tables.Tag;
import com.learnit.datasets.TagHolder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssParser;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

// TODO: 01.05.2022 create tag
// TODO: 01.05.2022 edit tag 
// TODO: 01.05.2022 update tag

//Master-detail window
public class TagsWindowController implements Initializable {
    @FXML
    TextArea cssTextArea;
    @FXML
    ColorPicker fontColorPicker,borderColorPicker;
    @FXML
    BorderPane borderPane;
    @FXML
    JFXListView<HBox> jfxListView;

    ObservableList<Tag> tags;
    HBox hBox;
    String currentCssText;
    TagItemController currentController;

    public TagsWindowController() {
        jfxListView = new JFXListView<>();
        tags= FXCollections.observableList(TagHolder.getInstance().getTags());
        tags.addListener(new ListChangeListener<Tag>() {
            @Override
            public void onChanged(Change<? extends Tag> change) {
                while (change.next()){
                    if(change.wasAdded()){
                        // TODO: 02.05.2022 add tag to ui
                    }
                    else if(change.wasRemoved()){
                        // TODO: 02.05.2022 delete tag from ui
                    }
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addTagsToUi();

        /*HBox hBoxCounter = new HBox();
        Label counter = new Label(String.format("The count of tags are: %d", jfxListView.getItems().size()));
        hBoxCounter.getChildren().add(counter);
        jfxListView.getItems().add(0,hBoxCounter);*/

        //ColorPicker dynamically change css test
        borderColorPicker.setTooltip(new Tooltip("Изменить цвет границы"));
        fontColorPicker.setTooltip(new Tooltip("Изменить цвет шрифта"));




        jfxListView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.isPrimaryButtonDown()){
                    currentController = (TagItemController) jfxListView.getSelectionModel().getSelectedItem().getUserData();
                    cssTextArea.setText(currentController.getNewCssContent());
                    CssParser cssParser = new CssParser();

                    try {
                        cssParser.parse(currentController.getNewCssUrl());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    cssTextArea.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observableValue, String old, String current) {
                            currentController.setNewCssContent(current);
                            File file = new File((String.format("D:/JavaProjects/LearnIt/src/main/resources/com/learnit/css/tags/%s",currentController.getTag().getId()+".css")));
                            try {
                                if(file.canWrite()){
                                    FileWriter fileWriter = new FileWriter(file);
                                    fileWriter.write(current);
                                    fileWriter.close();
                                }
                            } catch (IOException ex){
                                ex.printStackTrace(); // TODO: 01.05.2022 alert
                            }

                        }
                    });

                    borderColorPicker.setOnHidden(colorEvent -> {
                        Color color = borderColorPicker.getValue();
                        // TODO: 02.05.2022 there is need to get rgb and put it back into css area
                    });

                    fontColorPicker.setOnHidden(colorEvent -> {
                        Color color = fontColorPicker.getValue();
                        // TODO: 02.05.2022 there is need to get rgb and put it back into css area
                    });
                }
            }
        });



    }

    public void addTagsToUi(Tag tag){
        FXMLLoader fxmlLoader = new FXMLLoader();
        TagItemController tagItemController = new TagItemController(tag);
        fxmlLoader.setController(tagItemController);
        fxmlLoader.setLocation(MainWindow.class.getResource("TagItem.fxml"));

        try {
            hBox = fxmlLoader.load();
            hBox.setUserData(tagItemController);
            System.out.println(fxmlLoader.getRoot().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (hBox != null) {
            hBox.setSpacing(15d);
            hBox.setOnMousePressed(event -> {
                System.out.println(event.getTarget());
            });
        }

        jfxListView.getItems().add(hBox);
    }

    public void addTagsToUi(){
        for (Tag tag: tags) {
            addTagsToUi(tag);
        }
    }

}
