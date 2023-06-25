package com.main.controller;

import com.main.EmailManager;
import com.main.controller.services.MessageRendererService;
import com.main.model.EmailMessage;
import com.main.model.EmailTreeItem;
import com.main.model.SizeInteger;
import com.main.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class MainWindowController extends BaseController implements Initializable {
    private MenuItem markUnread = new MenuItem("mark as unread");
    private MenuItem deleteMessageMenuItem = new MenuItem("delete message");
    private MenuItem showMessageDetailsMenuItem = new MenuItem("view details");
    @FXML
    private TableView<EmailMessage> emailsTableView;
    @FXML
    private TreeView<String> emailsTreeView;
    @FXML
    private WebView emailsWebView;
    @FXML
    private TableColumn<EmailMessage, String> senderCol;
    @FXML
    private TableColumn<EmailMessage, String> subjectCol;
    @FXML
    private TableColumn<EmailMessage, String> recipientCol;
    @FXML
    private TableColumn<EmailMessage, SizeInteger> sizeCol;
    @FXML
    private TableColumn<EmailMessage, Date> dateCol;

    private MessageRendererService messageRendererService;

    public MainWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void optionsAction() {
        viewFactory.showOptionsWindow();
    }

    @FXML
    void addAccountAction() {
        viewFactory.showLoginWindow();
    }

    @FXML
    void composeMessageAction() {
        viewFactory.showComposeMessageWindow();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpEmailsTreeView();
        setUpEmailsTableView();
        setUpFolderSelection();
        setUpBoldRows();
        setUpMessageRendererService();
        setUpMessageSelection();
        setUpContextMenus();
    }

    private void setUpContextMenus() {
        markUnread.setOnAction(event -> {
            emailManager.setUnRead();
        });
        deleteMessageMenuItem.setOnAction(event -> {
            emailManager.deleteSelectedMessage();
            emailsWebView.getEngine().loadContent("");
        });
        showMessageDetailsMenuItem.setOnAction(event -> {
            viewFactory.showEmailDetailsWindow();
        });
    }

    private void setUpMessageSelection() {
        emailsTableView.setOnMouseClicked(event -> {
            EmailMessage emailMessage = emailsTableView.getSelectionModel().getSelectedItem();
            if (emailMessage != null) {
                emailManager.setSelectedMessage(emailMessage);
                if (!emailMessage.isRead()) {
                    emailManager.setRead();
                }
                emailManager.setSelectedMessage(emailMessage);
                messageRendererService.setEmailMessage(emailMessage);
                messageRendererService.restart();
            }
        });
    }

    private void setUpMessageRendererService() {
        messageRendererService = new MessageRendererService(emailsWebView.getEngine());
    }

    private void setUpBoldRows() {
        emailsTableView.setRowFactory(new Callback<TableView<EmailMessage>, TableRow<EmailMessage>>() {
            @Override
            public TableRow<EmailMessage> call(TableView<EmailMessage> param) {
                return new TableRow<EmailMessage>(){
                    @Override
                    protected void updateItem(EmailMessage item, boolean empty){
                        super.updateItem(item, empty);
                        if(item != null) {
                            if(item.isRead()){
                                setStyle("");
                            } else {
                                setStyle("-fx-font-weight: bold");
                            }
                        }
                    }
                };
            }
        });
    }

    private void setUpFolderSelection() {
        emailsTreeView.setOnMouseClicked(e->{
            EmailTreeItem<String> item = (EmailTreeItem<String>)emailsTreeView.getSelectionModel().getSelectedItem();
            if (item != null) {
                emailManager.setSelectedFolder(item);
                emailsTableView.setItems(item.getEmailMessages());
            }
        });
    }

    private void setUpEmailsTableView() {
        senderCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, String>("sender")));
        subjectCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, String>("subject")));
        recipientCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, String>("recipient")));
        sizeCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, SizeInteger>("size")));
        dateCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, Date>("date")));
        emailsTableView.setContextMenu(new ContextMenu(markUnread, deleteMessageMenuItem, showMessageDetailsMenuItem));
    }

    private void setUpEmailsTreeView() {
        emailsTreeView.setRoot(emailManager.getFoldersRoot());
        emailsTreeView.setShowRoot(false);
    }
}