module com.tbsg.turnbasedstrategygame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires javafx.media;
    
    requires com.google.gson;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.tbsg.turnbasedstrategygame to javafx.fxml,com.google.gson;
    exports com.tbsg.turnbasedstrategygame;
    exports com.tbsg.turnbasedstrategygame.controllers;
    exports com.tbsg.turnbasedstrategygame.library.engine;
    opens com.tbsg.turnbasedstrategygame.controllers to javafx.fxml;
    opens com.tbsg.turnbasedstrategygame.library.engine to com.google.gson;
}