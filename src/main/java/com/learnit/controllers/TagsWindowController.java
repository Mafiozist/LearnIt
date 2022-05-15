package com.learnit.controllers;


import com.jfoenix.controls.*;
import com.learnit.MainWindow;
import com.learnit.database.data.tables.Tag;
import com.learnit.datasets.TagHolder;
//import com.mysql.cj.xdevapi.Schema;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.controlsfx.dialog.FontSelectorDialog;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.*;
import java.net.URL;
import java.util.*;

// TODO: 01.05.2022 create tag
// TODO: 01.05.2022 edit tag 
// TODO: 01.05.2022 update tag

//Master-detail window
public class TagsWindowController implements Initializable {
    @FXML
    TextField tagName;
    @FXML
    CheckBox transparentBackground;
    @FXML
    JFXButton changeFont,defaultButton;
    @FXML
    Slider borderInsets,borderWidth,borderRadius,backgroundRadius;
    @FXML
    TextArea cssTextArea;
    @FXML
    ColorPicker fontColorPicker,borderColorPicker,backgroundColorPicker;
    @FXML
    HBox root;
    @FXML
    JFXListView<HBox> jfxListView;
    @FXML
    private ContextMenu contextMenu;
    private MenuItem removeTag,addTag;

    private ObservableList<Tag> tags;
    private HBox hBox;
    private TagItemController currentController;
    private Font font;
    private ValidationSupport validationSupport;

