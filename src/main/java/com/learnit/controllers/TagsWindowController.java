package com.learnit.controllers;

import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.learnit.MainWindow;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TagsWindowController implements Initializable {
    @FXML
    JFXListView<HBox> jfxListView;

    public TagsWindowController(){


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jfxListView.centerShapeProperty().setValue(true);

        /*jfxListView.setOnZoom((zoomEvent) -> {
            System.out.println("Zoom! Zoom!");
            if(zoomEvent.isControlDown()){
                System.out.println("CtrlPressed!");
                for (int i = 1; i < jfxListView.getItems().size(); i++) {
                    jfxListView.getItems().get(i).setPrefWidth(jfxListView.getItems().get(i).getWidth()*zoomEvent.getTotalZoomFactor());
                    jfxListView.getItems().get(i).setPrefHeight(jfxListView.getItems().get(i).getHeight()*zoomEvent.getTotalZoomFactor());
                }
            }
        });*/ //for feature zoom functionality ... maybe

        HBox hBoxCounter = new HBox();
        Label counter = new Label(String.format("The count of tags are: %d",100));
        hBoxCounter.getChildren().add(counter);

        for (int i = 0; i < 100; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainWindow.class.getResource("TagItem.fxml"));
            HBox hBox = null;
            try {
                hBox = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Label label = new Label(String.valueOf(i+1));
            label.setAlignment(Pos.CENTER);

            if (hBox != null) {
                hBox.getChildren().add(0,label);
                hBox.setSpacing(15d);

                hBox.setOnMousePressed(event -> {
                    System.out.println(event.getTarget());
                    if(event.isSecondaryButtonDown()){
                        JFXPopup jfxPopup = new JFXPopup();
                        VBox vBox = new VBox();

                        for (int j = 0; j < 3; j++) {
                            JFXButton button1 = new JFXButton("Task");
                            button1.setPadding(new Insets(10,0,10,0));//todo button with hover effect
                            vBox.getChildren().add(button1);
                        }

                        jfxPopup.setPopupContent(vBox);
                        jfxPopup.show(jfxListView, JFXPopup.PopupVPosition.TOP,JFXPopup.PopupHPosition.LEFT, event.getX() + 10, event.getY() + 40 );
                    }});
            }

            //hBox.getChildren().add(0, new JFX)
            if(i ==0) jfxListView.getItems().add(hBoxCounter);
            jfxListView.getItems().add(hBox);



            //jfxListView.setExpanded(true);
        }

        jfxListView.setOnMousePressed(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                if(event.isPrimaryButtonDown()){
                    System.out.println(event.getTarget());
                }
            }
        });

    }
}
