package com.learnit.controllers;

import com.learnit.MainWindow;
import com.learnit.textconverters.SupportedTextFormats;
import com.learnit.textconverters.TextConverter;
import com.learnit.textconverters.TextConverterFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.xml.transform.TransformerConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.learnit.textconverters.SupportedTextFormats.*;


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

    public void initialize()  {/*
        //FlexibleInformationContainer = new TilePane();
        ArrayList<Button> list = new ArrayList<>();

        button = new Button();
        button.setOnAction(event -> {
            onButtonClick();
        });

        button.setPrefSize(200,200);
        button.setStyle("-fx-padding: 10px; -fx-color: red;");
        FlexibleInformationContainer.getChildren().add(button);

        //Dynamically adding new extension to filer of SupportedTextFormats ENUM by using Stream
        fileChooser = new FileChooser();
        List<String> extensionFilter = Stream.of(SupportedTextFormats.values())
                                        .map(Enum::name)
                                        .map(extension -> "*."+ extension.toLowerCase())
                                        .collect(Collectors.toList());

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt, *.docx, *.doc, *.pdf)", extensionFilter);
        fileChooser.getExtensionFilters().add(extFilter);*/

        //Just trying to set up main window
        try{
            TilePane tilePane = lLoader.load();
            lTab.setContent(tilePane);

            tilePane = tLoader.load();
            tTab.setContent(tilePane);

            tilePane = cLoader.load();
            cTab.setContent(tilePane);

            //tilePane = rLoader.load();
            //rTab.setContent(tilePane);

        } catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
            e.printStackTrace();
        }

     }

    @FXML
    void selectSingleFile(){
       /* File our = fileChooser.showOpenDialog(null);
        if(our != null) path.setText(our.getAbsolutePath());*/
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
        /*try {
            TextConverterFactory textConverterFactory = TextConverterFactory.getInstance();
            TextConverter textConverter;
            String filePath = (path.getText().isBlank() || path.getText().isEmpty())? "C:\\Users\\dinar\\Desktop\\TextFilesExamples\\Вакансия.docx" : path.getText();

            textConverter= textConverterFactory.createTextConverter(filePath);

            textArea.setText(textConverter.convert());
            htmlEditor.setHtmlText( textArea.getText());

        } catch (IOException|NullPointerException ex){
            ex.printStackTrace();
        }*/
    }

    @FXML
    void onTabChanged(){

    }

}