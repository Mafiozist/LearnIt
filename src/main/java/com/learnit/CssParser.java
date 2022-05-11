package com.learnit;

import javafx.css.Stylesheet;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
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

    public CssParser(String baseCssContent, URL newCssUrl, String newCssPath){
        this.baseCssContent = baseCssContent;
        this.newCssUrl = newCssUrl;
        this.newCssPath = newCssPath;

        if(newCssUrl != null) newCssContent = getCssData(newCssUrl.getPath());

        //exception.printStackTrace();
        System.out.println("URL data is empty");
        newCssPath = getNewCssFileByContent(baseCssContent, newCssPath).getPath();
    }


    public void parse(){
        cssMap = new HashMap<>();

        cssMap.put("1","2");
        cssMap.remove("1");
        try {
            StringBuilder sb = new StringBuilder(newCssContent);
            int start, end = 0;
            for (int i = 0; i < newCssContent.length(); i++) {
                start = sb.indexOf("-", i);
                end = sb.indexOf(";", i) + 1;

                if (start == -1 || end == -1) return;

                char[] arr = new char[end - start];
                String parsedCssAtribute = new String("");
                sb.getChars(start, end, arr, 0);
                parsedCssAtribute = String.valueOf(arr);

                String attribute = parsedCssAtribute.split(":")[0];
                String parameter = parsedCssAtribute.split(":")[1];

                try {
                    cssMap.put(attribute + ":", parameter);
                } catch (UnsupportedOperationException exception) {
                    exception.printStackTrace();
                }

                i= end;
            }

        }catch(NullPointerException  exception){
            exception.printStackTrace(); // TODO: 03.05.2022 alert new css content is null
        }
    }

    public boolean updateCssFile(int tagId){
        File[] fileArr = new File[]{ // TODO: 03.05.2022 i have no idea which of these files changes and uses the CSSFX
                new File((String.format("D:/JavaProjects/LearnIt/src/main/resources/com/learnit/css/tags/%s", tagId+".css"))),
                new File((String.format("D:/JavaProjects/LearnIt/target/classes/com/learnit/css/tags/%s",tagId+".css")))
        };

       return updateFiles(fileArr);
    }

    private boolean updateCssFile(String name){
        File[] fileArr = new File[]{ // TODO: 03.05.2022 i have no idea which of these files changes and uses the CSSFX
                new File((String.format("D:/JavaProjects/LearnIt/src/main/resources/com/learnit/css/tags/%s", name+".css"))),
                new File((String.format("D:/JavaProjects/LearnIt/target/classes/com/learnit/css/tags/%s",name+".css")))
        };
        return updateFiles(fileArr);
    }

    public boolean updateCssFile(String content, int id){
        newCssContent = content;
        return updateCssFile(id);
    }

    private boolean updateCssFile(String content, String name){
        newCssContent = content;
        return updateCssFile(name);
    }
    private boolean updateFiles(File[] arr){
        boolean isUpdated = false;
        try {
            for (File file: arr) {
                if(file.canWrite()){
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(newCssContent);
                    fileWriter.close();
                    isUpdated = true;
                }
            }
        } catch (IOException ex){
            ex.printStackTrace(); // TODO: 01.05.2022 alert
        }
        return isUpdated;
    }



    public String getCssData(String path) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

    public String getBaseCssContent() {
        return baseCssContent;
    }

    public String getNewCssPath() {
        return newCssPath;
    }

    public void setNewCssPath(String newCssPath) {
        this.newCssPath = newCssPath;
    }

    public boolean removeCssFile(){
        boolean isFileRemoved = false;
        try {
            Files.delete(Path.of(newCssPath));
            isFileRemoved = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isFileRemoved;
    }

    public URL getNewCssUrl() {
        File file = new File(newCssPath);
        if (file.exists() && !newCssPath.isEmpty()){

            try {
                newCssUrl = file.toURI().toURL();
            } catch (MalformedURLException e) {
                System.out.println("getNewCssUrl exception: smth went wrjong with url. Again.");
                e.printStackTrace();
            }

        }
        return newCssUrl;
    }

    public void setNewCssUrl(URL newCssUrl) {
        this.newCssUrl = newCssUrl;
    }

    public String getNewCssContent() {
        if(newCssContent == null) return baseCssContent;
        return newCssContent;
    }

    public void setNewCssContent(String newCssContent) {
        this.newCssContent = newCssContent;
    }
}
