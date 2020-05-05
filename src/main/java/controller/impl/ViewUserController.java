package controller.impl;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import model.dto.UserDto;
import service.api.UserService;

public class ViewUserController {
        @FXML
        private AnchorPane root;

        @FXML
        private TableView<UserDto> table;

        @FXML
        private TableColumn<UserDto, String> userNameCol;

        @FXML
        private TableColumn<UserDto, String> roleCol;


        @FXML
        private TableColumn<UserDto, Button> deleteColumn;


        private UserService userService;
        private int height, width;

        public ViewUserController(UserService adminService, int width, int height) {
                this.userService = adminService;
                this.height = height;
                this.width = width;

        }

        @FXML
        void initialize() {
            assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'ViewUserView.fxml'.";
            assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'ViewUserView.fxml'.";
            assert userNameCol != null : "fx:id=\"usernNameCol\" was not injected: check your FXML file 'ViewUserView.fxml'.";
            assert roleCol != null : "fx:id=\"roleCol\" was not injected: check your FXML file 'ViewUserView.fxml'.";
            this.root.setPrefHeight(height);
            this.root.setPrefWidth(width);

            userNameCol.setCellValueFactory((new PropertyValueFactory<>("username")));
            roleCol.setCellValueFactory((new PropertyValueFactory<>("role")));
            deleteColumn.setCellValueFactory((new PropertyValueFactory<>("deleteButton")));

            userNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
            roleCol.setCellFactory(TextFieldTableCell.forTableColumn());

            userNameCol.setOnEditCommit(event->{
                Long id = userService.findByUsername(event.getOldValue()).getId();
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setUsername(event.getNewValue());
                userService.updateUser(id, event.getNewValue(), event.getNewValue());
            });

            roleCol.setOnEditCommit(event->{
                UserDto user = table.getSelectionModel().getSelectedItem();
                Long id = userService.findByUsername(user.getUsername()).getId();
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setUsername(event.getNewValue());
                userService.updateUser(id, event.getNewValue(), event.getNewValue());
            });


            table.setEditable(true);
            ObservableList<UserDto> users = FXCollections.observableArrayList(userService.findUsers());
            table.setItems(users);

            for(UserDto us : users){
                us.getDeleteButton().setOnAction(e->{
                    UserDto user = table.getSelectionModel().getSelectedItem();
                    String userName = user.getUsername();
                    userService.deleteUser(userName);
                    table.getItems().remove(user);

                });
            }

        }

}
