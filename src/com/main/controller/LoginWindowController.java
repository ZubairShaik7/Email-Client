package com.main.controller;

import com.main.EmailManager;
import com.main.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginWindowController extends BaseController {
        @FXML
        private TextField emailAddressField;

        @FXML
        private Label errorLabel;

        @FXML
        private PasswordField passwordField;

        public LoginWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
                super(emailManager, viewFactory, fxmlName);
        }

        @FXML
        void loginButtonAction() {
                System.out.println("Click!");
                viewFactory.showMainWindow();
                Stage stage = (Stage) errorLabel.getScene().getWindow();
                viewFactory.closeStage(stage);
        }
}