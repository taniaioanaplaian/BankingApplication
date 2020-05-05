package controller.impl;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.entity.Audit;
import model.entity.User;
import model.enumeration.Role;
import repository.api.AuditRepository;
import service.api.UserService;

import java.io.IOException;
import java.time.LocalDateTime;


public class LoginController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button loginButton;

    private final UserService userService;
    private int HEIGHT, WIDTH;

    public LoginController(UserService userService, int WIDTH, int HEIGHT) {
        this.userService = userService;
        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;
    }


    @FXML
    void initialize() {
        assert anchorPane != null : "fx:id=\"anchorPane\" was not injected: check your FXML file 'LoginView.fxml'.";
        assert usernameLabel != null : "fx:id=\"usernameLabel\" was not injected: check your FXML file 'LoginView.fxml'.";
        assert passwordLabel != null : "fx:id=\"passwordLabel\" was not injected: check your FXML file 'LoginView.fxml'.";
        assert usernameTextField != null : "fx:id=\"usernameTextField\" was not injected: check your FXML file 'LoginView.fxml'.";
        assert passwordTextField != null : "fx:id=\"passwordTextField\" was not injected: check your FXML file 'LoginView.fxml'.";
        assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'LoginView.fxml'.";
        loginButton.setOnAction(new LoginHandlerEvent());

    }
    public void clearInputs() {
        this.usernameTextField.setText("");
        this.passwordTextField.setText("");
    }

    private class LoginHandlerEvent implements EventHandler {

       @Override
       public void handle(Event event) {
           String username = usernameTextField.getText();
           String password = passwordTextField.getText();
           User currentUser = userService.login(username, password);
           if(currentUser != null) {
               //successful login
               if(currentUser.getRole().equals(Role.ADMINISTRATOR)) {
                   try {
                       MyMain.openAdminView(username);

                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               } else {
                   try {
                       MyMain.openEmployeeView(username);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           } else {
               clearInputs();
           }
       }
    }
}


