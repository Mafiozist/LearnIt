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
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;

public class RevisingWindowController implements Initializable{
    @FXML private StackPane stackPane;
    @FXML private BorderPane root;
    @FXML private ToggleGroup revisingState;
    @FXML private JFXRadioButton all,booksOnly,tagsOnly;

    private volatile Queue<Card> cardsQueue;

    private ObservableList<Card> cards;
    private volatile FilteredList<Card> filtredCards;
    private ArrayList<Book> lastChoseBooks;

    private volatile Date currentDateTime;

    private SimpleObjectProperty<Node> currentCardUi;
    private volatile SimpleBooleanProperty hasAnyAvaliableCards;
    private Thread checkCardsAvailability, updateTime;
    private Runnable checkCardsAvailabilityTask;

    public RevisingWindowController(){
        root = new BorderPane();
        cardsQueue = new LinkedList<>();
        cards = CardHolder.getInstance().getCards();
        filtredCards = new FilteredList<>(cards);
        hasAnyAvaliableCards = new SimpleBooleanProperty(false);

        cards.addListener((ListChangeListener<Card>) c -> filtredCards = new FilteredList<>(cards));

        checkCardsAvailabilityTask = ()->{
            while (true){
                System.out.println(filtredCards);

                if(!filtredCards.isEmpty()){
                    hasAnyAvaliableCards.set(true);
                    updateQueue();
                    //System.out.println("Карты появились");
                    return;
                }else {
                    filterCards(filtredCards,lastChoseBooks);
                    //System.out.println("Карт пока нет");
                }
            }
        };

        checkCardsAvailability = new Thread(checkCardsAvailability);

        checkCardsAvailability.setDaemon(true);

        hasAnyAvaliableCards.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                Platform.runLater(()->loadCard(cardsQueue.poll()));
            }
        });


        updateTime = new Thread(()->{
            while (true) currentDateTime = new GregorianCalendar().getTime();
        });
        updateTime.setDaemon(true);
        updateTime.start();

        currentCardUi = new SimpleObjectProperty<>();
        currentCardUi.bindBidirectional(root.centerProperty());

        currentCardUi.addListener((observable, oldValue, newValue) -> {
            //System.out.println("Changed");
            root.centerProperty().setValue(newValue);

            if(newValue == null && cardsQueue.isEmpty()){
                filterCards(filtredCards,lastChoseBooks);
                hasAnyAvaliableCards.set(false);

                checkCardsAvailability = new Thread(checkCardsAvailabilityTask);
                checkCardsAvailability.start();

                return;
            }

            if(newValue == null){
                updateUi();
            }

        });


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(currentCardUi.getValue() == null) MyUtils.openMessageDialog(stackPane, "Внимание!",
                "Вам необходимо выбрать вариант группировки справа!");

        revisingState.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioButton rb = (RadioButton) t1.getToggleGroup().getSelectedToggle();

            JFXDialog dialog = null;

            switch (rb.getText()) {
                case "Все" -> {
                    currentCardUi.setValue(null);

                    lastChoseBooks = null;
                    filterCards(filtredCards, null);
                    updateQueue();
                    updateUi();
                }

                case "Книги" -> {
                    currentCardUi.setValue(null);
                    dialog = MyUtils.openBookSelectDialog(stackPane, Library.getInstance().getBooks());

                    JFXDialog finalDialog = dialog;

                    dialog.setOnDialogClosed(closed->{
                        lastChoseBooks = ((SelectDialogController) finalDialog.getUserData()).getChangedBooks();

                        if(lastChoseBooks!= null || !lastChoseBooks.isEmpty()){

                            filterCards(filtredCards, lastChoseBooks);
                            updateQueue();
                            updateUi();
                        }

                    });
                }

                case "Тэги" ->{
                    root.centerProperty().setValue(null);
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
        Predicate<Card> todayIsDate = i -> {
            System.out.println("Дата следующего повторения "+ i.getNextRepetition() + "  Сравниваю с " + currentDateTime);
            return i.getNextRepetition().compareTo(currentDateTime) <= 0;
        };

        if (choseBooks == null || choseBooks.isEmpty()) {
            list.setPredicate(deleted.negate().and(todayIsDate));
            return;
        }

        Predicate<Card> containsBook = card -> choseBooks.contains(card.getBook());
        list.setPredicate(deleted.negate().and(todayIsDate).and(containsBook));
    }

    public void updateQueue(){
        cardsQueue = new LinkedList<>(filtredCards);
    }

    public void updateUi(){
            if(currentCardUi.getValue() == null) {
               loadCard(cardsQueue.poll());
            }

        //MyUtils.openMessageDialog(stackPane, "Внимание!", "Отсутсвуют карты для повторения!\nВозможно вы повторили все на сегодня!");
    }

    public void  loadCard(Card card){
            //System.out.println("LoadingCard:" + card.getQuestion());
            if (card == null) return;

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainWindow.class.getResource("CardReviseFace.fxml"));

            CardReviseFaceController controller = new CardReviseFaceController(currentCardUi);
            controller.setData(card);
            fxmlLoader.setController(controller);

            try {
               currentCardUi.setValue(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


}
