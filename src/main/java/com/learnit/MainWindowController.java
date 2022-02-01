package com.learnit;

import com.learnit.textconverters.SupportedTextFormats;
import com.learnit.textconverters.TextConverter;
import com.learnit.textconverters.TextConverterFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.xml.transform.TransformerConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.learnit.textconverters.SupportedTextFormats.*;


//Constructor is worked before creating of xml file //This file for experements use until work it's done
//There is logic of main window
public class MainWindowController {
    @FXML
    public Tab filesTab;

    @FXML
    public Tab cardsTab;

    @FXML
    public Tab repetitionTab;
    public TextArea textArea;
    public Button setHtmlText;
    public TextField path;

    @FXML
    TabPane tabPane;

    @FXML
    GridPane FlexibleInformationContainer;

    @FXML
    Label label;
    Button button;

    @FXML
    HTMLEditor htmlEditor;

    @FXML
    Button chooseFile;
    FileChooser fileChooser;


    public void initialize(){
        //FlexibleInformationContainer = new TilePane();
        ArrayList<Button> list = new ArrayList<>();

        button = new Button();
        button.setOnAction(event -> {
            onButtonClick();
        });

        button.setPrefSize(200,200);
        button.setStyle("-fx-padding: 10px; -fx-color: red;");
        FlexibleInformationContainer.getChildren().add(button);

        //Dynamically adding new extension to filer of SupportedTextFormats ENUM by using Stream
        fileChooser = new FileChooser();
        List<String> extensionFilter = Stream.of(SupportedTextFormats.values())
                                        .map(Enum::name)
                                        .map(extension -> "*."+ extension.toLowerCase())
                                        .collect(Collectors.toList());

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt, *.docx, *.doc, *.pdf)", extensionFilter);
        fileChooser.getExtensionFilters().add(extFilter);
    }

    @FXML
    void selectSingleFile(){
        File our = fileChooser.showOpenDialog(null);
        if(our != null) path.setText(our.getAbsolutePath());
    }

    void onButtonClick(){
        System.out.println("Button clicked!");
        Runnable task = () -> {
            try {
                Thread.sleep(150);

                Platform.runLater(()->{
                    label.setText("TEXT!");
                });

                Thread.sleep(300);

                Platform.runLater(()-> {
                    label.setStyle("-fx-text-fill:yellow;-fx-font-size: 32px");
                });

            } catch (InterruptedException ex) {
                //We don't need to do Anything
                ex.printStackTrace();
            }
        };

        new Thread(task).start();
    }

    @FXML
    void onTextChanged(){
        try {
            TextConverterFactory textConverterFactory = TextConverterFactory.getInstance();
            TextConverter textConverter;
            String filePath = (path.getText().isBlank() || path.getText().isEmpty())? "C:\\Users\\dinar\\Desktop\\TextFilesExamples\\Вакансия.docx" : path.getText();

            textConverter= textConverterFactory.createTextConverter(filePath);

            textArea.setText(textConverter.convert());
            htmlEditor.setHtmlText( textArea.getText());

        } catch (IOException|NullPointerException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    void onTabChanged(){

    }

}