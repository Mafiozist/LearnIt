package com.learnit.controllers;

import com.jfoenix.controls.JFXButton;
import com.learnit.MainWindow;
import com.learnit.database.data.tables.Card;
import com.learnit.database.data.tables.Tag;
import com.learnit.datasets.TagHolder;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

//This controller will be able to change data and apperience of card view
public class CardItemController implements Initializable {

    @FXML private StackPane stackPane;
    @FXML private JFXButton delete;
    @FXML private VBox cardVBox;
    @FXML private GridPane tagsGridPane;
    @FXML private Label questionLabel;
    private Card card;

    public CardItemController(){

    }

    //The method uses for setting data from card (Card.java) into CardItem.fxm by serializing
    public void setData(Card card) throws IOException {
        this.card = card;
        Element body = Jsoup.parse(card.getQuestion()).body();
        questionLabel.setText(body.text());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Tag> tags = TagHolder.getInstance().getTags();

        /*for (int i = 0, p=0; i < 3; i++) {

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(MainWindow.class.getResource("TagItem.fxml"));
                HBox hBox;
                try {
                    TagItemController tagItemController= new TagItemController(url,tags.get(i));
                    fxmlLoader.setController(tagItemController);

                    hBox = fxmlLoader.load();
                    hBox.setUserData(tags.get(i));

                    tagsGridPane.setAlignment(Pos.CENTER);
                    tagsGridPane.add(hBox,0,i );

                } catch (IOException e) {
                    e.printStackTrace();
                }
        } */

    }
    public Card getCard() {
        return card;
    }

    public void removeTagFromUi(Tag tag){
        tagsGridPane.getChildren().removeIf(tagNode -> ((Tag) tagNode.getUserData()).equals(tag));
    }


}
