package com.learnit;

import javafx.css.Stylesheet;

import java.io.*;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CssParser {
    private static CssParser cssParser;
    private Map<String,String> cssMap;

    //For tag change functionality
    private String baseCssContent;
    private String newCssPath;
    private URL newCssUrl;
    private String newCssContent;

    public CssParser(){
    }

    public void parse(){

    }

    public CssParser(String baseCssContent, URL newCssUrl, String newCssPath){
        this.baseCssContent = baseCssContent;
        this.newCssUrl = newCssUrl;
        this.newCssPath = newCssPath;
        initBaseCssFiles();
    }

    public String getCssData(String path) throws IOException {
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        Stream<String> lines = bufferedReader.lines();
        return setCssDataFormatting(lines.collect(Collectors.joining()));
    }
    
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

    //There is needs a check on created file
    public File getNewCssFileByContent(String cssContent, String path){
        File file = new File(path);
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

    //Test case (with formatting) (method does not be able to add another end of line symbols if there has one)
    /*setCssDataFormatting("#tHBox{\n-fx-border-color: rgb(9, 108, 121);\n -fx-border-insests: 5;\n -fx-border-width: 4;\n -fx-border-radius: 10.0;\n -fx-background-color: transparent;\n }\n"+
    "#tLabel{\n -fx-text-align: center;\n -fx-font-size: 10;\n -fx-font-weight: bold;\n -fx-font-style: normal;\n -fx-font-family: 'Times New Roman', Times, serif;\n fx-text-fill: rgb(40, 12, 92);\n }\n"); */
    private String setCssDataFormatting(String rawCssData){
        StringBuilder stringBuilder = new StringBuilder(rawCssData);
        // enter after specific character
        stringBuilder = setEndOfLineAfter(stringBuilder,rawCssData,"{");
        stringBuilder = setEndOfLineAfter(stringBuilder,rawCssData,"}");
        stringBuilder = setEndOfLineAfter(stringBuilder,rawCssData,";");
        return stringBuilder.toString();
    }

    void initBaseCssFiles(){
        try {
            newCssContent = getCssData(newCssUrl.getPath());
        } catch (IOException exception) {
            exception.printStackTrace();
            System.out.println("URL data is empty");
            newCssPath = getNewCssFileByContent(newCssContent, newCssPath).getPath();
        }
    }

    public String getBaseCssContent() {
        return baseCssContent;
    }

    public String getNewCssPath() {
        return newCssPath;
    }

    public void setNewCssPath(String newCssPath) {
        this.newCssPath = newCssPath;
    }

    public URL getNewCssUrl() {
        return newCssUrl;
    }

    public void setNewCssUrl(URL newCssUrl) {
        this.newCssUrl = newCssUrl;
    }

    public String getNewCssContent() {
        return newCssContent;
    }

    public void setNewCssContent(String newCssContent) {
        this.newCssContent = newCssContent;
    }
}
