package com.learnit.datasets;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Tag {
    private String name;
    private Image thumbnail;
    private Color color;
    private Boolean Highlighter;
    private Color HighlighterColor; //
    private int size;/// common thing for all the instances
    private int priority;

    //Установка значения по-умолчанию
    public Tag(){
        name = "DefaultTagName";
        //TagColor = "Black";
        //TagHighlighter = true;
        //TagHighlighterColor = "BLUE"
        //TagSize = 10px - Размер обводки вокруг текста (расстояние от текста до конца обводки)
    }

    public String getName() {
        return name;
    }
    public void setName(String tagName) {
        this.name = tagName;
    }
}
