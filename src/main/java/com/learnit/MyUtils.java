package com.learnit;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.learnit.controllers.SelectDialogController;
import com.learnit.database.data.tables.Book;
import com.learnit.database.data.tables.Tag;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyUtils {


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
    public static void openTagSelectDialog(StackPane parent, ArrayList<Tag> tags){
        openSelectDialog(parent,null,tags);
    }

    public static void openBookSelectDialog(StackPane parent, ArrayList<Book> books){
        openSelectDialog(parent,books,null);
    }

    private static void openSelectDialog(StackPane parent, ArrayList<Book> books, ArrayList<Tag> tags){
        JFXDialog singleJfxDialog = new JFXDialog();
        JFXButton save = new JFXButton();

        try {
            FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource("SelectDialog.fxml"));
            singleJfxDialog.setContent(loader.load());
            SelectDialogController controller = loader.getController();

            if(books!=null) controller.setBooks(books);
            if(tags!=null) controller.setTags(tags);

            singleJfxDialog.setDialogContainer(parent);
            singleJfxDialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
