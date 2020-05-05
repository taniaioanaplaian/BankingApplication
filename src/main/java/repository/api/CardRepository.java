package repository.api;

import model.dto.ClientCardDto;
import model.entity.Client;
import model.entity.CreditCard;

import javax.smartcardio.Card;
import java.util.List;

public interface CardRepository extends Crud<CreditCard> {

    CreditCard loadByIban(String iban);
    public List<ClientCardDto> loadByClient(Client client);

}
