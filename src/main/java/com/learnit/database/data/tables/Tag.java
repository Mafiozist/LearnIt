package com.learnit.database.data.tables;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public class Tag {
   private static int size;
   private int appId;

   //DbData
   private int id;
   private String name;
   private Image img;

   //Wrapper data containing at pc
   private int priority;
   private Color color;
   private Boolean Highlighter;
   private Color HighlighterColor;

   public Tag() {
      size++;
      id = -1;
      appId = size;
      priority = 0;
      color = Color.AQUA;
      name = "InitialName";
   }

   public Tag setId(int id) {
      this.id = id;
      return this;
   }

   public int getAppId() {
      return appId;
   }

   public int getId(){
      return id;
   }

   public Tag setName(String name){
      this.name = name;
      return this;
   }
   public Tag setImg(Image img){
      /// TODO: 28.04.2022 converting img to blob and reverse
      this.img = img;
      return this;
   }

   public String getName(){
      return name;
   }

}
