package com.learnit.database.data.tables;

import com.learnit.MainWindow;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;

public class Book {
    private static int objectSize;
    private int id,appId;
    private ArrayList<Tag> tag;
    private String name, htmlText;
    private double textSize;
    private Date createDate, changeDate;
    private Image titleImg;

    public Book(){
        appId = objectSize++;
        name = "The new book name";
        id = -1;
        htmlText = "";
        textSize = 0d;
        createDate = new Date();
        changeDate = new Date();
    }

    public int getId() {
        return id;
    }
    public Book setId(int id){
        this.id = id;
        return this;
    }
    public int getAppId() {
        return appId;
    }

    //Tags
    public ArrayList<Tag> getTags() {
        return tag;
    }
    public Book setTags(ArrayList<Tag> tag) {
        this.tag = tag;
        return this;
    }

    //Header
    public String getName() {
        return name;
    }
    public Book setName(String name) {
        this.name = name;
        return this;
    }

    //Body
    public String getHtmlText() {
        return htmlText;
    }
    public Book setHtmlText(String htmlText) {
        this.htmlText = htmlText;
        return this;
    }


    public double getTextSize() {
        return textSize;
    }
    public Book setTextSize(double textSize) {
        this.textSize = textSize;
        return this;
    }

    //Date
    public Date getCreateDate() {
        return createDate;
    }
    public Date getChangeDate() {
        return changeDate;
    }

    public Image getTitleImg() throws NullPointerException  {
        if (titleImg == null){
            titleImg = new Image(MainWindow.class.getResource("images/defaultBookImage.png").toString(),true);
        }

        return titleImg;
    }
    public Book setTitleImg(byte[] titleImg) {
        Image image = new Image(new ByteArrayInputStream(titleImg));
        this.titleImg = image;
        return this;
    }

}
