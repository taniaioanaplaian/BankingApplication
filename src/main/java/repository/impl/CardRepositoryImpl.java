package repository.impl;

import javafx.scene.control.Button;
import model.dto.ClientCardDto;
import model.entity.Client;
import model.entity.CreditCard;

import repository.JDBConnectionWrapper;
import repository.api.CardRepository;
import utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardRepositoryImpl implements CardRepository {

    private static final String LOAD_BY_IBAN = "SELECT * FROM card WHERE IBAN = ?";
    private static final String DELETE ="DELETE FROM card WHERE clientId= ?";
    private static final String INSERT = "INSERT into card(clientId, accountId, creationDate," +
            "cvv, creditCardNumber, IBAN, money) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE card SET money=? WHERE IBAN =?";
    private static final String SELECT = "SELECT * FROM card";
    private static final String LOAD_BY_CLIENT = "SELECT * FROM card WHERE clientId = ? ";

    private final JDBConnectionWrapper jdbConnectionWrapper;

    public CardRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper) {
        this.jdbConnectionWrapper = jdbConnectionWrapper;
    }


    public CreditCard loadByIban(String iban) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(LOAD_BY_IBAN);
            preparedStatement.setString(1, iban);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                CreditCard card = extractFields(resultSet);
                return card;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ClientCardDto> loadByClient(Client client) {
        Connection connection = jdbConnectionWrapper.getConnection();
        List<ClientCardDto> users = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(LOAD_BY_CLIENT);
            preparedStatement.setLong(1, client.getClientId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                ClientCardDto creditCard = new ClientCardDto(
                        resultSet.getString("iban"),
                        resultSet.getString("cvv"),
                        resultSet.getDouble("money"),
                        (double) 0,
                        new Button("Delete")
                );
                users.add(creditCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public CreditCard create(CreditCard card) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            /*
            * INSERT into user(clientId, accountId, creationDate," +
            "cvv, creditCardNumber, IBAN, money) VALUES (?, ?, ?, ?, ?, ?, ?)"
            *
            * */
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, card.getClientId());
            preparedStatement.setLong(2, card.getAccountId());
            preparedStatement.setDate(3, Utils.convertToDateViaInstant(card.getCreationDate()));

            preparedStatement.setString(4, card.getCvv());
            preparedStatement.setString(5, card.getCreditCardNumber());
            preparedStatement.setString(6, card.getIBAN());
            preparedStatement.setDouble(7, card.getMoney());

            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                card.setCardId(resultSet.getLong(1));
                return card;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CreditCard update(CreditCard card) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDouble(1, card.getMoney());
            preparedStatement.setString(2, card.getIBAN());

            int changedRows = preparedStatement.executeUpdate();

            if (changedRows > 0) {
                return card;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

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

    public CreditCard findById(Long id) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(LOAD_BY_CLIENT);
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

    public List<CreditCard> findAll() {
        Connection connection = jdbConnectionWrapper.getConnection();
        List<CreditCard> users = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                CreditCard creditCard = extractFields(resultSet);
                users.add(creditCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private CreditCard extractFields(ResultSet resultSet) throws SQLException {
        CreditCard card = new CreditCard();
        card.setCardId(resultSet.getLong("cardId"));
        card.setClientId(resultSet.getLong("clientId"));
        card.setAccountId(resultSet.getLong("accountId"));
        card.setCreationDate(Utils.convertDate(resultSet.getDate("creationDate")));
        card.setCreditCardNumber(resultSet.getString("creditCardNumber"));
        card.setCvv(resultSet.getString("cvv"));
        card.setIBAN(resultSet.getString("IBAN"));
        card.setMoney(resultSet.getDouble("money"));
        return card;
    }
}
