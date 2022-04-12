package com.learnit.database.data.structures;

//Базоый класс, хранящий книги, статьи и прочий материал для работы
import com.learnit.datasets.Tag;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//A class represents base structure of text files in app
//The setters of the class returns it to easy-use of methods
public class TextDoc{
    private int docId;
    private int dbId;

    //The name of document
    private String docName;

    //The text data which contains html code to further represent it in HtmlEditor
    private String docText;

    //Main information about data changing
    private LocalDateTime docCreationDateTime;
    private LocalDateTime docLastEditDateTime;

    //Array of tags to further easy way of sorting data
    private ArrayList<Tag> tags;

    //base metrics to know, that file has been changed
    private double baseFileSize; //Size is taken from db or disk space
    private double currentFileSize;
    private int baseLetterCount;
    private int currentLetterCount;

    //
    public TextDoc(){
        docCreationDateTime = docLastEditDateTime = LocalDateTime.now();
        String[] str = docCreationDateTime.toString().split("T");
        docId = -1;
        dbId = -1;
        docName = "-1";
        docText = "-1";
        tags = new ArrayList<>();
        currentFileSize = 0.0d;
        baseLetterCount = 0;
    }

    public int getDocId() {
        return docId;
    }
    public TextDoc setDocId(int docId) {
        this.docId = docId;
        return this;
    }

    public int getDbId() {
        return dbId;
    }
    public TextDoc setDbId(int dbId) {
        this.dbId = dbId;
        return this;
    }

    public String getDocName() {
        return docName;
    }
    public TextDoc setDocName(String docName) {
        this.docName = docName;
        return this;
    }

    public String getDocText() {
        return docText;
    }
    public TextDoc setDocText(String docText) {
        this.docText = docText;
        return this;
    }
    public LocalDateTime getDocCreationDateTime() {
        return docCreationDateTime;
    }

    public TextDoc setDocCreationDateTime(LocalDateTime docCreationDateTime) {
        this.docCreationDateTime = docCreationDateTime;
        return this;
    }
    public LocalDateTime getCreationDateTime() {
        return  docCreationDateTime;
    }

    public TextDoc setDocLastEditDateTime(LocalDateTime docLastEditDateTime) {
        this.docLastEditDateTime = docLastEditDateTime;
        return this;
    }
    public LocalDateTime getDocLastEditDateTime() {
        return docLastEditDateTime;
    }

    public TextDoc setTags(Tag ... tag){
        tags.addAll(List.of(tag));
        return this;
    }

    public double getCurrentFileSize() {
        return currentFileSize;
    }
    public TextDoc setCurrentFileSize(double currentFileSize) {
        this.currentFileSize = currentFileSize;
        return this;
    }
    public int getBaseLetterCount() {
        return baseLetterCount;
    }
    public TextDoc setCurrentLetterCount(int letterCount) {
        this.currentLetterCount = letterCount;
        return this;
    }

}
