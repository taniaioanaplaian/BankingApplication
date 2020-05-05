package controller.impl;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.entity.User;
import model.enumeration.Role;
import service.api.UserService;
import utils.Utils;

public class CreateUserController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label userLabel;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private Button registerButton;
    @FXML
    private Label messageLabel;

    private int WIDTH, HEIGHT;
    private UserService userService;

    public CreateUserController(UserService userService,int WIDTH, int HEIGHT) {
        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;
        this.userService = userService;
    }

    @FXML
    void initialize() {
        assert userLabel != null : "fx:id=\"userLabel\" was not injected: check your FXML file 'CreateUserView.fxml'.";
        assert usernameTextField != null : "fx:id=\"usernameTextField\" was not injected: check your FXML file 'CreateUserView.fxml'.";
        assert passwordTextField != null : "fx:id=\"passwordTextField\" was not injected: check your FXML file 'CreateUserView.fxml'.";
        assert choiceBox != null : "fx:id=\"choiceBox\" was not injected: check your FXML file 'CreateUserView.fxml'.";
        assert registerButton != null : "fx:id=\"registerButton\" was not injected: check your FXML file 'CreateUserView.fxml'.";
        this.choiceBox.setItems(FXCollections.observableArrayList(
                "Administrator", "Employee"));
        this.choiceBox.setValue("Employee");
        anchorPane.setPrefHeight(HEIGHT);
        anchorPane.setPrefWidth(WIDTH);


        this.registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String username = usernameTextField.getText();
                String password = passwordTextField.getText();
                String role = choiceBox.getSelectionModel().getSelectedItem();
                if (username != null && password != null && role != null) {
                    User user;
                        switch (role) {
                        case "Administrator":
                            user = userService.register(username, password, Role.ADMINISTRATOR);
                            if(user != null){
                                Utils.displayMessage(messageLabel, "User "+ user.getUserName() + " with role "+ user.getRole().toString() + " successfully " +
                                        "registered!", "GREEN");
                                break;
                            }else{
                                Utils.displayMessage(messageLabel, "Error at registration, username taken!", "RED");

                            }
                        case "Employee":
                            user = userService.register(username, password, Role.EMPLOYEE);
                            if(user != null){
                                Utils.displayMessage(messageLabel, "User "+ user.getUserName() + " with role "+ user.getRole().toString() + " successfully " +
                                        "registered!", "GREEN");
                                break;
                            }else{
                                Utils.displayMessage(messageLabel, "Error at registration, username taken!", "RED");
                            }

                            default:
                                Utils.displayMessage(messageLabel, "Error at registration", "RED");
                        }

                    
                }else{
                    Utils.displayMessage(messageLabel, "Please fill all fields!", "RED");
                }
            }
        });

    }
}
