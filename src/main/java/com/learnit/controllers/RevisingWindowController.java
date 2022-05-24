package com.learnit.controllers;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXRadioButton;
import com.learnit.MainWindow;
import com.learnit.MyUtils;
import com.learnit.database.data.tables.Book;
import com.learnit.database.data.tables.Card;
import com.learnit.datasets.CardHolder;
import com.learnit.datasets.Library;
import com.learnit.datasets.TagHolder;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;

public class RevisingWindowController implements Initializable {
    @FXML private StackPane stackPane;
    @FXML private BorderPane root;
    @FXML private ToggleGroup revisingState;
    @FXML private JFXRadioButton all,booksOnly,tagsOnly;
    private Queue<Card> cardsQueue;

    public RevisingWindowController(){
        root = new BorderPane();
        cardsQueue = new LinkedList<>();
            }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            CardReviseFaceController cardReviseFaceController = new CardReviseFaceController(root);
            cardReviseFaceController.setData(new Card());

            fxmlLoader.setController(cardReviseFaceController);
            fxmlLoader.setLocation(MainWindow.class.getResource("CardReviseFace.fxml"));
            BorderPane bp = fxmlLoader.load();
            root.setCenter(bp);
        } catch (IOException ex){
            ex.printStackTrace(); // TODO: 12.05.2022 alert can't load interface
        }

        revisingState.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioButton rb = (RadioButton) t1.getToggleGroup().getSelectedToggle();
            ObservableList<Card> cards = CardHolder.getInstance().getCards();
            FilteredList<Card> filtredCards = new FilteredList<>(cards);

            JFXDialog dialog = null;

            switch (rb.getText()) {
                case "Все" -> {

                    filterCards(filtredCards, null);
                    cardsQueue = new LinkedList<>(sortByDate(filtredCards));

                    loadCard(cardsQueue.poll());
                }

                case "Книги" -> {
                    dialog = MyUtils.openBookSelectDialog(stackPane, Library.getInstance().getBooks());

                    JFXDialog finalDialog = dialog;
                    dialog.setOnDialogClosed(closed->{
                        ArrayList<Book> books = ((SelectDialogController) finalDialog.getUserData()).getChangedBooks();

                        if(books!= null || !books.isEmpty()){

                            filterCards(filtredCards,books);
                            cardsQueue = new LinkedList<>(sortByDate(filtredCards));

                            loadCard(cardsQueue.poll());
                        }

                    });

                }

                case "Тэги" ->{
                    // TODO: 12.05.2022 there is feature for filtering cards by tags
                    dialog = MyUtils.openTagSelectDialog(stackPane, TagHolder.getInstance().getTags());
                }
            }
        });
    }

    public List<Card> sortByDate(ObservableList<Card> cards){
        List<Card> tmp = new ArrayList<>(cards);

        //Date date = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
        tmp.sort((o1, o2) -> {
            if (o1.getNextRepetition() == null || o2.getNextRepetition() == null)
                return 0;
            return o1.getNextRepetition().compareTo(o2.getNextRepetition());
        });

        return tmp;
    }

    public void filterCards(FilteredList<Card> list, ArrayList<Book> choseBooks){
        list.setPredicate(null);
        Predicate<Card> deleted = Card::isDeleted;
        Predicate<Card> todayIsDate = i -> i.getNextRepetition().compareTo(new GregorianCalendar().getTime()) <= 0;


        if (choseBooks == null || choseBooks.isEmpty()) {
            list.setPredicate(deleted.negate().and(todayIsDate));
            return;
        }

        Predicate<Card> containsBook = card -> choseBooks.contains(card.getBook());
        list.setPredicate(deleted.negate().and(todayIsDate).and(containsBook));
    }


    public void loadCard(Card card){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainWindow.class.getResource("CardReviseFace.fxml"));

        CardReviseFaceController controller = new CardReviseFaceController(root);
        controller.setData(card);
        fxmlLoader.setController(controller);

        try {
            root.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
