module com.learnit.learnit {
    requires javafx.web;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;


    requires org.controlsfx.controls;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    //For parsing to html of doc/docx files
    requires mammoth;
    //For easy html using experience
    requires org.jsoup;

    opens com.learnit to javafx.fxml;
    exports com.learnit;
    exports com.learnit.textconverters;
    opens com.learnit.textconverters to javafx.fxml;
    exports com.learnit.datasets;
    opens com.learnit.datasets to javafx.fxml;
    exports com.learnit.controllers;
    opens com.learnit.controllers to javafx.fxml;

}