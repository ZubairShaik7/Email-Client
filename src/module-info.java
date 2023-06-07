module EmailClient {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.web;

    opens com.main;
    opens com.main.view;
    //opens com.main.model;
    opens com.main.controller;
}