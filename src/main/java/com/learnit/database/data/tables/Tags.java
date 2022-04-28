package com.learnit.database.data.tables;

import com.learnit.database.connection.OfflineDatabaseConnection;
import com.learnit.datasets.TagHolder;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Tags  {
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

   public Tags() {
      size++;
      appId = size;
      priority = 0;
      color = Color.AQUA;
   }

   public Tags setId(int id) {
      this.id = id;
      return this;
   }

   public int getAppId() {
      return appId;
   }

   public int getId(){
      return id;
   }

   public Tags setName(String name){
      this.name = name;
      return this;
   }
   public Tags setImg(Image img){
      /// TODO: 28.04.2022 converting img to blob and reverse
      this.img = img;
      return this;
   }

   public String getName(){
      return name;
   }

}
