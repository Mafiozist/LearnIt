package com.learnit.controllers;

import com.learnit.CssParser;
import com.learnit.MainWindow;
import com.learnit.database.data.tables.Tag;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import java.io.*;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;


public class TagItemController implements Initializable {
    @FXML
    public HBox tHBox;
    @FXML
    public Label tLabel;
    @FXML
    public BorderPane borderLabelPane ,borderImgPane;
    @FXML
    public ImageView img;
    private Tag tag;

    private CssParser cssParser;

    //For tag change functionality
    private final String defaultCssContent =
            "#tHBox.tagitem {\n" +
                    "    -fx-border-color: rgb(33, 125, 151);\n" +
                    "    -fx-border-insets: 1;\n" +
                    "    -fx-border-width: 10;\n" +
                    "    -fx-border-radius: 10;\n" +
                    "    -fx-background-color:transparent;\n" +
                    "    -fx-background-radius: 10;\n"+
                    "}\n" +
            "#tLabel.tagitem {\n" +
                    "    -fx-text-fill: rgb(23, 125, 125);\n" +
                    "    -fx-text-align: right;\n" +
                    "    -fx-font-size: 15;\n" +
                    "    -fx-font-weight: bold;\n" +
                    "    -fx-font-style: normal;\n" +
                    "    -fx-font-family: 'Times New Roman', Times, serif;\n" +
                    "}\n";

    public TagItemController(URL url,Tag tag){
        this.tag = tag;

        if(tag.getId()!=-1) {
            //Getting absolute path to the data
            File file = new File(url.getPath());
            StringBuilder sb = new StringBuilder(file.getAbsolutePath());
            sb.replace(sb.indexOf("target"),sb.length(),"src/main/resources/com/learnit/css/tags/");
            ////////////////////////////////////////////////////////////

            String formatedStr = sb.toString() + tag.getId() + ".css";
            //String formatedStr = sb.toString() + tag.getName() + ".css";
            formatedStr = formatedStr.replace("\\","/");

            cssParser = new CssParser(
                    defaultCssContent,
                    MainWindow.class.getResource(String.format("css/tags/%d.css", tag.getId())), formatedStr);

            tHBox = new HBox();
            //cssParser.parse();
        } else if(tag.getId() == -1){ //Object isn't contained at db
            this.tag = tag;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setData(tag);

        tHBox.getStylesheets().add(cssParser.getNewCssUrl().toExternalForm());
    }

    public void updateCssOnNode(){
       tHBox.getStylesheets().clear();
       tHBox.getStylesheets().add(cssParser.getNewCssUrl().toExternalForm());
    }

    public void updateCssFromFile(){
        tHBox.getStylesheets().clear();
        tHBox.getStylesheets().add("file:///"+cssParser.getNewCssPath().replace("\\","/"));
    }

    public String getNewCssPath() {
        return cssParser.getNewCssPath();
    }
    public URL getNewCssUrl(){
        return cssParser.getNewCssUrl();
    }

    public void setData(Tag tag){
        tLabel.setText(tag.getName());
        // TODO: 01.05.2022 set image
    }
    public Tag getTag(){
        return tag;
    }
    public String getNewCssContent() {
        return cssParser.getNewCssContent();
    }

    public CssParser getCssParser() {
        return cssParser;
    }

    public void setNewCssContent(String css){
        cssParser.setNewCssContent(css);
    }
}