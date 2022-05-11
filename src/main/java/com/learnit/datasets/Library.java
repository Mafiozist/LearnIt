package com.learnit.datasets;

import com.learnit.database.connection.OfflineDatabaseConnection;
import com.learnit.database.data.tables.Book;
import com.learnit.database.data.tables.Tag;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;

//it's use for containing and changing appearance of books
//parameters for appearance loads from disk directly and users can change them
public class Library {
    private static Library library;
    private ObservableList<Book> books;
    private String getBooksQuery = String.format("SELECT * FROM %s;", "books");
    private String addBooksQuery;
    Connection connection;

    private Library() throws SQLException {
        ArrayList<Book> bookArrayList = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            connection = OfflineDatabaseConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getBooksQuery);
            resultSet = preparedStatement.executeQuery();
        }catch (NullPointerException exception){
            exception.printStackTrace();
            System.out.println("Cannot connect to db"); // TODO: 11.05.2022 db connection error
        }


        while (resultSet != null && resultSet.next()){
            Book book = new Book();
            book.setId(resultSet.getInt(1));
            book.setName(resultSet.getString(2));
            book.setHtmlText(resultSet.getString(3));
            book.setTextSize(resultSet.getFloat(4));
            Blob blob = resultSet.getBlob(7);

            if(blob != null) book.setTitleImg(blob.getBytes(0, (int) blob.length()));

            bookArrayList.add(book);
        }

       books = FXCollections.observableList(bookArrayList);

       books.addListener( (ListChangeListener<? super Book>) change -> {
           while (change.next()){
               if(change.wasAdded()){
                   try {

                       Book book = change.getAddedSubList().get(0);
                       Image image = book.getTitleImg();

                       PreparedStatement addStatement = connection
                               .prepareStatement(String.format("INSERT INTO books(name,htmlText)" +
                               "VALUES(%s, %s)", "\'"+book.getName()+"\'", "\' "+book.getHtmlText()+" \'"));
                       addStatement.execute();

                   } catch (SQLException e) {
                       e.printStackTrace();
                   }
                   // TODO: 30.04.2022 if book was added there is need to add book to db either

                } else if (change.wasRemoved()){
                   // TODO: 30.04.2022  if book was removed there is need to remove from db either

               }
           }

       });



        /*Runnable checkObservableEventsTask = ()->{

            for (int i = 0; i < 9; i++) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                books.remove(books.size() - 1);
            }


            System.out.println("Starting to change book`s parameters");
            Platform.runLater(()->{
                books.add(new Book());
            });


        };
        new Thread(checkObservableEventsTask).start();*/
    }

    public static Library getInstance(){
        if(library == null) {
            try {
                library = new Library();
            } catch (SQLException e) {
                e.printStackTrace(); // TODO: 30.04.2022 alert
            }
        }
        return library;
    }

    public Library addBook(Book book){
        books.add(book);
        return this;
    }
    public Library updateBook(Book book){ // TODO: 30.04.2022  need to add image and tags
        System.out.println("Db. Book is updated");
        String updateQuery = String.format(
        "UPDATE books SET name = '%s', " +
                "htmlText = '%s' " +
                "WHERE id = %d;", book.getName(), book.getHtmlText(), book.getId());

        try {
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return this;
    }

    public Library removeBook(Book book){
        books.remove(book);
        return this;
    }

    public ObservableList<Book> getBooks() {
        return books;
    }
}
