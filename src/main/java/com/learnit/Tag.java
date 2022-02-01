package com.learnit;

public class Tag {
    String tagName;
    //TagColor////
    ///TagHighlighter////
    //TagHighlighterColor
    ///TagSize///

    //Установка значения по-умолчанию
    Tag(){
        tagName = "DefaultTagName";
        //TagColor = "Black";
        //TagHighlighter = true;
        //TagHighlighterColor = "BLUE"
        //TagSize = 10px - Размер обводки вокруг текста (расстояние от текста до конца обводки)
    }

    public String getTagName() {
        return tagName;
    }
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
