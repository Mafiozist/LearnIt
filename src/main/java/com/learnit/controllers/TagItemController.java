package com.learnit.controllers;

import com.learnit.MainWindow;
import com.learnit.database.data.tables.Tag;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private String baseCssPath;
    private URL baseCssUrl;
    private String baseCssContent;
    private String newCssPath;
    private URL newCssUrl;
    private String newCssContent;


    public TagItemController(Tag tag){
        this.tag = tag;

        tHBox = new HBox();
        try {
            baseCssPath = MainWindow.class.getResource("css/tags/BaseTag.css").getPath();
            baseCssContent = setCssDataFormatting(getCssData(baseCssPath));
            baseCssUrl = MainWindow.class.getResource("css/tags/BaseTag.css");
            newCssPath = getNewCssFile(getCssData(baseCssPath)).getPath();
            newCssContent = setCssDataFormatting(getCssData(newCssPath));
            newCssUrl = MainWindow.class.getResource(String.format("css/tags/%d.css",tag.getId()));

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setData(tag);
        tHBox.getStylesheets().add(newCssUrl.toExternalForm());
    }

    //There is needs a check on created file
    private File getNewCssFile(String cssContent){
        File file = new File((String.format("D:/JavaProjects/LearnIt/src/main/resources/com/learnit/css/tags/%s",tag.getId()+".css")));
        try {
            if(file.createNewFile()){
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(cssContent);
                fileWriter.close();
            }
        } catch (IOException ex){
            ex.printStackTrace(); // TODO: 01.05.2022 alert
        }

        return file;
    }

    private String getCssData(String path) throws IOException {
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        Stream<String> lines = bufferedReader.lines();

        return lines.collect(Collectors.joining());
    }
    private String setCssDataFormatting(String rawCssData){
        StringBuilder stringBuilder = new StringBuilder(rawCssData);
        // enter after specific character
        stringBuilder = setEndOfLineAfter(stringBuilder,rawCssData,"{");
        stringBuilder = setEndOfLineAfter(stringBuilder,rawCssData,"}");
        stringBuilder = setEndOfLineAfter(stringBuilder,rawCssData,";");

        return stringBuilder.toString();
    }
    //Test case with formatting (method does not be able to add another end of line symbols if there has one)
    /*setCssDataFormatting("#tHBox{\n-fx-border-color: rgb(9, 108, 121);\n -fx-border-insests: 5;\n -fx-border-width: 4;\n -fx-border-radius: 10.0;\n -fx-background-color: transparent;\n }\n"+
    "#tLabel{\n -fx-text-align: center;\n -fx-font-size: 10;\n -fx-font-weight: bold;\n -fx-font-style: normal;\n -fx-font-family: 'Times New Roman', Times, serif;\n fx-text-fill: rgb(40, 12, 92);\n }\n"); */

    private StringBuilder setEndOfLineAfter(StringBuilder stringBuilder, String rawCssData,String chr){
        for (int i = 0; i < rawCssData.length(); i++) {

            int a = stringBuilder.indexOf(chr,i);
            //If there is nothing left then return value
            if(a == -1) return stringBuilder;

            int tmp = a + 1;
            if(tmp >= rawCssData.length()) { //for cases where the searching symbol at the end of string it's mean that we need to put end line
                stringBuilder.insert(tmp,"\n");
                return stringBuilder;
            }
            if(stringBuilder.charAt(tmp) != '\n') stringBuilder.insert(tmp,"\n");
            i=a;
        }
        return stringBuilder;
    }

    public String getNewCssPath() {
        return newCssPath;
    }
    public URL getNewCssUrl(){
        return newCssUrl;
    }

    public void setData(Tag tag){
        tLabel.setText(tag.getName());
        // TODO: 01.05.2022 set image
    }
    public Tag getTag(){
        return tag;
    }
    public String getNewCssContent() {
        return newCssContent;
    }
    public void setNewCssContent(String css){
        newCssContent = css;
    }
}
