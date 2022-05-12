package com.learnit.controllers;

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
    public Tab lTab;// library tab
    @FXML
    public Tab cTab; //Cards tab
    @FXML
    public Tab rTab; //revisingTab
    @FXML
    public Tab tTab; //tags tab
    @FXML
    public TabPane tabPane;
    @FXML
    public TextField search;

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
            StackPane stackPane = lLoader.load();
            lTab.setContent(stackPane);
        } catch (IOException e){
            e.printStackTrace();
        }

        try {
            HBox borderPane = tLoader.load();
            tTab.setContent(borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BorderPane bp = rLoader.load();
            rTab.setContent(bp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            TilePane tilePane = cLoader.load();
            cTab.setContent(tilePane);
        } catch (IOException e) {
            e.printStackTrace();
        }

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