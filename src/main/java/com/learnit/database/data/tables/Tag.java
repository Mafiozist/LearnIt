package com.learnit.database.data.tables;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;


public class Tag {
   private static int size;
   //private int appId;

   //DbData
   private int id;
   private SimpleStringProperty name;
   private Image img;


   public Tag() {
      size++;
      id = -1;
      //appId = size;
      name = new SimpleStringProperty("InitialName");
   }

   public Tag setId(int id) {
      this.id = id;
      return this;
   }

   //public int getAppId() {
   //   return appId;
   //}

   public int getId(){
      return id;
   }

   public Tag setName(String name){
      this.name.setValue(name);
      return this;
   }
   public Tag setImg(Image img){
      /// TODO: 28.04.2022 converting img to blob and reverse
      this.img = img;
      return this;
   }

   public String getName(){
      return name.getValue();
   }

   public SimpleStringProperty getNameProperty(){
      return name;
   }
}
