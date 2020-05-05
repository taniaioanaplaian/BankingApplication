package controller.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.dto.ClientCardDto;
import model.entity.Client;
import service.api.EmployeeService;
import utils.Utils;

public class TransferController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ChoiceBox<String> client;

    @FXML
    private Button transfer;

    @FXML
    private ChoiceBox<String> source;

    @FXML
    private ChoiceBox<String> destination;

    @FXML
    private TextField money;

    @FXML
    private ChoiceBox<String> company;

    @FXML
    private Button pay;

    @FXML
    private Button getCards;

    @FXML
    private Label messageLabel;

    private final EmployeeService employeeService;
    private int width;
    private int height;

    public TransferController(EmployeeService employeeService, int width, int height) {
        this.employeeService = employeeService;
        this.width = width;
        this.height = height;
    }

    @FXML
    void initialize() {
        assert anchorPane != null : "fx:id=\"anchorPane\" was not injected: check your FXML file 'TransferView.fxml'.";
        assert client != null : "fx:id=\"client\" was not injected: check your FXML file 'TransferView.fxml'.";
        assert transfer != null : "fx:id=\"transfer\" was not injected: check your FXML file 'TransferView.fxml'.";
        assert source != null : "fx:id=\"source\" was not injected: check your FXML file 'TransferView.fxml'.";
        assert destination != null : "fx:id=\"destination\" was not injected: check your FXML file 'TransferView.fxml'.";
        assert money != null : "fx:id=\"money\" was not injected: check your FXML file 'TransferView.fxml'.";
        assert company != null : "fx:id=\"company\" was not injected: check your FXML file 'TransferView.fxml'.";
        assert pay != null : "fx:id=\"pay\" was not injected: check your FXML file 'TransferView.fxml'.";

        this.anchorPane.setPrefWidth(width);
        this.anchorPane.setPrefHeight(height);

        List<Client> clients = employeeService.findAllClients();
        List<String> names = new ArrayList<>();
        List<String> companies = new ArrayList<>();
        List<String> ibans = new ArrayList<>();
        for(Client c : clients){
            if(c.getFirstName().equals("Company"))
            {
                companies.add(c.getFirstName() + " " + c.getLastName());}
            else names.add(c.getFirstName() + " " + c.getLastName());
        }
        this.client.setItems(FXCollections.observableArrayList(names));

        this.company.setItems(FXCollections.observableArrayList(companies));

        getCards.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String c = client.getSelectionModel().getSelectedItem();
                List<ClientCardDto> emp = employeeService.getClientCardData(c);
                for(ClientCardDto card : emp){
                   ibans.add(card.getIban());
                }
                source.setItems(FXCollections.observableArrayList(ibans));
                source.setValue(ibans.get(0));
                destination.setItems(FXCollections.observableArrayList(ibans));
                destination.setValue(ibans.get(0));
            }
        });

        transfer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean success = employeeService.transferBetweenAccounts(source.getSelectionModel().getSelectedItem(), destination.getSelectionModel().getSelectedItem(),
                        Double.parseDouble(money.getText()));
                if(success){
                    Utils.displayMessage(messageLabel, "Success!", "GREEN");
                }
                else{
                    Utils.displayMessage(messageLabel, "Failed!", "RED");
                }
            }
        });

        pay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean success = employeeService.payBill(source.getSelectionModel().getSelectedItem(), company.getSelectionModel().getSelectedItem(), Double.parseDouble(money.getText()));
                if(success){
                    Utils.displayMessage(messageLabel, "Success!", "GREEN");
                }
                else{
                    Utils.displayMessage(messageLabel, "Failed!", "RED");
                }

            }
        });

    }
}
