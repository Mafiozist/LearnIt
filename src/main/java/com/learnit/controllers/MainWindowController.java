package com.learnit.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXTabPane;
import com.learnit.MainWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;

//Constructor is worked before creating of xml file //This file for experements use until work it's done
//There is logic of main window
public class MainWindowController {
    @FXML
    private Tab lTab;// library tab
    @FXML
    private Tab cTab; //Cards tab
    @FXML
    private Tab rTab; //revisingTab
    @FXML
    private Tab tTab; //tags tab
    @FXML
    private JFXTabPane tabPane;
    @FXML
    private TextField search;
    @FXML
    StackPane stackPane;

    FXMLLoader lLoader, cLoader, rLoader, tLoader;

    public MainWindowController(){
        lTab = new Tab();
        rTab = new Tab();
        tTab = new Tab();
        cTab = new Tab();

        lLoader = new FXMLLoader();
        rLoader = new FXMLLoader();
        cLoader = new FXMLLoader();
        tLoader = new FXMLLoader();

        lLoader.setLocation(MainWindow.class.getResource("LibraryWindow.fxml"));
        rLoader.setLocation(MainWindow.class.getResource("RevisingWindow.fxml"));
        cLoader.setLocation(MainWindow.class.getResource("CardWindow.fxml"));
        tLoader.setLocation(MainWindow.class.getResource("TagsWindow.fxml"));

    }

    public void initialize()  {
        //Just trying to set up main window
        try{
            lTab.setContent( lLoader.load());
        } catch (IOException e){
            e.printStackTrace();
        }

        try {
            tTab.setContent(tLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            rTab.setContent(rLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            cTab.setContent(cLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        tabPane.tabMinWidthProperty().bind(stackPane.widthProperty().divide(tabPane.getTabs().size()));

     }

    @FXML
    void selectSingleFile(){

    }

    void onButtonClick(){

    }

    @FXML
    void onTextChanged(){

    }

    @FXML
    void onTabChanged(){

    }

}