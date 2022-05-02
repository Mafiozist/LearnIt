package com.learnit.datasets;
import com.learnit.database.connection.OfflineDatabaseConnection;
import com.learnit.database.data.tables.Tag;
import javafx.beans.value.ObservableValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TagHolder {
    private static TagHolder tagHolder;
    private ArrayList<Tag> tags;
    private final String getTagsQuery = "SELECT * FROM tags;";

    //Установка значения по-умолчанию
    private TagHolder()  {
        try {
            tags = new ArrayList<>();
            Connection connection = OfflineDatabaseConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getTagsQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Tag tag = new Tag();
                tag.setId(resultSet.getInt(1)).setName(resultSet.getString(2));
                this.tags.add(tag);

                // TODO: 28.04.2022 getting data from pc
            }

            resultSet.close();
        } catch (NullPointerException|SQLException exception){
            exception.printStackTrace(); // TODO: 02.05.2022 alert
        }
    }

    public static TagHolder getInstance(){
        if (tagHolder == null) tagHolder = new TagHolder();
        return tagHolder;
    }

    public ArrayList<Tag> getTags(){
        return tags;
    }

    public int[] getTagsIdsByNames(String ... names){
        int i = 0;
        int[] tagsIds = new int[names.length];
        //В идеале бы оптимизацию подвести
        for (String name: names){
            for (Tag tag: tags) {
                if(tag.getName().equals(name)) tagsIds[i++] = tag.getId();
            }
        }
        return tagsIds;
    }

    public int getTagIdByName(String name){
        for (Tag tag : tags){
            if(tag.getName().equals(name)) return tag.getAppId();
        }
        return -1;
    }
}
