package com.learnit.controllers;

import com.learnit.MainWindow;
import com.learnit.database.data.tables.Book;
import com.learnit.datasets.Library;
import com.learnit.textconverters.SupportedTextFormats;
import com.learnit.textconverters.TextConverter;
import com.learnit.textconverters.TextConverterFactory;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LibraryWindowController implements Initializable {
    @FXML
    public TilePane tilePane;
    private ContextMenu contextMenu;
    private ObservableList<Book> books;


    public LibraryWindowController(){

        books = FXCollections.observableList(Library.getInstance().getBooks());

        contextMenu = new ContextMenu();
        MenuItem addBook = new MenuItem("AddBook");
        MenuItem addNewBook = new MenuItem("AddNewBook");

        addNewBook.setOnAction(actionEvent -> {
            System.out.println(actionEvent.getSource());
            openEditDialog(new Book());
        });

        addBook.setOnAction(actionEvent -> {
            File file = getFileWithFilter();

            //Converting data from files and parsing them to ... db
            try {
                TextConverterFactory textConverterFactory = TextConverterFactory.getInstance();
                TextConverter textConverter = textConverterFactory.createTextConverter(file.getPath());

                Book book = new Book();
                book.setName(file.getName().split("\\.")[0]);
                book.setHtmlText(textConverter.convert());

                //Need something to do with data
                //create UI card of info and visualize it
                openEditDialog(book);
            }catch (IOException |  NullPointerException ex) {
                ex.printStackTrace(); //todo alert
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

        books.addListener((ListChangeListener<? super Book>) change ->{
            while (change.next()){
                if(change.wasAdded()){
                    updateUI(change.getList().get(0)); //Because at the same time, i am always add one book for now
                }
                if (change.wasRemoved()){
                    // TODO: 30.04.2022 delete from library UI and db
                }

            }
        });

        updateUI();
    }

    public void openEditDialog(Book book){
        FXMLLoader wloader = new FXMLLoader();
        wloader.setLocation(MainWindow.class.getResource("CreateEditBookWindow.fxml"));
        CreateEditBookWindowController controller = null;
        Stage bWindow = new Stage();
        bWindow.setTitle(book.getName());
        try {
            Scene scene = new Scene(wloader.load(),800, 600);
            controller = wloader.getController();
            controller.setBook(book);
            //System.out.println(scene);

            bWindow.setResizable(true);
            bWindow.setScene(scene);
            bWindow.show();
            //System.out.println(bWindow);
        } catch (IOException ex){
            ex.printStackTrace(); //todo alert
        }

        CreateEditBookWindowController finalController = controller;
        bWindow.setOnCloseRequest(windowEvent -> {
            if (finalController != null) {
                book.setHtmlText(finalController.htmlEditor.getHtmlText());
            }
            if (book.getId() != -1){
                Library.getInstance().updateBook(book);
            }
            else if (book.getId() == -1){ //If object aren't taken from db

                if(!Library.getInstance().getBooks().contains(book)){
                    updateUI(book);
                    Library.getInstance().addBook(book);
                }
            }

        });
    }

    public void updateUI()  {
        for (Book book: books )  {
            updateUI(book);
        }
    }

    public void updateUI(Book book) {
        FXMLLoader bookloader = new FXMLLoader();
        bookloader.setLocation(MainWindow.class.getResource("LibraryItem.fxml"));
        VBox vBox = null;

            try {
                vBox = bookloader.load();
                vBox.setOnMousePressed(event -> {
                    if(event.isPrimaryButtonDown()) openEditDialog(book);
                });
            } catch (IOException e) {
                e.printStackTrace(); // TODO: 30.04.2022 alert
            }

            LibraryItemController controller = bookloader.getController();
            controller.setBook(book);

            tilePane.getChildren().add(vBox);

    }

    public File getFileWithFilter(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file to work with");

        List<String> extensionFilter = Stream.of(SupportedTextFormats.values()).map(Enum::name)
                .map(extension -> "*."+ extension.toLowerCase())
                .collect(Collectors.toList());

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text formats ", extensionFilter);
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenDialog(tilePane.getScene().getWindow());
    }

}
