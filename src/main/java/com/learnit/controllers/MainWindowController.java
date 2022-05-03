package com.learnit.controllers;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTabPane;
import com.learnit.MainWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
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
            TilePane tilePane = lLoader.load();
            lTab.setContent(tilePane);

            //Adding gap between each card
            tilePane.setPadding(new Insets(10,0,0,10));
            tilePane.hgapProperty().set(10);
            tilePane.vgapProperty().set(10);
            ///////////////////////////

            BorderPane borderPane =tLoader.load();
            tTab.setContent(borderPane);

            tilePane = cLoader.load();
            cTab.setContent(tilePane);



            //tilePane = rLoader.load();
            //rTab.setContent(tilePane);

        } catch (IOException e){
            e.printStackTrace(); // TODO: 03.05.2022 alert
        }

     }

    @FXML
    void selectSingleFile(){

    }

    void onButtonClick(){
        /*System.out.println("Button clicked!");
        Runnable task = () -> {
            try {
                Thread.sleep(150);

                Platform.runLater(()->{
                    label.setText("TEXT!");
                });

                Thread.sleep(300);

                Platform.runLater(()-> {
                    label.setStyle("-fx-text-fill:yellow;-fx-font-size: 32px");
                });

            } catch (InterruptedException ex) {
                //We don't need to do Anything
                ex.printStackTrace();
            }
        };

        new Thread(task).start();*/
    }

    @FXML
    void onTextChanged(){

    }

    @FXML
    void onTabChanged(){

    }

}