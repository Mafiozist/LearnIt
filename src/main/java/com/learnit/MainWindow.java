package com.learnit;

import com.learnit.controllers.LibraryWindowController;
import com.learnit.database.connection.OfflineDatabaseConnection;
import com.learnit.database.data.tables.Book;
import com.learnit.datasets.Library;
import com.learnit.datasets.TagHolder;
import fr.brouillard.oss.cssfx.CSSFX;
import fr.brouillard.oss.cssfx.api.URIToPathConverter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Tag;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Properties;



public class MainWindow extends Application {
    @Override
    public void start(Stage stage)  {
        CSSFX.start();

        //GregorianCalendar gregorianCalendar = new GregorianCalendar();
        //gregorianCalendar.setTimeInMillis(2579262354193l);
        //Date date = gregorianCalendar.getTime();
        //System.out.println(date);

        //Current windows locale
        Locale currentLocale = Locale.getDefault();
        Settings settings = Settings.getSettings();

        Scene scene = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("MainWindow.fxml") /*, bundle*/);
            scene = new Scene(fxmlLoader.load(),800,600);
        } catch (IOException exception){
            exception.printStackTrace();
        }

        stage.setTitle("СХИПИ");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}