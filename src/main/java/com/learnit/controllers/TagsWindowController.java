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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;

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
        addTagsToUi(url);

        //ColorPicker dynamically change css test
        borderColorPicker.setTooltip(new Tooltip("Изменить цвет границы"));
        fontColorPicker.setTooltip(new Tooltip("Изменить цвет шрифта"));


        jfxListView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.isPrimaryButtonDown()){
                    currentController = (TagItemController) jfxListView.getSelectionModel().getSelectedItem().getUserData();
                    cssTextArea.setText(currentController.getNewCssContent());

                    setColorParameter(borderColorPicker,"tHBox.tagitem","-fx-border-color");
                    setColorParameter(fontColorPicker, "tLabel.tagitem","-fx-text-fill");

                    cssTextArea.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observableValue, String old, String current) {
                            currentController.setNewCssContent(current);
                            currentController.getCssParser().updateCssFile(current,currentController.getTag().getId());
                            currentController.updateCss();
                        }
                    });

                    borderColorPicker.valueProperty().addListener(new ChangeListener<Color>() {
                        @Override
                        public void changed(ObservableValue<? extends Color> observableValue, Color color, Color t1) {
                            // TODO: 02.05.2022 there is need to get rgb and put it back into css area
                            changeColorParameter(
                                    "tHBox.tagitem",
                                    "-fx-border-color",
                                    (int) (t1.getRed() * 255),
                                    (int) (t1.getGreen() * 255),
                                    (int) (t1.getBlue() * 255));
                        }
                    });

                    fontColorPicker.valueProperty().addListener(new ChangeListener<Color>() {
                        @Override
                        public void changed(ObservableValue<? extends Color> observableValue, Color color, Color t1) {
                            // TODO: 02.05.2022 there is need to get rgb and put it back into css area
                            changeColorParameter(
                                    "tLabel.tagitem",
                                    "-fx-text-fill",
                                    (int) (t1.getRed() * 255),
                                    (int) (t1.getGreen() * 255),
                                    (int) (t1.getBlue() * 255));
                        }
                    });

                }
            }
        });

    }

    //They are here to take data from TextArea(cssTextArea) to change color values and give to user more comfortable control
    public void changeColorParameter(String startObjectName,String parameter, int r, int g, int  b) throws NullPointerException {
        StringBuilder sb = new StringBuilder(cssTextArea.getText());
        String newValue = String.format("rgb(%d,%d,%d)", r, g, b);

        int[] positions = findParameterValue(startObjectName,parameter);

        sb.replace(positions[0], positions[1], newValue);
        cssTextArea.setText(sb.toString());
    }

    public void setColorParameter(ColorPicker colorPicker, String cssObject, String cssParameter){
        StringBuilder sb = new StringBuilder(cssTextArea.getText());
        int[] pos = findParameterValue(cssObject,cssParameter);
        int[] rgb = new int[] {0,0,0};

        if (pos != null) {
            char[] value = new char[sb.substring(pos[0], pos[1]).length()];
            sb.getChars(pos[0], pos[1], value, 0);
            String str = String.valueOf(value).replace(",", " ");
            str = str.replace("rgb(", "");
            str = str.replace(")","");
            Scanner scanner = new Scanner(str);

            for (int i = 0; i < rgb.length; i++) {
                rgb[i] = scanner.nextInt();
                rgb[i] = (rgb[i] > 255)? 255 : Math.max(rgb[i], 0);
            }

            colorPicker.valueProperty().setValue(Color.rgb(rgb[0],rgb[1], rgb[2]));
        }
    }

    private int[] findParameterValue(String startObjectName,String parameter) {
        StringBuilder sb = new StringBuilder(cssTextArea.getText());
        int[] positions = new int[2]; //start and end of the value that was found
        //String newValue = String.format("rgb(%d,%d,%d)", r, g, b);

        final int startFrom = sb.indexOf(startObjectName);
        final int endTo = sb.indexOf("}", startFrom);

        if (startFrom == -1 || endTo==-1) return null; //If user has deleted css text from file

        int parStartPos = sb.indexOf(parameter, startFrom); // the start of necessary paremeter
        int valueEndPos = sb.indexOf(";", parStartPos);
        int valueStartPos = sb.indexOf(":", parStartPos);

        if (parStartPos == -1 || valueStartPos == -1 || valueEndPos == -1) return null;

        positions[0] = valueStartPos+1;
        positions[1] = valueEndPos;

        return positions;
    }


    public void addTagsToUi(URL url,Tag tag){
        FXMLLoader fxmlLoader = new FXMLLoader();
        TagItemController tagItemController = new TagItemController(url,tag);
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

    public void addTagsToUi(URL url){
        for (Tag tag: tags) {
            addTagsToUi(url,tag);
        }
    }

}
