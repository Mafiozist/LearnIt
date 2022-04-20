package com.learnit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Settings {
    private static Settings settings;

    //////////////////////// Settings in revising window main information appearance when you are revising info
    private static Font rFont;
    private static ObservableList<String> rFontNames;
    private static ComboBox<String> rFontsChooser;


    ////////////////////////
    //Settings in tags window
    //Fonts and size of text the same for all the tags but colour and image are unique


    ////////////////////////Settings in card window
    private double cwCardWidth;
    private double cwWindowCardHeight;


    ////////////////////////Settings in cards appearance
    private static int caTagQuantity;// between 0 and 9 because


    //Taking data from pc...
    private Settings(){
        rFontNames = FXCollections.observableList(Font.getFontNames());
        rFontsChooser = new ComboBox<String>();
        caTagQuantity = 9;

        //revisingFontsChooser.setPlaceholder(new Label("Something went wrong"));
        rFontsChooser.setItems(rFontNames);
        rFont = Font.font(rFontsChooser.getValue(), 25d);
        System.out.println();
    }

    public static int getCaTagQuantity() {
        return caTagQuantity;
    }
    public double getCwCardWidth() {
        return cwCardWidth;
    }
    public double getCwWindowCardHeight() {
        return cwWindowCardHeight;
    }
    public static Font getRFont() {
        return rFont;
    }

    public static void saveSettings(){

    }

    public static Settings getSettingsInstance() throws NullPointerException {
        if(settings == null) settings = new Settings();
        return settings;
    }
}
