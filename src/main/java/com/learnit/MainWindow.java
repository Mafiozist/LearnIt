package com.learnit;

import com.learnit.database.data.structures.Documentation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Locale;



public class MainWindow extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //Current windows locale
        Locale currentLocale = Locale.getDefault();

       /* Locale locale = new Locale("ru","RU");
        ResourceBundle bundle = ResourceBundle.getBundle("com.learnit.properties.TabName", locale);*/

        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("MainWindow.fxml") /*, bundle*/);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setWidth(1000);
        stage.setHeight(1000);
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}