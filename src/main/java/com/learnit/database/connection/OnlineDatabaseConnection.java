package com.learnit.database.connection;
import java.sql.Connection;

//Класс отвечающиай за доступ к онлайн версии базы данных
//Класс Синглтон
public class OnlineDatabaseConnection {
    private static OnlineDatabaseConnection db;

    private OnlineDatabaseConnection(){

    }

    public OnlineDatabaseConnection getConnection(){
        if(db == null)  db = new OnlineDatabaseConnection();
        return db;
    }
}
