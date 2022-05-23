package com.learnit.datasets;

import com.learnit.MyUtils;
import com.learnit.database.connection.OfflineDatabaseConnection;
import com.learnit.database.data.tables.Book;
import com.learnit.database.data.tables.Card;
import com.learnit.database.data.tables.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class TagHolder {
    private static TagHolder tagHolder;
    private ObservableList<Tag> tags;
    private final String getTagsQuery = "SELECT * FROM tags;";
    private final String[] tagsTables = new String[]{"book-tag","card-tag", "tags"};

    //Установка значения по-умолчанию
    private TagHolder()  {
        try {

            ArrayList<Tag> tagArrayList = new ArrayList<>();
            Connection connection = OfflineDatabaseConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getTagsQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Tag tag = new Tag();
                tag.setId(resultSet.getInt(1)).setName(resultSet.getString(2));
                tagArrayList.add(tag);

                // TODO: 28.04.2022 getting data from pc
            }

            tags = FXCollections.observableList(tagArrayList);
            resultSet.close();

            tags.addListener((ListChangeListener<Tag>) c -> {
                while (c.next()){
                    if(c.wasAdded()){
                        Tag added = c.getAddedSubList().get(0);
                        MyUtils.executeQuery(String.format("INSERT INTO %s(name,img) VALUES('%s', %s)","tags",c.getAddedSubList().get(0).getName(), "null"));
                        added.setId(TagHolder.getInstance().getTagIdByName(added.getName()));
                    }

                    if(c.wasRemoved()){
                        int[] isFullyDeleted = new int[tagsTables.length];
                        int i = 0;

                        for (String table: tagsTables) {
                            isFullyDeleted[i++] = (MyUtils.executeQuery(String.format("DELETE FROM `%s` WHERE tid = %d;",table, c.getRemoved().get(0).getId())))? 1:0;
                        }

                        i=0;
                        i += Arrays.stream(isFullyDeleted).sum(); // if all tables from tagsTables removes then the amount of counter == tagsTables.length
                    }
                }
            });

        } catch (NullPointerException|SQLException exception){
            exception.printStackTrace(); // TODO: 02.05.2022 alert
        }
    }

    public static TagHolder getInstance(){
        if (tagHolder == null) tagHolder = new TagHolder();
        return tagHolder;
    }

    public ObservableList<Tag> getTags(){
        return tags;
    }

    public int[] getTagsIdsByNames(String ... names){ // there is need to be changed on sql realization
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

    public int getTagIdByName(String name) {
        ResultSet res = MyUtils.executeQueryWithResult(String.format("SELECT tid FROM %s WHERE name='%s';", "tags", name));
        if(res !=null){
            try {
                if (res.next()){
                    return res.getInt(1);
                }

            } catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        return -1;
    }

    public boolean removeTag(Tag tag){ //true only if all the tables from tagsTables removes well
        System.out.println("Tag is deleted");
        tags.remove(tag);
        return !tags.contains(tag);
    }

    public boolean addTag(Tag tag){
        tags.add(tag);
        return tags.contains(tag);
    }

    public boolean updateTag(Tag tag){
        return MyUtils.executeQuery(String.format("UPDATE %s SET name = '%s' WHERE tid ='%s';", "tags",tag.getName(), tag.getId()));
    }
    public void selectTags(ArrayList<Tag> tags, Book book){
        System.out.println("Количество тэгов до:" +book.getTags().size());
        for (Tag tag: tags) {
            selectTag(tag,book);
        }
        System.out.println("Количество тэгов после:" +book.getTags().size());
    }
    public void selectTags(ArrayList<Tag> tags, Card card){
        for (Tag tag: tags) {
            selectTag(tag,card);
        }
    }
    public boolean selectTag(Tag tag, Book book){ //If tag was selected, but he has reference values it means reference will be deleted
        ResultSet res = MyUtils.executeQueryWithResult(String.format("SELECT bid FROM `%s` WHERE bid=%d and tid=%d;","book-tag",book.getId(),tag.getId()));
        try {
            res.next();
            //if bind exist
            if(res.getInt(1) != -1) { //if bind was created there is need to unbind it by deleting
               return MyUtils.executeQuery(String.format("DELETE FROM `%s` WHERE tid=%d and bid=%d;","book-tag", tag.getId(), book.getId()));
            }

        } catch (SQLException e) {
            //if bind doesn't exist
            return MyUtils.executeQuery(String.format("INSERT into `%s` VALUES(%d,%d)","book-tag",book.getId(),tag.getId()));
        }

        return false;
    }
    public boolean selectTag(Tag tag, Card card){
        ResultSet res = MyUtils.executeQueryWithResult(String.format("SELECT cid FROM `%s` WHERE cid=%d and tid=%d;","card-tag",card.getId(),tag.getId()));

        try {
            res.next();

            if(res.getInt(1) != -1) { //if bind was created there is need to unbind it by deleting
                return MyUtils.executeQuery(String.format("DELETE FROM `%s` WHERE tid=%d and cid=%d;","card-tag", tag.getId(),card.getId()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return MyUtils.executeQuery(String.format("INSERT into `%s` VALUES(%d,%d)","card-tag",tag.getId(),card.getId()));
    }

    public ArrayList<Tag> getBondedTags(Card card){
        ArrayList<Tag> tmp = new ArrayList<>();
        ResultSet res = MyUtils.executeQueryWithResult(String.format("SELECT tid FROM `card-tag` WHERE cid=%d;",card.getId()));

        try {
            while (res.next()){
                int id = res.getInt(1);
                tmp.add(getTagById(id));
            }
        }catch (SQLException ignored){}

        return tmp;
    }

    public ArrayList<Tag> getBondedTags(Book book){
        ArrayList<Tag> tmp = new ArrayList<>(); // there isnt need additional tags so there will be using tags from TagHolder.tags
        ResultSet res= MyUtils.executeQueryWithResult(String.format("SELECT tid FROM `book-tag` WHERE bid=%d;",book.getId()));

        try {
            while (res.next()){
                int id = res.getInt(1);
                tmp.add(getTagById(id));
            }
        }catch (SQLException ignored){}

        return tmp;
    }

    private Tag getTagById(int id){
        for (Tag tag: tags) {
            if(tag.getId() == id) return tag;
        }
        return null;
    }

    public void update(){
        tagHolder = new TagHolder();
    }
}
