module com.tbsg.turnbasedstrategygame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires javafx.media;


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.tbsg.turnbasedstrategygame to javafx.fxml;
    exports com.tbsg.turnbasedstrategygame;
    exports com.tbsg.turnbasedstrategygame.controllers;
    opens com.tbsg.turnbasedstrategygame.controllers to javafx.fxml;
}