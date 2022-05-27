package com.learnit;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.learnit.controllers.SelectDialogController;
import com.learnit.database.connection.OfflineDatabaseConnection;
import com.learnit.database.data.tables.Book;
import com.learnit.database.data.tables.Tag;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyUtils {


    //ExecuteQuery
    public static ResultSet executeQueryWithResult(String qry){
        ResultSet resultSet = null;
        try {
            Connection connection = OfflineDatabaseConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(qry);
            resultSet = preparedStatement.executeQuery();
        }catch (NullPointerException| SQLException exception){
            exception.printStackTrace();
            System.out.println("Cannot connect to db"); // TODO: 11.05.2022 db connection error
        }
        return resultSet;
    }

    public static boolean executeQuery(String query){
        boolean isExecuted = false;
        Connection connection = OfflineDatabaseConnection.getInstance().getConnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            isExecuted=true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isExecuted;
    }

    public static int getLastAddedId(String tablename, String idStr) {
        ResultSet rid = MyUtils.executeQueryWithResult(String.format("SELECT %s FROM `%s` WHERE %s=(SELECT max(%s) FROM `%s`);",idStr, tablename, idStr, idStr,tablename));
        int id = -1;
        try {
            rid.next();
            id = rid.getInt(1);
        }catch (SQLException exception){
            exception.printStackTrace();
        }
        return id;
    }

    //Message dialogs
    public static void openMessageDialog(StackPane parent, String header, String msg){
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Text(header));
        layout.setBody(new Text(msg));
        JFXButton ok = new JFXButton("Ok");
        JFXDialog jfxDialog = new JFXDialog(parent, layout, JFXDialog.DialogTransition.CENTER);
        jfxDialog.show();

        ok.setOnMousePressed(event -> {
            if(event.isPrimaryButtonDown()) {
                jfxDialog.close();
            }
        });
        layout.setActions(ok);
    }


    //there maybe is need a commander pattern or a constructor
    //Dialogs which outputs the selected items ids
    //returns ArrayList of tags or books
    public static JFXDialog openTagSelectDialog(StackPane parent, ObservableList<Tag> tags){
        return openSelectDialog(parent,null,tags);
    }

    public static JFXDialog openBookSelectDialog(StackPane parent, ObservableList<Book> books){
        return openSelectDialog(parent,books,null);
    }

    private static JFXDialog openSelectDialog(StackPane parent, ObservableList<Book> books, ObservableList<Tag> tags){
        JFXDialog singleJfxDialog = new JFXDialog();

        try {
            FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource("dialogsView/SelectDialog.fxml"));
            singleJfxDialog.setContent(loader.load());
            SelectDialogController controller = loader.getController();

            if(books!=null) {
                controller.setBooks(books);
            }
            if(tags!=null) {
                controller.setTags(tags);
            }

            singleJfxDialog.setUserData(controller);
            singleJfxDialog.setDialogContainer(parent);
            singleJfxDialog.show();

        } catch (IOException ignored) {}

        return singleJfxDialog;
    }


    //html
    public static String getCentredHtml(String html){
        Document document = Jsoup.parse(html);
        Element head = document.head();
        head.appendElement("style");
        head.select("style").
                append("""
                            html,body {
                            \theight: 100%;
                            \twidth: 100%;
                            }
                            """).
                append("""
                            .container {
                            \talign-items: center;
                            \tdisplay: flex;
                            \tjustify-content: center;
                            \theight: 100%;
                            \twidth: 100%;
                            }
                            """);

        Element body = document.body();
        //crating the new elements to center the info
        Element divWrapper = new Element("div");
        Element div = new Element("div");

        divWrapper.addClass("container");
        div.addClass("content");
        div.appendChild(body);
        divWrapper.appendChild(div);


        //System.out.println(divWrapper.outerHtml());
        body.html(divWrapper.outerHtml());
        document.body().html(body.html());

        //System.out.println(document.html());
        return document.html();
    }

}
