package service.api;

import model.entity.Client;

import java.util.List;

public interface ClientService {

    Client createClient(Client client);
    boolean deleteClient(String ssn);
    Client updateClient(String ssn, String clientName);
    List<Client> findAllClients();


}
