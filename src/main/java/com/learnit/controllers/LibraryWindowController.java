package com.learnit.controllers;

import com.jfoenix.controls.*;
import com.learnit.MainWindow;
import com.learnit.database.data.tables.Book;
import com.learnit.datasets.Library;
import com.learnit.textconverters.SupportedTextFormats;
import com.learnit.textconverters.TextConverter;
import com.learnit.textconverters.TextConverterFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


//there it could be added smooth animation of adding and changing of library cards on layout
public class LibraryWindowController implements Initializable {
    @FXML
    private TilePane tilePane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField search;
    @FXML
    private JFXButton selectTags;
    @FXML
    private StackPane stackPane;

    private ContextMenu contextMenu, removeContextMenu;
    private ObservableList<Book> books;
    private FilteredList<VBox> filtredBooks;
    private ArrayList<VBox> tempBooksUi;
    private ObservableList<VBox> booksUi;


    public LibraryWindowController(){
        books = FXCollections.observableList(Library.getInstance().getBooks());
        tempBooksUi = new ArrayList<>();
        filtredBooks = new FilteredList<>(FXCollections.observableList(tempBooksUi));
        scrollPane = new ScrollPane();
        contextMenu = new ContextMenu();
        booksUi = FXCollections.observableList(tempBooksUi);
        MenuItem addBook = new MenuItem("Добавить");
        MenuItem addNewBook = new MenuItem("Создать");
        removeContextMenu = new ContextMenu();
        MenuItem removeBook = new MenuItem("Удалить");

        scrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observableValue, Bounds bounds, Bounds t1) {
                tilePane.setPrefSize(bounds.getWidth(),bounds.getHeight());
            }
        });

        addNewBook.setOnAction(actionEvent -> {
            openEditDialog(new Book());
        });


        removeBook.setOnAction(actionEvent -> {
            //VBox vBox = (VBox) ((Node)actionEvent.getTarget());
            //LibraryItemController controller= (LibraryItemController) vBox.getUserData();
            //System.out.println(controller.book.getName());
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

        HBox box = new HBox();
        box.getChildren().add(0,new TextField());
        box.getChildren().add(1, new JFXButton());

        contextMenu.getItems().addAll(addBook,addNewBook);
        removeContextMenu.getItems().add(removeBook);
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
                    addToUI(change.getAddedSubList().get(0)); //Because at the same time, i am always add only one book
                }
                if (change.wasRemoved()){
                    removeFromUi(change.getRemoved().get(0));
                }
            }
        });

        selectTags.setOnMousePressed(pressed->{
            if(pressed.isPrimaryButtonDown()){
                JFXDialog jfxDialog = new JFXDialog();
                JFXButton save = new JFXButton();

                try {
                    jfxDialog.setContent(FXMLLoader.load(MainWindow.class.getResource("SelectDialog.fxml")));
                    jfxDialog.setDialogContainer(stackPane);
                    jfxDialog.show();

                    jfxDialog.setOnDialogClosed(jfxDialogEvent -> {

                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        search.textProperty().addListener((observableValue, s, current) -> {
            Predicate<VBox> contains = i-> ((Book)i.getUserData()).getName().toLowerCase().contains(current.toLowerCase());
            filtredBooks.setPredicate(contains);
            if(current.isEmpty() || current.isBlank()){
                filtredBooks.setPredicate(null);
            }
            tilePane.getChildren().clear();
            tilePane.getChildren().addAll(filtredBooks);
        });

        addToUI();
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
                book.setHtmlText(finalController.getHtmlEditor().getHtmlText());
            }
            if (book.getId() != -1){
                Library.getInstance().updateBook(book);
            }
            else if (book.getId() == -1){ //If object aren't taken from db

                if(!Library.getInstance().getBooks().contains(book)){
                    Library.getInstance().addBook(book);
                    addToUI(book);
                }
            }
        });
    }

    public void addToUI()  {
        for (Book book: books )  {
            addToUI(book);
        }
    }

    public void addToUI(Book book) {
        FXMLLoader bookloader = new FXMLLoader();
        bookloader.setLocation(MainWindow.class.getResource("LibraryItem.fxml"));
        VBox vBox = null;

            try {
                vBox = bookloader.load();
                vBox.setOnMousePressed(event -> {
                    if(event.isPrimaryButtonDown()) openEditDialog(book);
                });

                vBox.setUserData(book);

                VBox finalVBox = vBox;
                vBox.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                    @Override
                    public void handle(ContextMenuEvent contextMenuEvent) {
                        removeContextMenu.show(finalVBox, contextMenuEvent.getScreenX(),contextMenuEvent.getScreenY());
                    }
                });

            } catch (IOException e) {
                e.printStackTrace(); // TODO: 30.04.2022 alert
            }

            LibraryItemController controller = bookloader.getController();
            controller.setBook(book);

            tilePane.getChildren().add(vBox);
        tempBooksUi.add(vBox);
    }

    public void removeFromUi(Book book){

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