    public TagsWindowController() {
        jfxListView = new JFXListView<>();
        tags= FXCollections.observableList(TagHolder.getInstance().getTags());
        borderInsets = new Slider();
        borderWidth = new Slider();
        borderRadius = new Slider();
        contextMenu = new ContextMenu();
        removeTag = new MenuItem("Удалить");
        addTag = new MenuItem("Добавить");
        contextMenu.getItems().addAll(addTag,removeTag);

        jfxListView.setOnScroll(scrollEvent -> {
            jfxListView.refresh();
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addTagsToUi(url);

        //ColorPicker dynamically change css test
        borderColorPicker.setTooltip(new Tooltip("Изменить цвет границы"));
        fontColorPicker.setTooltip(new Tooltip("Изменить цвет шрифта"));
        backgroundColorPicker.setTooltip(new Tooltip("Изменить цвет фона"));
        borderInsets.setTooltip(new Tooltip("Отступы от границ"));
        borderWidth.setTooltip(new Tooltip("Ширина границ"));
        borderRadius.setTooltip(new Tooltip("Радиус границ"));

        jfxListView.setOnContextMenuRequested(contextMenuEvent -> {
            contextMenu.show(jfxListView, contextMenuEvent.getScreenX(),contextMenuEvent.getScreenY());
        });


        tags.addListener((ListChangeListener<Tag>) change -> {

            while (change.next()){
                if(change.wasAdded()){// if tag was created by user
                    Tag tag = change.getAddedSubList().get(0);
                    TagHolder.getInstance().addTag(tag); //adding it to db
                    tag.setId(TagHolder.getInstance().getTagIdByName(tag.getName()));
                    addTagsToUi(url, tag); // cause at 1 operation adding 1 tag as well
                }
                else if(change.wasRemoved()){
                    TagHolder.getInstance().removeTag(change.getRemoved().get(0));
                    removeTagFromUi(change.getRemoved().get(0));
                }
            }
        });


        jfxListView.setOnMousePressed(event -> {
            if((event.isPrimaryButtonDown() || event.isSecondaryButtonDown()) &&
                    !jfxListView.getItems().isEmpty() && jfxListView.getSelectionModel().isSelected(jfxListView.getSelectionModel().getSelectedIndex())) {
                currentController = (TagItemController) jfxListView.getSelectionModel().getSelectedItem().getUserData();
                cssTextArea.setText(currentController.getNewCssContent());


                setIntColorParameter(borderColorPicker, "tHBox.tagitem", "-fx-border-color");
                setIntColorParameter(fontColorPicker, "tLabel.tagitem", "-fx-text-fill");
                setIntColorParameter(backgroundColorPicker, "tHBox.tagitem", "-fx-background-color");

                borderRadius.setValue(getSingleIntParameter("tHBox.tagitem", "-fx-border-radius"));
                borderWidth.setValue(getSingleIntParameter("tHBox.tagitem", "-fx-border-width"));
                borderInsets.setValue(getSingleIntParameter("tHBox.tagitem", "-fx-border-insets"));
                backgroundRadius.setValue(getSingleIntParameter("tHBox.tagitem", "-fx-background-radius"));

                tagName.setText(currentController.getTag().getName()); // TODO: 05.05.2022 change the name of the tag here and at db


                transparentBackground.selectedProperty()
                        .setValue(isStringParameterSet("tHBox.tagitem", "-fx-background-color", "transparent"));

                cssTextArea.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String old, String current) {
                        currentController.setNewCssContent(current);
                        currentController.getCssParser().updateCssFile(url,current, currentController.getTag().getId());
                        currentController.updateCssOnNode();
                    }
                });

                borderColorPicker.valueProperty().addListener((observableValue, color, t1) -> {
                    changeColorParameter("tHBox.tagitem",
                            "-fx-border-color",
                            (int) (t1.getRed() * 255),
                            (int) (t1.getGreen() * 255),
                            (int) (t1.getBlue() * 255));
                });

                fontColorPicker.valueProperty().addListener((observableValue, color, t1) -> {
                    changeColorParameter("tLabel.tagitem",
                            "-fx-text-fill",
                            (int) (t1.getRed() * 255),
                            (int) (t1.getGreen() * 255),
                            (int) (t1.getBlue() * 255));
                });

                backgroundColorPicker.valueProperty().addListener((observableValue, color, t1) -> {
                    changeColorParameter("tHBox.tagitem",
                            "-fx-background-color",
                            (int) (t1.getRed() * 255),
                            (int) (t1.getGreen() * 255),
                            (int) (t1.getBlue() * 255));
                });

                borderInsets.valueProperty().addListener((observableValue, number, t1) ->
                        changeIntParameter("tHBox.tagitem", "-fx-border-insets", (int) borderInsets.getValue()));

                borderWidth.valueProperty().addListener((observableValue, number, t1) ->
                        changeIntParameter("tHBox.tagitem", "-fx-border-width", (int) borderWidth.getValue()));

                borderRadius.valueProperty().addListener((observableValue, number, t1) ->
                        changeIntParameter("tHBox.tagitem", "-fx-border-radius", (int) borderRadius.getValue()));

                backgroundRadius.valueProperty().addListener((observableValue, number, t1) ->
                        changeIntParameter("tHBox.tagitem", "-fx-background-radius", (int) backgroundRadius.getValue()));

                defaultButton.setOnMousePressed(defaultEvent -> {
                    if (defaultEvent.isPrimaryButtonDown()) {
                        cssTextArea.setText(currentController.getCssParser().getBaseCssContent());
                    }
                });

                transparentBackground.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                    if (t1) setStringColorParameter("tHBox.tagitem", "-fx-background-color", "transparent");
                });

                removeTag.setOnAction(actionEvent -> {
                    tags.remove(currentController.getTag());
                    jfxListView.getItems().remove(jfxListView.getSelectionModel().getSelectedItem());
                });

                tagName.setOnKeyPressed(keyEvent -> {
                    if(keyEvent.getCode().equals(KeyCode.ENTER)){
                        if (tagName.getText().equals(currentController.getTag().getName())) return;

                        int id = TagHolder.getInstance().getTagIdByName(tagName.getText());

                        if(id != -1){
                            showMessageDialog("Такой тег уже существует!");
                        }
                        else {
                            currentController.getTag().setName(tagName.getText());
                            currentController.setData(currentController.getTag());
                            TagHolder.getInstance().updateTag(currentController.getTag());
                        }
                    }
                });

            }
        });

        addTag.setOnAction(actionEvent -> {
            Tag tag = new Tag();
            int id = TagHolder.getInstance().getTagIdByName(tag.getName());
            if(id != -1){
                showMessageDialog("Такой тег уже существует!");
            } else {
                tags.add(tag);
            }
        });

    }

    //They are here to take data from TextArea(cssTextArea) to change color values and give to user more comfortable control of css
    public void changeColorParameter(String cssObject,String parameter, int r, int g, int  b) throws NullPointerException {
        StringBuilder sb = new StringBuilder(cssTextArea.getText());
        String newValue = String.format("rgb(%d,%d,%d)", r, g, b);

        int[] positions = findParameterValue(cssObject,parameter);
        try {
            sb.replace(positions[0], positions[1], newValue);
            cssTextArea.setText(sb.toString());

        } catch (NullPointerException ex){
            System.out.println("Color parameter is not found"); // TODO: 05.05.2022 alert
        }

    }

    public void changeIntParameter(String cssObject,String parameter, int v){
        int[] pos =  findParameterValue(cssObject,parameter);
        StringBuilder sb = new StringBuilder(cssTextArea.getText());

        try {
            char[] value = new char[pos[1] - pos[0]];
            sb.replace(pos[0],pos[1], String.valueOf(v));
            cssTextArea.setText(sb.toString());
        }catch (NullPointerException ex){
            System.out.println("Int parameter is not found"); // TODO: 05.05.2022 alert
        }
    }

    public int getSingleIntParameter(String cssObject, String parameter){
        int[] pos =  findParameterValue(cssObject,parameter);
        StringBuilder sb = new StringBuilder(cssTextArea.getText());
        int fValue = 0;
        try {
            char[] value = new char[pos[1] - pos[0]];
            sb.getChars(pos[0],pos[1], value, 0);
            Scanner scanner = new Scanner(String.valueOf(value));
            return scanner.nextInt();
        } catch (NullPointerException ex){
            System.out.println("TagsWindowController#getSingleIntParameter: Int parameter is not found");
        }
        return fValue;
    }

    public void setIntColorParameter(ColorPicker colorPicker, String cssObject, String cssParameter){
        StringBuilder sb = new StringBuilder(cssTextArea.getText());
        int[] pos = findParameterValue(cssObject,cssParameter);
        int[] rgb = new int[] {0,0,0};

        if (pos != null) {
            char[] value = new char[sb.substring(pos[0], pos[1]).length()];
            sb.getChars(pos[0], pos[1], value, 0);
            String str = String.valueOf(value).replace(",", " ");
            str = str.replace("rgb(", "");
            str = str.replace(")","");

            if(str.equals("transparent")) { // TODO: 07.05.2022 this is need to be changed but i am running out of time and it's probably will done further
                return;
            }

            Scanner scanner = new Scanner(str);
            try {
                for (int i = 0; i < rgb.length; i++) {
                    rgb[i] = scanner.nextInt();
                    rgb[i] = (rgb[i] > 255)? 255 : Math.max(rgb[i], 0);
                }
            }catch (Exception ex){
                System.out.println("Неудалось изменить цвет, возможно стоит параметр белый или отсутсвует соотвуствующий атрибут.");
            }


            colorPicker.valueProperty().setValue(Color.rgb(rgb[0],rgb[1], rgb[2]));
        }
    }

    public void setStringColorParameter(String cssObject, String cssParameter, String value){
        StringBuilder sb = new StringBuilder(cssTextArea.getText());
        int[] pos = findParameterValue(cssObject,cssParameter);

        try {
            sb.replace(pos[0], pos[1], value);
        } catch (NullPointerException ex){
            System.out.println("TagsWindowController#setIntColorParameter: Parameter is not found");
        }

        cssTextArea.setText(sb.toString());
    }

    public boolean isStringParameterSet(String cssObject, String cssParameter, String value){
        StringBuilder sb = new StringBuilder(cssTextArea.getText());
        int[] pos = findParameterValue(cssObject,cssParameter);

        char[] val = null;
        try {
           val = new char[pos[1]-pos[0]];
           sb.getChars(pos[0],pos[1],val,0);
        } catch (NullPointerException exception){
            System.out.println("TagsWindowController#isParameterSet: Parameter is not found");
            return false;
        }
        String str = String.valueOf(val).toLowerCase();

        return str.equals(value.toLowerCase());
    }

    private int[] findParameterValue(String cssObject,String parameter) {
        StringBuilder sb = new StringBuilder(cssTextArea.getText());
        int[] positions = new int[2]; //start and end of the value that was found
        //String newValue = String.format("rgb(%d,%d,%d)", r, g, b);

        final int startFrom = sb.indexOf(cssObject);
        final int endTo = sb.indexOf("}", startFrom);

        if (startFrom == -1 || endTo==-1) return null; //If user has deleted css text from file

        int parStartPos = sb.indexOf(parameter, startFrom); // the start of necessary paremeter
        int valueEndPos = sb.indexOf(";", parStartPos);
        int valueStartPos = sb.indexOf(":", parStartPos);

        if (parStartPos == -1 || valueStartPos == -1 || valueEndPos == -1) return null;

        positions[0] = valueStartPos+1;
        positions[1] = valueEndPos;

        return positions;
    }


    public void addTagsToUi(URL url,Tag tag){
        FXMLLoader fxmlLoader = new FXMLLoader();
        TagItemController tagItemController = new TagItemController(url,tag);
        fxmlLoader.setController(tagItemController);
        fxmlLoader.setLocation(MainWindow.class.getResource("TagItem.fxml"));

        try {
            hBox = fxmlLoader.load();
            hBox.setUserData(tagItemController);
            System.out.println(fxmlLoader.getRoot().toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (hBox != null) {
            hBox.setSpacing(15d);
            hBox.setOnMousePressed(event -> {
                System.out.println(event.getTarget());
            });
        }

        jfxListView.getItems().add(0,hBox);
    }

    public void addTagsToUi(URL url){
        for (Tag tag: tags) {
            addTagsToUi(url,tag);
        }
    }

    public void removeTagFromUi(Tag tag){
        for (HBox hbox: jfxListView.getItems()) {
            TagItemController controller = (TagItemController) hbox.getUserData();
            if(controller!=null && controller.getTag().equals(tag)){
                jfxListView.getItems().remove(tag);
                controller.getCssParser().removeCssFile();
            }
        }
    }

    public void removeTagsfromUi(Tag ... tags){
        for (Tag tag1: tags) {
            removeTagFromUi(tag1);
        }
    }

    public void onMousePressed(MouseEvent event) {
        if(event.isPrimaryButtonDown()){
            FontSelectorDialog fs = new FontSelectorDialog(null);
            Optional<Font> fontOptional = fs.showAndWait();

            fontOptional.ifPresent(value -> font = value);
            if (font != null) System.out.println(font);
        }
    }

    public void showMessageDialog(String msg){
        Dialog<String> dialog = new Dialog<String>();
        dialog.setTitle("Внимание");
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.setContentText(msg);
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.show();
    }

}
