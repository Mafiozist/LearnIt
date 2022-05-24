package com.learnit.controllers;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.learnit.MainWindow;
import com.learnit.MyUtils;
import com.learnit.database.data.tables.Book;
import com.learnit.database.data.tables.Card;
import com.learnit.datasets.CardHolder;
import com.learnit.datasets.Library;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CardWindowController implements Initializable {
    @FXML private ListView<String> listview;
    @FXML private BorderPane borderPane;
    @FXML private TilePane tilePane;
    @FXML private StackPane stackPane;

    //filterData
    ObservableList<Card> cards = CardHolder.getInstance().getCards();

    private FilteredList<StackPane> cardsUiFilterList;
    private ObservableList<StackPane> cardsUiList;

    private SimpleIntegerProperty bookSize;
    private SimpleIntegerProperty cardSize;

    private JFXDialog currentFilterDialog = null;

    public CardWindowController(){
        tilePane = new TilePane();
        stackPane = new StackPane();
        borderPane = new BorderPane();
        listview = new ListView<>();
        cardsUiList = FXCollections.emptyObservableList();
        bookSize = Library.getInstance().getBooksSizeProperty();
        cardSize = CardHolder.getInstance().getCardSizeProperty();

        cards.addListener((ListChangeListener<Card>) c -> {
            System.out.println("The amount of cards changed");
            while (c.next()){
                if (c.wasAdded()){

                    for (Card card: c.getAddedSubList()) {
                        cardsUiList.add(addCardToUi(card));
                    }

                    updateFilter();
                    updateUI();
                }

                if (c.wasRemoved()){
                    removeFromUi(c.getRemoved().get(0));
                }

            }
        });
    }



    //I could use lookup to change settings from diff nodes if it will be needed
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //cardSize.bind(CardHolder.getInstance().getCardSizeProperty());
        cardSize.addListener((observable, oldValue, newValue) -> {

        });

        tilePane.setOnMousePressed(pressed->{
            System.out.println(pressed.getTarget());

            if(pressed.isMiddleButtonDown()) {
               currentFilterDialog = MyUtils.openBookSelectDialog(stackPane, Library.getInstance().getBooks());

               currentFilterDialog.setOnDialogClosed(event -> {
                   // FIXME: 22.05.2022 representing of selected books on the screen
                   SelectDialogController selectDialogController = ((SelectDialogController) currentFilterDialog.getUserData());
                   updateListView(selectDialogController.getChangedBooks());
               });

            }
        });

        addAllCardsToUi(CardHolder.getInstance().getCards());

        // TODO: 18.05.2022 remove from UI  
        // TODO: 18.05.2022 add card to ui 
        // TODO: 18.05.2022 apply tags to it, apply books to it
        // TODO: 21.05.2022 dynamic deleting tag from UI card

    }

    public void openCardEditorDialog(Card card, StackPane parent){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainWindow.class.getResource("CreateEditCardWindow.fxml"));
        //Doing some stuff with controller....

        try {
            BorderPane bp = fxmlLoader.load();
            Stage stage = new Stage();
            String q = card.getQuestion();
            String a = card.getAnswer();

            CreateEditCardWindowController controller = fxmlLoader.getController();
            controller.setData(card,null);

            Scene scene = new Scene(bp);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();

            stage.setOnHidden(hidding ->{
                StringBuilder sb = new StringBuilder(card.getAnswer());
                StringBuilder sb2 = new StringBuilder(card.getQuestion());
                if (sb.indexOf("head") == -1 || sb.indexOf("body") == -1 || sb2.indexOf("head") == -1 || sb2.indexOf("body") == -1) return; //if data wasnt get from html
                if (q.equals(card.getQuestion()) && a.equals(card.getAnswer())) return; //default cards are not saves

                try {
                    ((CardItemController)parent.getUserData()).setData(card);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CardHolder.getInstance().updateCard(card);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addAllCardsToUi(ObservableList<Card> cards){
        ArrayList<StackPane> tmpUi = new ArrayList<>();
        for (Card card : cards) {
            tmpUi.add(addCardToUi(card));
        }

        cardsUiList = FXCollections.observableArrayList(tmpUi);
        cardsUiFilterList = new FilteredList<>(cardsUiList);
        tilePane.getChildren().addAll(cardsUiFilterList);
    }

    private StackPane addCardToUi(Card card) {
        //loading card
        FXMLLoader fxmlCardLoader = new FXMLLoader();
        fxmlCardLoader.setLocation(MainWindow.class.getResource("CardItem.fxml"));

        try {
            StackPane sp = fxmlCardLoader.load();
            CardItemController itemController = fxmlCardLoader.getController();
            itemController.setData(card);
            sp.setUserData(itemController);

            initDeleteBtn(sp,sp,card);

            VBox vb = (VBox) sp.lookup("#cardVBox");

            vb.setOnMousePressed(clicked->{
                if(clicked.isPrimaryButtonDown()) openCardEditorDialog( ((CardItemController)sp.getUserData()).getCard(), sp);
            });

            return sp;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateListView(ArrayList<Book> books){
        listview.getItems().clear();

        if(books == null || books.isEmpty()){
            return;
        }

        for (Book book: books) {
            listview.getItems().add(book.getName());
        }
    }

    public void initDeleteBtn(Node node, StackPane stackPane, Card card){

        JFXButton deleteBtn = (JFXButton) node.lookup("#delete");

        deleteBtn.setOnMousePressed(event -> {
            if(event.isPrimaryButtonDown()){

                JFXDialogLayout layout = new JFXDialogLayout();

                Label header = new Label("Внимание!");
                header.setFont(Font.font("Times New Roman Bold", 24));

                layout.setHeading(header);

                Element body = Jsoup.parse(card.getQuestion()).body();

                Label label = new Label(String.format("Вы действительно хотите удалить:\n%s?", body.text()));
                label.wrapTextProperty().setValue(true);
                label.setFont(Font.font("Times new roman", 14));

                layout.setBody(label);
                JFXButton ok = new JFXButton("Удалить полностью");
                JFXButton yes = new JFXButton("Удалить");
                JFXButton cancel = new JFXButton("Нет");

                layout.setActions(ok,cancel);

                JFXDialog jfxDialog = new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.CENTER);
                jfxDialog.show();

                ok.setOnMousePressed(pressed -> {
                    if (pressed.isPrimaryButtonDown()) {
                        removeFromUi(card);
                        CardHolder.getInstance().removeCardWithStatistics(card);
                        jfxDialog.close();
                    }
                });

                yes.setOnMousePressed(pressed->{
                    if (pressed.isPrimaryButtonDown()) {
                        removeFromUi(card);
                        CardHolder.getInstance().removeCard(card);
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
    }

    void removeFromUi(Card card){
        cardsUiList.removeIf(pane-> ((CardItemController)pane.getUserData()).getCard().equals(card));
        updateFilter();
        updateUI();
    }

    public void updateFilter(){
        cardsUiFilterList = new FilteredList<>(cardsUiList);
    }

    public void updateUI(){
        tilePane.getChildren().clear();
        tilePane.getChildren().addAll(cardsUiFilterList);
    }
}
