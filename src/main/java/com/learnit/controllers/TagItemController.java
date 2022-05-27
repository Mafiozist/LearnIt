package com.learnit.controllers;

import com.learnit.CssParser;
import com.learnit.MainWindow;
import com.learnit.database.data.tables.Tag;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class TagItemController implements Initializable {
    @FXML private HBox tHBox;
    @FXML private Label tLabel;
    @FXML private BorderPane borderLabelPane ,borderImgPane;
    @FXML private ImageView img;
    private Tag tag;

    private CssParser cssParser;

    //For tag change functionality
    private final String defaultCssContent =
            """
                    #tHBox.tagitem {
                        -fx-border-color: rgb(33, 125, 151);
                        -fx-border-insets: 1;
                        -fx-border-width: 10;
                        -fx-border-radius: 10;
                        -fx-background-color:transparent;
                        -fx-background-radius: 10;
                    }
                    #tLabel.tagitem {
                        -fx-text-fill: rgb(23, 125, 125);
                        -fx-text-align: right;
                        -fx-font-size: 15;
                        -fx-font-weight: bold;
                        -fx-font-style: normal;
                        -fx-font-family: 'Times New Roman', Times, serif;
                    }
                    """;

    public TagItemController(URL url,Tag tag){
        this.tag = tag;
        tLabel = new Label();

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

            //tLabel.textProperty().bind(tag.getNameProperty());
            tag.getNameProperty().addListener((observable, oldValue, newValue) -> {
                tLabel.setText(newValue);
            });


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