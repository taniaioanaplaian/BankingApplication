package service.api;

import model.entity.Account;
import model.enumeration.AccountType;
import model.enumeration.CardCurrency;

import java.util.List;

public interface AccountService {

    Account createAccount(Account account);
    boolean deleteAccount(Account account);
    List<Account> findAccountsByType(AccountType type);
    List<Account> findAccounts();
    List<Account> findAccountsByCurrency(CardCurrency currency);
    Long getAccountId(AccountType accountType, CardCurrency currency);



}
