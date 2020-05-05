package repository.api;

import model.entity.Client;

import java.util.List;

public interface ClientRepository extends Crud<Client>{
    Client loadBySsn(String ssn);
    Client findByName(String firstName, String lastName);

}
