package com.learnit;

import com.learnit.database.data.structures.TextDoc;
import com.learnit.datasets.Tag;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;


public class MainWindow extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //Current windows locale
        Locale currentLocale = Locale.getDefault();

       /* Locale locale = new Locale("ru","RU");
        ResourceBundle bundle = ResourceBundle.getBundle("com.learnit.properties.TabName", locale);*/


        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("CardWindow.fxml") /*, bundle*/);
        Scene scene = new Scene(fxmlLoader.load());

        scene.getStylesheets().add(getClass().getResource("css/Card.css").toExternalForm());

        stage.setTitle("Hello!");
        stage.setWidth(500);
        stage.setHeight(500);
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}