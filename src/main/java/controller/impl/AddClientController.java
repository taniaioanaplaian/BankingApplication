package controller.impl;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.entity.Account;
import model.entity.Client;
import model.enumeration.AccountType;
import model.enumeration.CardCurrency;
import service.api.EmployeeService;
import utils.Utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class AddClientController {

    @FXML
    private Label messageLabel;
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField ssn;

    @FXML
    private ChoiceBox<String> accountType;
    @FXML
    private ChoiceBox<String> currency;

    @FXML
    private TextField deposit;

    @FXML
    private Button registerButton;
    private int WIDTH, HEIGHT;
    private EmployeeService employeeService;

    public AddClientController(EmployeeService employeeService, int width, int height) {
        this.employeeService = employeeService;
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    @FXML
    void initialize() {

        anchorPane.setPrefWidth(WIDTH);
        anchorPane.setPrefHeight(HEIGHT);
        List<String> enumNames = Stream.of(AccountType.values())
                .map(AccountType::name)
                .collect(Collectors.toList());
        List<String> currencies = Stream.of(CardCurrency.values())
                .map(CardCurrency::name)
                .collect(Collectors.toList());

        this.accountType.setItems(FXCollections.observableArrayList(
                enumNames));
        this.accountType.setValue(enumNames.get(0));

        this.currency.setItems(FXCollections.observableArrayList(
                currencies));
        this.currency.setValue(currencies.get(0));

        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Client client = new Client();
                client.setFirstName(firstName.getText());
                client.setLastName(lastName.getText());
                client.setSsn(ssn.getText());
                Account account = new Account();
                account.setCurrency(CardCurrency.valueOf(currency.getSelectionModel().getSelectedItem()));
                account.setType(AccountType.valueOf(accountType.getSelectionModel().getSelectedItem()));
                Double money = Double.parseDouble(deposit.getText());

                client = employeeService.addClient(client, account, money);
                if(client != null){
                    Utils.displayMessage(messageLabel, "Success!", "GREEN");

                }else{
                    Utils.displayMessage(messageLabel, "Failed!", "RED");
                }

            }
        });
    }



}
