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
    private static double tagChangeWindowWidth, tagChangeWindowHeight;

    ////////////////////////Settings in card window
    private static double cwCardWidth;
    private static double cwWindowCardHeight;


    ////////////////////////Settings in cardHolders appearance
    private static int caTagQuantity = 3;// between 0 and 9 because

    ///////////////////////RevisingSettings/////////////////////
    private static int minutesToAgain = 5;
    private static int goodInterval = 2;
    public static  int easyInterval = 3;
    public static  int hardInterval = 1;
    public static int maxIntervalToRevise;

    private static double goodIntervalMultiplier = 1.3;
    public static  double easyIntervalMultiplier = 1.5;
    public static  double hardIntervalMultiplier = 0.5d;

    private static int maxCardsPerDay = 75;
    private static int maxDayInterval = 100;
    private static int minDayInterval = 1;
    private static int repeatInMinutes = 10;

    private static int[] learningPhaseMinutes = new int[]{1, 10, 25}; //if all the walls passed by 1 day with good result or 1 of them with easy result which

    //Taking data from pc...
    private Settings(){
        rFontNames = FXCollections.observableList(Font.getFontNames());
        rFontsChooser = new ComboBox<>();
        caTagQuantity = 9;

        //revisingFontsChooser.setPlaceholder(new Label("Something went wrong"));
        rFontsChooser.setItems(rFontNames);
        rFont = Font.font(rFontsChooser.getValue(), 25d);

        //tagChangeWindowWidth = 170d;
        tagChangeWindowHeight = 600d;

        System.out.println();
    }

    //Tags
    public static int getCaTagQuantity() {
        return caTagQuantity;
    }

    public static double getTagChangeWindowWidth() {
        return tagChangeWindowWidth;
    }
    public static double getTagChangeWindowHeight(){
        return tagChangeWindowHeight;
    }

    //Cards
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

    public static Settings getSettings(){
        if(settings == null) settings = new Settings();
        return settings;
    }
}
