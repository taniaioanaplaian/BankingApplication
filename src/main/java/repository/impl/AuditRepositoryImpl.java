package repository.impl;

import model.entity.Audit;
import model.entity.Transfer;
import repository.JDBConnectionWrapper;
import repository.api.AuditRepository;
import utils.Utils;

import java.sql.*;
import java.util.List;

public class AuditRepositoryImpl implements AuditRepository {


    private static final String INSERT = "INSERT into audit(creationDate, username, action) VALUES (?, ?,?)";
    private static final String SELECT = "SELECT * FROM audit";
    private static final String LOAD_BY_USERNAME = "SELECT * FROM audit WHERE username = ? ";

    private final JDBConnectionWrapper jdbConnectionWrapper;

    public AuditRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper) {
        this.jdbConnectionWrapper = jdbConnectionWrapper;
    }


    @Override
    public Audit create(Audit audit) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT,
                    Statement.RETURN_GENERATED_KEYS);
            System.out.println(audit.getUsername()+" audit");
            preparedStatement.setDate(1, Utils.convertToDateViaInstant(audit.getDateTimeAction()));
            preparedStatement.setString(2,audit.getUsername());
            preparedStatement.setString(3,audit.getAction());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                audit.setAuditId(resultSet.getLong(1));
                return audit;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Audit update(Audit audit) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public Audit findById(Long id) {
        return null;
    }

    @Override
    public List<Audit> findAll() {
        return null;
    }

    @Override
    public Audit findByUsername(String username) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(LOAD_BY_USERNAME);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Audit audit = new Audit();
                audit.setAuditId(resultSet.getLong("auditId"));
                audit.setUsername(resultSet.getString("username"));
                audit.setAction(resultSet.getString("action"));
                audit.setDateTimeAction(Utils.convertDate(resultSet.getDate("creationDate")));
                return audit;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
