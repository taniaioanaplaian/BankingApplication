package service.impl;

import model.dto.ClientCardDto;
import model.entity.*;
import repository.api.*;
import service.api.EmployeeService;
import utils.Utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOGGER = Logger.getLogger(EmployeeServiceImpl.class.getName());
    private AccountRepository accountRepository;
    private CardRepository cardRepository;
    private ClientRepository clientRepository;
    private TransferRepository transferRepository;
    private AuditRepository auditRepository;
    private String username;

    public EmployeeServiceImpl(AuditRepository auditRepository, AccountRepository accountRepository, CardRepository cardRepository, ClientRepository clientRepository,
                               TransferRepository transferRepository){
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.clientRepository = clientRepository;
        this.transferRepository = transferRepository;
        this.auditRepository = auditRepository;

    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Client addClient(Client client, Account account, Double deposit) {

        Client newClient  = clientRepository.loadBySsn(client.getSsn());
        if(newClient == null){
            newClient = clientRepository.create(client);
        }
        long id = accountRepository.getId(account.getCurrency(), account.getType());
        System.out.println(id);
        CreditCard card = new CreditCard();
        card.setClientId(newClient.getClientId());
        card.setMoney(deposit);
        card.setIBAN(Utils.randomAlphaNumeric(30));
        card.setCvv(Utils.randomAlphaNumeric(3));
        card.setCreationDate(LocalDateTime.now());
        card.setCreditCardNumber(Utils.randomAlphaNumeric(20));
        if(id > 0){
            card.setAccountId(id);
        }else{
            account = accountRepository.create(account);
            card.setAccountId(account.getAccountId());

        }
        card = cardRepository.create(card);
        if(card.getCardId() > 0){
            auditRepository.create(new Audit(LocalDateTime.now(), username, "Client added"));
            return client;
        }
        return null;
    }

    @Override
    public List<Client> findAllClients() {
        auditRepository.create(new Audit(LocalDateTime.now(), username, "Find clients"));
        return clientRepository.findAll();
    }

    @Override
    public List<ClientCardDto> getClientCardData(String clientName) {
        List<ClientCardDto> cards;
        String[] name = clientName.split(" ");
        Client client = clientRepository.findByName(name[0], name[1]);
        cards = cardRepository.loadByClient(client);
        auditRepository.create(new Audit(LocalDateTime.now(), username, "getClientData"));
        return cards;
    }

    @Override
    public boolean delete(String client) {
        String[] name = client.split(" ");
        Long id = clientRepository.findByName(name[0], name[1]).getClientId();
        auditRepository.create(new Audit(LocalDateTime.now(), username, "Delete client"));
        return cardRepository.delete(id);
    }

    @Override
    public void makeDeposit(Double money, String iban) {
        CreditCard card = cardRepository.loadByIban(iban);
        card.setMoney(card.getMoney() + money);
        cardRepository.update(card);
        auditRepository.create(new Audit(LocalDateTime.now(), username, "Make deposit"));
    }

    @Override
    public boolean payBill(String ibanSource, String company, Double money) {
        CreditCard card = cardRepository.loadByIban(ibanSource);
        String[] name = company.split(" ");
        Client client = clientRepository.findByName(name[0], name[1]);
        List<ClientCardDto> companyClient = cardRepository.loadByClient(client);
        Double availableMoney = card.getMoney();
        if(availableMoney < money)
            return false;
        else{
            Transfer transfer = new Transfer();
            makeDeposit(-money, ibanSource);
            transfer.setSource(card.getCardId());
            makeDeposit(money, companyClient.get(0).getIban());
            transfer.setTransferDateTime(LocalDateTime.now());
            transfer.setMoney(money);
            card = cardRepository.loadByIban(companyClient.get(0).getIban());
            transfer.setDestination(card.getCardId());

            if(transferRepository.create(transfer)==null)
                return false;
            else {
                auditRepository.create(new Audit(LocalDateTime.now(), username, "Pay Bill"));
                return true;
            }
        }

    }

    @Override
    public boolean transferBetweenAccounts(String ibanSource, String ibanDestination, Double money) {
        CreditCard source = cardRepository.loadByIban(ibanSource);
        CreditCard destination = cardRepository.loadByIban(ibanDestination);
        Double availableMoney = source.getMoney();
        if(availableMoney < money){
            return false;
        }
        else{
            makeDeposit(-money, ibanSource);
            makeDeposit(money, ibanDestination);
            Transfer transfer = new Transfer();
            transfer.setTransferDateTime(LocalDateTime.now());
            transfer.setMoney(money);
            transfer.setDestination(destination.getCardId());
            transfer.setSource(source.getCardId());
            if(transferRepository.create(transfer)==null)
                return false;
            else {
                auditRepository.create(new Audit(LocalDateTime.now(), username, "Transfer"));
                return true;
            }
        }

    }


}
