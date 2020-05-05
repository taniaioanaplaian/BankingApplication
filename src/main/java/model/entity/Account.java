package model.entity;

import model.enumeration.AccountType;
import model.enumeration.CardCurrency;

public class Account {
    private Long accountId;
    private AccountType type;
    private CardCurrency currency;

    public Account() {
    }

    public Account(Long accountId, AccountType type, CardCurrency currency) {
        this.accountId = accountId;
        this.type = type;
        this.currency = currency;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public CardCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(CardCurrency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", type=" + type +
                ", currency=" + currency +
                '}';
    }

}
