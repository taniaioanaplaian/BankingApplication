package repository.impl;

import model.entity.Client;
import model.entity.Transfer;
import repository.JDBConnectionWrapper;
import repository.api.TransferRepository;
import utils.Utils;

import java.sql.*;
import java.util.List;

public class TransferRepositoryImpl implements TransferRepository {

    private static final String LOAD_BY_SOURCE = "SELECT * FROM transfer WHERE sourceId = ?";
    private static final String DELETE ="DELETE transfer user WHERE transferId= ?";
    private static final String INSERT = "INSERT into transfer(sourceId, destinationId, creationDate, money) VALUES (?, ?, ?, ?)";
    private static final String SELECT = "SELECT * FROM transfer";

    private final JDBConnectionWrapper jdbConnectionWrapper;

    public TransferRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper) {
        this.jdbConnectionWrapper = jdbConnectionWrapper;
    }


    @Override
    public Transfer update(Transfer transfer) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public Transfer create(Transfer transfer) {

        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, transfer.getSource());
            preparedStatement.setLong(2,transfer.getDestination());
            preparedStatement.setDate(3, Utils.convertToDateViaInstant(transfer.getTransferDateTime()));
            preparedStatement.setDouble(4,transfer.getMoney() );
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                transfer.setTransferId(resultSet.getLong(1));
                return transfer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Transfer findById(Long id) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(LOAD_BY_SOURCE);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Transfer client = new Transfer();
                client.setTransferId(resultSet.getLong("transferId"));
                client.setSource(resultSet.getLong("sourceId"));
                client.setDestination(resultSet.getLong("destinationId"));
                client.setMoney(resultSet.getDouble("money"));
                client.setTransferDateTime(Utils.convertDate(resultSet.getDate("creationDate")));
                return client;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Transfer> findAll() {
        return null;
    }
}
