package controller.impl;

import java.io.IOException;

import controller.api.RootController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import repository.api.AuditRepository;
import service.api.UserService;

public class AdminController implements RootController {

    private final UserService adminService;
    private final String userName;
    private  int HEIGHT  ;
    private  int WIDTH ;

    @FXML
    private BorderPane borderPane;
    @FXML
    private Label welcomeLabel;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button createButton;

    @FXML
    private Button viewButton;
    @FXML
    private Button generateButton;
    @FXML
    private Button logOutButton;
    @FXML
    private Button auditButton;

    public AdminController(AuditRepository auditRepository, UserService adminService, int WIDTH, int HEIGHT, String username) {
        this.adminService = adminService;
        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;
        this.userName = username;
    }


    @FXML
    void initialize() {
        assert borderPane != null : "fx:id=\"borderPane\" was not injected: check your FXML file 'AdminView.fxml'.";
        assert welcomeLabel != null : "fx:id=\"welcomeLabel\" was not injected: check your FXML file 'AdminView.fxml'.";
        assert anchorPane != null : "fx:id=\"anchorPane\" was not injected: check your FXML file 'AdminView.fxml'.";
        assert createButton != null : "fx:id=\"createButton\" was not injected: check your FXML file 'AdminView.fxml'.";
        assert viewButton != null : "fx:id=\"viewButton\" was not injected: check your FXML file 'AdminView.fxml'.";
        assert generateButton != null : "fx:id=\"generateButton\" was not injected: check your FXML file 'AdminView.fxml'.";
        assert logOutButton != null : "fx:id=\"logOutButton\" was not injected: check your FXML file 'AdminView.fxml'.";
        this.welcomeLabel.setText("Welcome " + userName);
        this.borderPane.setPrefHeight(HEIGHT);
        this.borderPane.setPrefWidth(WIDTH);
        this.anchorPane = (AnchorPane) borderPane.getCenter();


        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(AdminController.this.getClass().getResource("/CreateUserView.fxml"));
                loader.setController(new CreateUserController(adminService, (int) anchorPane.getWidth(), (int) anchorPane.getHeight()));
                AnchorPane root = null;
                try {
                    root = loader.load();
                    borderPane.setCenter(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        viewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openView();
            }
        });

        auditButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(AdminController.this.getClass().getResource("/AuditView.fxml"));
                loader.setController(new AuditViewController(adminService, (int) anchorPane.getWidth(), (int) anchorPane.getHeight()));
                AnchorPane root = null;
                try {
                    root = loader.load();
                    borderPane.setCenter(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        logOut(logOutButton);

    }

    private void openView() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AdminController.this.getClass().getResource("/ViewUserView.fxml"));
        loader.setController(new ViewUserController(adminService, (int) anchorPane.getWidth(), (int) anchorPane.getHeight()));
        AnchorPane root = null;
        try {
            root = loader.load();
            borderPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
