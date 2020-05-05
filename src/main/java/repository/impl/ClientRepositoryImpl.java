package repository.impl;

import model.entity.Account;
import model.entity.Client;
import model.entity.User;
import model.enumeration.Role;
import repository.JDBConnectionWrapper;
import repository.api.ClientRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepositoryImpl implements ClientRepository {
    private static final String LOAD_BY_SSN = "SELECT * FROM client WHERE ssn = ?";
    private static final String DELETE ="DELETE FROM client WHERE clientId= ?";
    private static final String INSERT = "INSERT into client(firstName, lastName, ssn) VALUES (?, ?,?)";
    private static final String UPDATE = "UPDATE client SET firstName =? lastName=? WHERE clientId=?";
    private static final String SELECT = "SELECT * FROM client";
    private static final String LOAD_BY_USERNAME = "SELECT * FROM client WHERE firstName = ? and lastName = ? ";

    private final JDBConnectionWrapper jdbConnectionWrapper;

    public ClientRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper) {
        this.jdbConnectionWrapper = jdbConnectionWrapper;
    }
    @Override
    public Client loadBySsn(String ssn) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(LOAD_BY_SSN);
            preparedStatement.setString(1, ssn);
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
    public Client create(Client client) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, client.getFirstName());
            preparedStatement.setString(2, client.getLastName());
            preparedStatement.setString(3, client.getSsn());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                client.setClientId(resultSet.getLong(1));
                return client;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Client update(Client client) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, client.getFirstName());
            preparedStatement.setString(2, client.getLastName());
            preparedStatement.setLong(3, client.getClientId());

            int changedRows = preparedStatement.executeUpdate();

            if (changedRows > 0) {
                return client;
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
    public Client findById(Long id) {
        return null;
    }

    @Override
    public Client findByName(String firstName, String lastName) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(LOAD_BY_USERNAME);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);

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
    public List<Client> findAll() {
        Connection connection = jdbConnectionWrapper.getConnection();
        List<Client> clients = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                Client client = extractFields(resultSet);
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }
    private Client extractFields(ResultSet resultSet) throws SQLException {
        Client client = new Client();
        client.setClientId(resultSet.getLong("clientId"));
        client.setFirstName(resultSet.getString("firstName"));
        client.setLastName(resultSet.getString("lastName"));
        client.setSsn(resultSet.getString("ssn"));
        return client;
    }
}
