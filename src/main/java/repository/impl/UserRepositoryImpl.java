package repository.impl;

import model.entity.Audit;
import model.entity.User;
import model.enumeration.AccountType;
import model.enumeration.Role;
import repository.JDBConnectionWrapper;
import repository.api.UserRepository;
import utils.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository{


    private static final String LOAD_BY_USERNAME = "SELECT * FROM user WHERE username = ?";
    private static final String FIND_AUDIT = "SELECT * FROM audit WHERE username = ?";
    private static final String DELETE ="DELETE FROM user WHERE userId= ?";
    private static final String INSERT = "INSERT into user(username, password, role) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE user SET username=?, password=?, role=? WHERE userId=?";
    private static final String SELECT = "SELECT * FROM user";
    private static final String LOAD_BY_ID = "SELECT * FROM user WHERE userId = ? ";

    private final JDBConnectionWrapper jdbConnectionWrapper;

    public UserRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper) {
        this.jdbConnectionWrapper = jdbConnectionWrapper;
    }

    @Override
    public User loadByUserName(String userName) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(LOAD_BY_USERNAME);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                User user = new User();
                user.setId(resultSet.getLong("userId"));
                user.setUserName(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                String role = resultSet.getString("role");
                switch (role){
                    case "ADMINISTRATOR":
                        user.setRole(Role.ADMINISTRATOR);
                        break;
                    case "EMPLOYEE":
                        user.setRole(Role.EMPLOYEE);
                        break;
                }
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Audit> findAudit(String username) {
        Connection connection = jdbConnectionWrapper.getConnection();
        List<Audit> actions = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_AUDIT);
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                Audit audit = new Audit();
                audit.setUsername(username);
                audit.setAuditId(resultSet.getLong("auditId"));
                audit.setAction(resultSet.getString("action"));
                audit.setDateTimeAction(Utils.convertDate(resultSet.getDate("creationDate")));
                actions.add(audit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actions;
    }

    @Override
    public User create(User user) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().toString());

            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                user.setId(resultSet.getLong(1));
                return user;
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public User update(User user) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().toString());
            preparedStatement.setLong(4, user.getId());

            int changedRows = preparedStatement.executeUpdate();

            if (changedRows > 0) {
                return user;
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
    public User findById(Long id) {
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
    public List<User> findAll() {
        Connection connection = jdbConnectionWrapper.getConnection();
        List<User> users = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                User user = extractFields(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private User extractFields(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong(1));
        user.setUserName(resultSet.getString(2));
        user.setPassword(resultSet.getString(3));
        String role = resultSet.getString(4);
        switch (role){
            case "ADMINISTRATOR":
                user.setRole(Role.ADMINISTRATOR);
                break;
            case "EMPLOYEE":
                user.setRole(Role.EMPLOYEE);
                break;
        }
        return user;
    }
}
