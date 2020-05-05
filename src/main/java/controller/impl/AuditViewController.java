package controller.impl;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import model.dto.UserDto;
import model.entity.Audit;
import model.entity.User;
import service.api.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditViewController {

    private int WIDTH, HEIGHT;
    private UserService userService;

    public AuditViewController(UserService adminService, int width, int height) {
        this.userService = adminService;
        this.WIDTH = width;
        this.HEIGHT = height;
    }


    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ChoiceBox<String> chooseUser;

    @FXML
    private Button getAudit;

    @FXML
    private TableView<Audit> table;

    @FXML
    private TableColumn<Audit, String> usernameColumn;

    @FXML
    private TableColumn<Audit, LocalDateTime> dateColumn;

    @FXML
    private TableColumn<Audit, String> actionColumn;

    @FXML
    void initialize() {
        assert anchorPane != null : "fx:id=\"anchorPane\" was not injected: check your FXML file 'AuditView.fxml'.";
        assert chooseUser != null : "fx:id=\"chooseUser\" was not injected: check your FXML file 'AuditView.fxml'.";
        assert getAudit != null : "fx:id=\"getAudit\" was not injected: check your FXML file 'AuditView.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'AuditView.fxml'.";
        assert usernameColumn != null : "fx:id=\"usernameColumn\" was not injected: check your FXML file 'AuditView.fxml'.";
        assert dateColumn != null : "fx:id=\"dateColumn\" was not injected: check your FXML file 'AuditView.fxml'.";
        assert actionColumn != null : "fx:id=\"actionColumn\" was not injected: check your FXML file 'AuditView.fxml'.";

        this.anchorPane.setPrefHeight(HEIGHT);
        this.anchorPane.setPrefHeight(WIDTH);



        List<UserDto> users = userService.findUsers();
        List<String> names = new ArrayList<>();
        for(UserDto u : users){
            names.add(u.getUsername());
        }
        this.table.setVisible(false);
        chooseUser.setItems(FXCollections.observableArrayList(names));
        chooseUser.setValue(names.get(0));

        usernameColumn.setCellValueFactory((new PropertyValueFactory<>("username")));
        actionColumn.setCellValueFactory((new PropertyValueFactory<>("action")));
        dateColumn.setCellValueFactory((new PropertyValueFactory<>("dateTimeAction")));

        getAudit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                table.setVisible(true);
                List<Audit> audit = userService.getAudit(chooseUser.getValue());
                table.setItems(FXCollections.observableArrayList(audit));
            }
        });

    }
}
