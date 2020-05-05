package controller.impl;
import controller.api.RootController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import model.entity.Audit;
import repository.api.AuditRepository;
import service.api.EmployeeService;

import java.io.IOException;
import java.time.LocalDateTime;

public class EmployeeController implements RootController {

        @FXML
        private BorderPane borderPane;

        @FXML
        private Button addClientButton;

        @FXML
        private Button addCardButton;

        @FXML
        private Button processTransferButton;

        @FXML
        private Button logOutButton;

        @FXML
        private AnchorPane anchorPane;

        @FXML
        private Label welcomeLabel;

        private final EmployeeService employeeService;
        private final AuditRepository auditRepository;
        private final String userName;
        private  int HEIGHT  ;
        private  int WIDTH ;

        public EmployeeController(AuditRepository auditRepository, EmployeeService employeeService, int WIDTH, int HEIGHT, String username) {
            this.employeeService = employeeService;
            this.HEIGHT = HEIGHT;
            this.WIDTH = WIDTH;
            this.userName = username;
            this.auditRepository = auditRepository;
        }


        @FXML
            void initialize() {
                assert borderPane != null : "fx:id=\"borderPane\" was not injected: check your FXML file 'EmployeeView.fxml'.";
                assert addClientButton != null : "fx:id=\"addClientButton\" was not injected: check your FXML file 'EmployeeView.fxml'.";
                assert addCardButton != null : "fx:id=\"addCardButton\" was not injected: check your FXML file 'EmployeeView.fxml'.";
                assert processTransferButton != null : "fx:id=\"processTransferButton\" was not injected: check your FXML file 'EmployeeView.fxml'.";
                assert logOutButton != null : "fx:id=\"logOutButton\" was not injected: check your FXML file 'EmployeeView.fxml'.";
                assert anchorPane != null : "fx:id=\"anchorPane\" was not injected: check your FXML file 'EmployeeView.fxml'.";
                assert welcomeLabel != null : "fx:id=\"welcomeLabel\" was not injected: check your FXML file 'EmployeeView.fxml'.";
                this.welcomeLabel.setText("Welcome " + userName);
                this.borderPane.setPrefHeight(HEIGHT);
                this.borderPane.setPrefWidth(WIDTH);
                this.anchorPane = (AnchorPane) borderPane.getCenter();
                logOut(logOutButton);


                addClientButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(this.getClass().getResource("/AddClientView.fxml"));
                        loader.setController(new AddClientController(employeeService, (int) anchorPane.getWidth(), (int) anchorPane.getHeight()));
                        AnchorPane root = null;
                        try {
                            root = loader.load();
                            borderPane.setCenter(root);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

            addCardButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(this.getClass().getResource("/ViewCard.fxml"));
                    loader.setController(new ViewCardController(employeeService, (int) anchorPane.getWidth(), (int) anchorPane.getHeight()));
                    AnchorPane root = null;
                    try {
                        root = loader.load();
                        borderPane.setCenter(root);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });

            processTransferButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(this.getClass().getResource("/TransferView.fxml"));
                    loader.setController(new TransferController(employeeService, (int) anchorPane.getWidth(), (int) anchorPane.getHeight()));
                    AnchorPane root = null;
                    try {
                        root = loader.load();
                        borderPane.setCenter(root);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            });


        }


    }




