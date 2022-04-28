package com.learnit.database.connection;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;

//# Класс отвечающий за доступ к офлайн версии базы данных
//Возможно стоит включить и создание БД в случае ее отсутствия??? Либо вывести в отдельный класс для дальнейшей работы
//Класс Синглтон
public class OfflineDatabaseConnection {
    private static OfflineDatabaseConnection connection;
    private static Connection dbLink;

    private OfflineDatabaseConnection(){
        String dbName="learning";
        String dbUser="root";
        String dbPass="root";
        String url="jdbc:mysql://localhost/"+dbName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbLink = DriverManager.getConnection(url, dbUser, dbPass);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static OfflineDatabaseConnection getInstance(){
        if(connection == null) connection = new OfflineDatabaseConnection();
        return connection;
    }

    public Connection getConnection() {
        return dbLink;
    }
}
