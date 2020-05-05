package repository.api;

import model.entity.Account;
import model.enumeration.AccountType;
import model.enumeration.CardCurrency;

import java.util.List;

public interface AccountRepository extends Crud<Account> {

    List<Account> loadByType(AccountType type);
    List<Account> loadByCurrency(CardCurrency currency);
    public Long getId(CardCurrency currency, AccountType type);
}
