package repository.impl;

import model.entity.Account;
import model.entity.User;
import model.enumeration.AccountType;
import model.enumeration.CardCurrency;
import model.enumeration.Role;
import repository.JDBConnectionWrapper;
import repository.api.AccountRepository;

import javax.smartcardio.Card;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountRepositoryImpl implements AccountRepository {

    private static final String LOAD_BY_CURRENCY = "SELECT * FROM account WHERE currency = ?";
    private static final String LOAD_BY_TYPE = "SELECT * FROM account WHERE accountType = ?";
    private static final String DELETE ="DELETE FROM account WHERE accountId= ?";
    private static final String INSERT = "INSERT into account(accountType, currency) VALUES (?, ?)";
    private static final String UPDATE = "UPDATE account SET accountType =? currency=? WHERE accountId=?";
    private static final String SELECT = "SELECT * FROM account";
    private static final String LOAD_BY_ID = "SELECT * FROM account WHERE accountId = ? ";
    private static final String FIND_ID = "SELECT * FROM account WHERE currency = ? and accountType = ? ";

    private final JDBConnectionWrapper jdbConnectionWrapper;

    public AccountRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper) {
        this.jdbConnectionWrapper = jdbConnectionWrapper;
    }

    public Long getId(CardCurrency currency, AccountType type){
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ID);
            preparedStatement.setString(1, currency.toString());
            preparedStatement.setString(2, type.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return extractFields(resultSet).getAccountId();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Account> loadByType(AccountType type) {

        return getAccounts(LOAD_BY_TYPE);
    }


    @Override
    public List<Account> loadByCurrency(CardCurrency currency) {

        return getAccounts(LOAD_BY_CURRENCY);
    }

    private List<Account> getAccounts(String loadByCurrency) {
        Connection connection = jdbConnectionWrapper.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(loadByCurrency);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                Account account = extractFields(resultSet);
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Account create(Account account) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getType().toString());
            preparedStatement.setString(2, account.getCurrency().toString());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                account.setAccountId(resultSet.getLong(1));
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Account update(Account account) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getType().toString());
            preparedStatement.setString(2, account.getCurrency().toString());
            preparedStatement.setLong(3, account.getAccountId());

            int changedRows = preparedStatement.executeUpdate();

            if (changedRows > 0) {
                return account;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, id);

            int updatedRows = preparedStatement.executeUpdate();

            return updatedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Account findById(Long id) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(LOAD_BY_ID);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return extractFields(resultSet);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Account> findAll() {
        return getAccounts(SELECT);
    }


    private Account extractFields(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setAccountId(resultSet.getLong(1));
        String type = resultSet.getString(2);
        switch (type){
            case "SAVINGS":
                account.setType(AccountType.SAVINGS);
                break;
            case "SHOPPING":
                account.setType(AccountType.SHOPPING);
                break;
        }

        String currency = resultSet.getString(3);
        switch (currency){
            case "EUR":
                account.setCurrency(CardCurrency.EUR);
                break;
            case "RON":
                account.setCurrency(CardCurrency.RON);
                break;
        }
        return account;
    }

}
