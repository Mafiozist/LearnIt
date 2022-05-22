package com.learnit.wrappers;

import com.learnit.MainWindow;
import com.learnit.controllers.TagItemController;
import com.learnit.database.data.tables.Tag;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;

public class TagWrapper extends HBox {
    private Tag tag;

    public TagWrapper(URL css, Tag tag){
        this.tag = tag;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainWindow.class.getResource("TagItem.fxml"));
        TagItemController tagItemController = new TagItemController(css,tag);
        fxmlLoader.setController(tagItemController);
        try {
            super.getChildren().add(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Tag getTag() {
        return tag;
    }
}
