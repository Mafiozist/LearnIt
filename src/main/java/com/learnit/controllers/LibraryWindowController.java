package com.learnit.controllers;

import com.jfoenix.controls.*;
import com.learnit.MainWindow;
import com.learnit.MyUtils;
import com.learnit.database.data.tables.Book;
import com.learnit.database.data.tables.Card;
import com.learnit.database.data.tables.Tag;
import com.learnit.datasets.CardHolder;
import com.learnit.datasets.Library;
import com.learnit.datasets.TagHolder;
import com.learnit.textconverters.SupportedTextFormats;
import com.learnit.textconverters.TextConverter;
import com.learnit.textconverters.TextConverterFactory;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
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

    private ContextMenu contextMenu;
    private ObservableList<Book> books;
    private FilteredList<StackPane> filtredBooks;
    private ArrayList<StackPane> tempBooksUi;
    private ObservableList<StackPane> booksUi;
    private JFXDialog singleJfxDialog;

    //Filtering methods
    private Predicate<StackPane> containsName;
    private Predicate<StackPane> containsTags;

    public LibraryWindowController(){
        books = FXCollections.observableList(Library.getInstance().getBooks());
        tempBooksUi = new ArrayList<>();
        filtredBooks = new FilteredList<>(FXCollections.observableList(tempBooksUi));
        scrollPane = new ScrollPane();
        contextMenu = new ContextMenu();
        booksUi = FXCollections.observableList(tempBooksUi);
        MenuItem addBook = new MenuItem("Добавить");
        MenuItem addNewBook = new MenuItem("Создать");

        scrollPane.viewportBoundsProperty().addListener((observableValue, bounds, t1) -> tilePane.setPrefSize(bounds.getWidth(),bounds.getHeight()));

        addNewBook.setOnAction(actionEvent -> openEditDialog(new Book()));


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
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Setting up a context menu for tile pane
        tilePane.setOnContextMenuRequested(contextMenuEvent -> contextMenu.show(tilePane, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));

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

        selectTags.setOnMousePressed(pressed-> {
            singleJfxDialog = MyUtils.openTagSelectDialog(stackPane, TagHolder.getInstance().getTags());

            singleJfxDialog.setOnDialogClosed(event-> {

                containsTags = pane -> {

                    if(!((SelectDialogController)singleJfxDialog.getUserData()).getChangedTags().isEmpty()){
                      ArrayList<Tag> arr = new ArrayList<>( ( (SelectDialogController) singleJfxDialog.getUserData() ).getChangedTags() );
                      return ( (Book) pane.getUserData() ).getTags().containsAll(arr);
                    }

                    return false;
                };

                if(((SelectDialogController)singleJfxDialog.getUserData()).getChangedTags().isEmpty()){
                    containsTags = null;
                }

                filterUI();

            });

        });



        search.textProperty().addListener((observableValue, s, current) -> {
            //filtredBooks.setPredicate(containsName);


            containsName = i-> {
                if(!(current.isEmpty() || current.isBlank())){
                    return ((Book)i.getUserData()).getName().toLowerCase().contains(current.toLowerCase());
                }
                return false;
            };

            if(current.isEmpty() || current.isBlank()) containsName = null;

            filterUI();
        });



        addToUI();
    }

    public void filterUI(){

        if(containsName == null && containsTags != null)  filtredBooks.setPredicate(containsTags);
        else if ( (containsName != null && !isSearchEmpty() ) && containsTags == null) filtredBooks.setPredicate(containsName);
        else if( containsTags != null && containsName != null && !isSearchEmpty()) filtredBooks.setPredicate(containsName.and(containsTags));
        else if( containsTags != null && containsName != null && isSearchEmpty()) filtredBooks.setPredicate(containsName.or(containsTags)); //somehow need to deny checkboxes filter but how
        else {
            filtredBooks.setPredicate(null);
        }

        updateUi();
    }

    private boolean isSearchEmpty(){
        return search.textProperty().get().isEmpty() || search.textProperty().get().isBlank();
    }

    public void openEditDialog(Book book){
        FXMLLoader wloader = new FXMLLoader();
        wloader.setLocation(MainWindow.class.getResource("CreateEditBookWindow.fxml"));
        CreateEditBookWindowController controller = null;
        Stage bWindow = new Stage();
        bWindow.titleProperty().bind(book.getNameProperty());
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
            
            ArrayList<Card> preparedCards = finalController.getPreparedNewCards();
            
            if (book.getId() != -1){ //if book exists
                Library.getInstance().updateBook(book);

                if(finalController.getChangedTags() != null) TagHolder.getInstance().selectTags(finalController.getChangedTags(), book);//update book-tags binds

                if (preparedCards ==null) return;
                // TODO: 17.05.2022 upload data and make reference from cards to book

                for (Card card: preparedCards) {
                    CardHolder.getInstance().addCard(card,book);
                }

            }
            else if (book.getId() == -1 && !Library.getInstance().getBooks().contains(book)){ //If object aren't taken from db
                Library.getInstance().addBook(book);
                book.setId(Library.getInstance().getLastBookId()); //Костыль
                addToUI(book);

                if(finalController.getChangedTags() != null) TagHolder.getInstance().selectTags(finalController.getChangedTags(), book); //update book-tags binds

                if (preparedCards == null) return;

                for (Card card: preparedCards) {
                    CardHolder.getInstance().addCard(card,book);
                }
            }
        });

    }

    public void updateUi(){
        tilePane.getChildren().clear();
        tilePane.getChildren().addAll(filtredBooks);
    }

    public void addToUI()  {
        for (Book book: books )  {
            addToUI(book);
        }
    }

    public void addToUI(Book book) {
        FXMLLoader bookloader = new FXMLLoader();
        bookloader.setLocation(MainWindow.class.getResource("LibraryItem.fxml"));
        StackPane sp = null;

            try {
                sp = bookloader.load();

                ImageView imageView = (ImageView) sp.lookup("#imgView");
                imageView.setOnMousePressed(event -> {
                    if(event.isPrimaryButtonDown()) openEditDialog(book);
                    System.out.println(event.getTarget());
                });

                sp.setUserData(book);
                JFXButton deleteBtn = (JFXButton) sp.lookup("#deleteBtn");

                deleteBtn.setOnMousePressed(event -> {
                   if(event.isPrimaryButtonDown()){

                       JFXDialogLayout layout = new JFXDialogLayout();
                       layout.setHeading(new Text("Внимание!"));
                       layout.setBody(new Text(String.format("Вы действительно хотите удалить: %s?", book.getName())));
                       JFXButton ok = new JFXButton("Да");
                       JFXButton cancel = new JFXButton("Нет");

                       layout.setActions(ok,cancel);

                       JFXDialog jfxDialog = new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.CENTER);
                       jfxDialog.show();

                       ok.setOnMousePressed(pressed -> {
                           if (pressed.isPrimaryButtonDown()) {
                               removeFromUi(book);
                               Library.getInstance().removeBook(book);
                               jfxDialog.close();
                           }
                       });

                       cancel.setOnMousePressed(pressed -> {
                           if(pressed.isPrimaryButtonDown()){
                               jfxDialog.close();
                           }
                       });

                   }
                });

            } catch (IOException e) {
                e.printStackTrace(); // TODO: 30.04.2022 alert
            }

            LibraryItemController controller = bookloader.getController();
            controller.setBook(book);

            tilePane.getChildren().add(sp);
        tempBooksUi.add(sp);
    }

    public void removeFromUi(Book book){
        tilePane.getChildren().removeIf(tile -> ((Book) tile.getUserData()).getId() == book.getId());
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
