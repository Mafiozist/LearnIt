package com.learnit.datasets;

import com.learnit.MyUtils;
import com.learnit.database.data.tables.Book;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

//it's use for containing and changing appearance of books
//parameters for appearance loads from disk directly and users can change them
public class Library {
    private static Library library;
    private ObservableList<Book> books;
    private String getBooksQuery = String.format(Locale.ROOT,"SELECT * FROM %s;", "books");
    private String addBooksQuery;
    private final String[] booksTables = new String[]{"book-card","book-tag","books"}; //the order is do matter
    private SimpleIntegerProperty booksSize;

    private Library() throws SQLException {
        ArrayList<Book> bookArrayList = new ArrayList<>();
        books = FXCollections.observableArrayList(new ArrayList<>());
        ResultSet resultSet = MyUtils.executeQueryWithResult(getBooksQuery);

        while (resultSet != null && resultSet.next()){
            Book book = new Book();
            book.setId(resultSet.getInt(1));
            book.setName(resultSet.getString(2));
            book.setHtmlText(resultSet.getString(3));
            book.setTextSize(resultSet.getFloat(4));
            Blob blob = resultSet.getBlob(7);

            if(blob != null) book.setTitleImg(blob.getBytes(0, (int) blob.length()));

            //there is binding for tag and book
            book.setTags(TagHolder.getInstance().getBondedTags(book));

            bookArrayList.add(book);
        }

       books = FXCollections.observableArrayList(bookArrayList);

       booksSize = new SimpleIntegerProperty(books.size());
       //booksSize.addListener((observable, oldValue, newValue) -> System.out.println(String.format("BooksSize changed from: %d to %d",oldValue,newValue)));

       books.addListener( (ListChangeListener<? super Book>) change -> {
           while (change.next()){
               if(change.wasAdded()){

                   Book book = change.getAddedSubList().get(0);
                   Image image = book.getTitleImg();
                   String qry = String.format(Locale.ROOT,"INSERT INTO books(name,htmlText) VALUES('%s', '%s')", book.getName(), book.getHtmlText());
                   MyUtils.executeQuery(qry);

                   booksSize.set(books.size());
                } else if (change.wasRemoved()){

                   for (String table: booksTables) {
                       MyUtils.executeQuery(String.format(Locale.ROOT,"DELETE FROM `%s` WHERE bid = %d;",table, change.getRemoved().get(0).getId()));
                   }

                   booksSize.set(books.size());
               }
           }
       });


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
        "UPDATE books SET htmlText = '%s' WHERE bid = %d;", book.getHtmlText(), book.getId());
        MyUtils.executeQuery(updateQuery);
        return this;
  }

  public Library updateBookName(Book book){
        MyUtils.executeQuery(String.format(Locale.ROOT,"UPDATE books SET name ='%s' WHERE bid='%d';",book.getName(), book.getId()));
        return this;
  }

  public Library removeBook(Book book){
        books.remove(book);
        return this;
  }

  public ObservableList<Book> getBooks() {
        return books;
    }

  public int getLastBookId() {
        ResultSet rid = MyUtils.executeQueryWithResult("SELECT bid FROM books WHERE bid=(SELECT max(bid) FROM books);");
        int id = -1;
        try {
            rid.next();
            id = rid.getInt(1);
        }catch (SQLException exception){
            exception.printStackTrace();
        }

        return id;
  }

  public Book getBookById(int id){
      for (Book book: books) {
          if(book.getId() == id) return book;
      }
      return null;
  }

  public SimpleIntegerProperty getBooksSizeProperty() {
        return booksSize;
    }
}
