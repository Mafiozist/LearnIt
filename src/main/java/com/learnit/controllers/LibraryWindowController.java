package com.learnit.controllers;

import com.learnit.MainWindow;
import com.learnit.datasets.Library;
import com.learnit.textconverters.SupportedTextFormats;
import com.learnit.textconverters.TextConverter;
import com.learnit.textconverters.TextConverterFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LibraryWindowController implements Initializable {
    @FXML
    public TilePane tilePane;
    private ContextMenu contextMenu;

    public LibraryWindowController(){
        contextMenu = new ContextMenu();
        MenuItem addBook = new MenuItem("AddBook");
        MenuItem addNewBook = new MenuItem("AddNewBook");

        addBook.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose file to work with");

            List<String> extensionFilter = Stream.of(SupportedTextFormats.values()).map(Enum::name)
                    .map(extension -> "*."+ extension.toLowerCase())
                    .collect(Collectors.toList());

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text formats ", extensionFilter);
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(tilePane.getScene().getWindow());

            //Converting data from files and parsing them to ... db
            try {
                TextConverterFactory textConverterFactory = TextConverterFactory.getInstance();
                TextConverter textConverter = textConverterFactory.createTextConverter(file.getPath());
                Library library = new Library();

                //Need something to do with data
                //create UI card of info and visualize it
                FXMLLoader bookloader = new FXMLLoader();
                bookloader.setLocation(MainWindow.class.getResource("LibraryItem.fxml"));
                VBox vBox = bookloader.load();
                LibraryItemController controller = bookloader.getController();

                //There is opened window where user can change main information
                //controller.setName(library.getName());
                //controller.setImg(library.getImg());

                tilePane.getChildren().add(vBox);


            }catch (IOException |  NullPointerException ex) {
                /*Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setContentText(ex.getLocalizedMessage());
                alert.setTitle("Ошибка, возможно файл пустой!");
                alert.setOnCloseRequest(dialogEvent -> {alert.close(); dialogEvent.consume();});
                alert.initModality(Modality.WINDOW_MODAL);
                alert.show();*/
                ex.printStackTrace();
            }
        });

        addNewBook.setOnAction(actionEvent -> {
            FXMLLoader wloader = new FXMLLoader();
            wloader.setLocation(MainWindow.class.getResource("CreateEditBookWindow.fxml"));
            CreateEditBookWindowController controller = wloader.getController();

            try {
                Stage bWindow = new Stage();
                bWindow.setTitle("The name of the book");
                Scene scene = new Scene(wloader.load(),800, 600);
                System.out.println(scene);

                bWindow.setResizable(true);
                bWindow.setScene(scene);
                bWindow.show();

                System.out.println(bWindow);
            } catch (IOException ex){
                ex.printStackTrace();
            }


        });

        contextMenu.getItems().addAll(addBook,addNewBook);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Setting up a context menu for tile pane
        tilePane.setOnContextMenuRequested(contextMenuEvent -> {
            contextMenu.show(tilePane, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
        } );

        //it`s needs to close context menu because of standard auto hide doesn't work as I need
        tilePane.setOnMouseClicked(event -> {
            if(event.isDragDetect()){
                contextMenu.hide();
            }

            //System.out.println(event.getTarget());
        });

    }

}
