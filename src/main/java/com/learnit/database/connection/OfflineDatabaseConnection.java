package com.learnit.database.connection;

//# Класс отвечающий за доступ к офлайн версии базы данных
//Возможно стоит включить и создание БД в случае ее отсутствия??? Либо вывести в отдельный класс для дальнейшей работы
//Класс Синглтон
public class OfflineDatabaseConnection {
    private static OfflineDatabaseConnection connection;

    private OfflineDatabaseConnection(){

    }

    public OfflineDatabaseConnection getIntstance(){
        if(connection == null) connection = new OfflineDatabaseConnection();
        return connection;
    }

}
