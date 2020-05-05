package service.api;

import model.dto.ClientCardDto;
import model.entity.Account;
import model.entity.Client;
import model.entity.CreditCard;

import java.util.List;

public interface EmployeeService {

    Client addClient(Client client, Account account, Double deposit);
    List<Client> findAllClients();
    List<ClientCardDto> getClientCardData(String clientName);
    boolean delete(String client);
    void makeDeposit(Double money, String iban);
    boolean payBill(String ibanSource, String company, Double money);
    boolean transferBetweenAccounts(String ibanSource, String ibanDestination, Double money);
    void setUsername(String name);

}
