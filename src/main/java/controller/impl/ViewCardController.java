package controller.impl;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import model.dto.ClientCardDto;
import model.dto.UserDto;
import model.entity.Client;
import service.api.EmployeeService;

import java.util.ArrayList;
import java.util.List;

public class ViewCardController {

    private int WIDTH, HEIGHT;
    private EmployeeService employeeService;

    public ViewCardController(EmployeeService employeeService, int width, int height) {
        this.employeeService = employeeService;
        this.WIDTH = width ;
        this.HEIGHT = height;
    }

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ChoiceBox<String> client;

    @FXML
    private Button getCardButton;

    @FXML
    private TableView<ClientCardDto> tableView;

    @FXML
    private TableColumn<ClientCardDto, String> cvvColumn;

    @FXML
    private TableColumn<ClientCardDto, String> moneyColumn;

    @FXML
    private TableColumn<ClientCardDto, String> ibanColumn;

    @FXML
    private TableColumn<ClientCardDto, Button> deleteColumn;


    @FXML
    private TableColumn<ClientCardDto, Double> sumColumn;

    @FXML
    void initialize() {
        assert anchorPane != null : "fx:id=\"anchorPane\" was not injected: check your FXML file 'ViewCard.fxml'.";
        assert client != null : "fx:id=\"client\" was not injected: check your FXML file 'ViewCard.fxml'.";
        assert getCardButton != null : "fx:id=\"getCardButton\" was not injected: check your FXML file 'ViewCard.fxml'.";
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'ViewCard.fxml'.";
        assert cvvColumn != null : "fx:id=\"cvvColumn\" was not injected: check your FXML file 'ViewCard.fxml'.";
        assert moneyColumn != null : "fx:id=\"moneyColumn\" was not injected: check your FXML file 'ViewCard.fxml'.";
        assert ibanColumn != null : "fx:id=\"ibanColumn\" was not injected: check your FXML file 'ViewCard.fxml'.";
        assert deleteColumn != null : "fx:id=\"deleteColumn\" was not injected: check your FXML file 'ViewCard.fxml'.";
        assert sumColumn != null : "fx:id=\"sumColumn\" was not injected: check your FXML file 'ViewCard.fxml'.";
        this.anchorPane.setPrefHeight(HEIGHT);
        this.anchorPane.setPrefWidth(WIDTH);

        List<Client> clients = employeeService.findAllClients();
        List<String> names = new ArrayList<>();
        for(Client c : clients){
            names.add(c.getFirstName() + " " + c.getLastName());
        }
        this.client.setItems(FXCollections.observableArrayList(names));
        this.client.setValue(names.get(0));
        this.tableView.setVisible(false);

        ibanColumn.setCellValueFactory((new PropertyValueFactory<>("iban")));
        cvvColumn.setCellValueFactory((new PropertyValueFactory<>("cvv")));
        moneyColumn.setCellValueFactory((new PropertyValueFactory<>("currentSum")));
        sumColumn.setCellValueFactory((new PropertyValueFactory<>("depositSum")));
        deleteColumn.setCellValueFactory((new PropertyValueFactory<>("deleteButton")));
        tableView.setEditable(true);
        sumColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        sumColumn.setOnEditCommit(event ->{
            ClientCardDto cl = tableView.getSelectionModel().getSelectedItem();
            String iban = cl.getIban();
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setCurrentSum(cl.getCurrentSum() + event.getNewValue());
            employeeService.makeDeposit(event.getNewValue(), iban);

            tableView.refresh();
        });

        this.getCardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tableView.setVisible(true);
               List<ClientCardDto> cardDto = employeeService.getClientCardData(client.getSelectionModel().getSelectedItem());
                tableView.setItems(FXCollections.observableArrayList(cardDto));
                for(ClientCardDto us : cardDto){
                    us.getDeleteButton().setOnAction(e->{
                        ClientCardDto user = tableView.getSelectionModel().getSelectedItem();
                        String clientName = client.getSelectionModel().getSelectedItem();
                        employeeService.delete(clientName);
                        tableView.getItems().remove(user);

                    });
                }

            }

        });

    }


}
