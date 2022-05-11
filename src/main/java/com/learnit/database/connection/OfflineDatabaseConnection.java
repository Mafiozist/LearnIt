package com.learnit.database.connection;

import javafx.scene.control.Alert;

import java.sql.*;

//# Класс отвечающий за доступ к офлайн версии базы данных
//Возможно стоит включить и создание БД в случае ее отсутствия??? Либо вывести в отдельный класс для дальнейшей работы
//Класс Синглтон
public class OfflineDatabaseConnection {
    private static OfflineDatabaseConnection connection;
    private static Connection dbLink;

    private String tags =
            "CREATE TABLE tags (\n" +
            "  id INT UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
            "  name VARCHAR(100) NOT NULL DEFAULT '',\n" +
            "  img BLOB DEFAULT NULL,\n" +
            "  PRIMARY KEY (id)\n" +
            ")";
    private String books =
            "CREATE TABLE books (\n" +
            "  id INT UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
            "  name VARCHAR(100) NOT NULL,\n" +
            "  htmlText LONGTEXT NOT NULL,\n" +
            "  size FLOAT NOT NULL DEFAULT -1,\n" +
            "  createDate DATETIME DEFAULT CURRENT_TIMESTAMP,\n" +
            "  changeDate DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,\n" +
            "  titleImg BLOB DEFAULT NULL,\n" +
            "  PRIMARY KEY (id)\n" +
            ")";

    private OfflineDatabaseConnection(){
        String dbName="learning";
        String dbUser="root";
        String dbPass="root";
        //String url="jdbc:mysql://localhost/"+dbName;
        String url="jdbc:h2:~/"+dbName+";IFEXISTS=TRUE";

        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            Class.forName("org.h2.Driver");
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
