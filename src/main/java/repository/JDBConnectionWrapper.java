package repository;


import java.sql.*;

public class JDBConnectionWrapper {
        private static final String DB_URL = "jdbc:mysql://localhost:3306/";
        private static final String USER = "root";
        private static final String PASS = "";
        private static final int TIMEOUT = 5;

        private Connection connection;

        public JDBConnectionWrapper(String schemaName) {
            try {

                connection = DriverManager.getConnection(DB_URL + schemaName + "?useSSL=false", USER, PASS);
                createTables();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private void createTables() throws SQLException {
            Statement statement = connection.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS user (" +
                    "  userId BIGINT(100) NOT NULL AUTO_INCREMENT," +
                    "  username varchar(255) NOT NULL," +
                    "  password varchar(255) NOT NULL," +
                    "  role varchar(255) NOT NULL," +
                    "  PRIMARY KEY (userId)," +
                    "  UNIQUE KEY id_UNIQUE (userId)" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
            statement.execute(sql);
           /* sql ="ALTER TABLE user ADD UNIQUE (username)";
            statement.execute(sql);*/

            sql = "CREATE TABLE IF NOT EXISTS client (" +
            "          clientId BIGINT(100) NOT NULL AUTO_INCREMENT," +
                    "  firstName varchar(255) NOT NULL," +
                    "  lastName varchar(255) NOT NULL," +
                    "  ssn varchar(255) NOT NULL," +
                    "  PRIMARY KEY (clientId)," +
                    "  UNIQUE KEY id_UNIQUE (clientId)" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";

            statement.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS account (" +
                    "  accountId BIGINT(100) NOT NULL AUTO_INCREMENT," +
                    "  accountType varchar(255) NOT NULL," +
                    "  currency varchar(255) NOT NULL," +
                    "  PRIMARY KEY (accountId)," +
                    "  UNIQUE KEY id_UNIQUE (accountId)" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
            statement.execute(sql);


            /*sql ="ALTER TABLE account ADD UNIQUE (accountType, currency)";
            statement.execute(sql);*/

            sql = "CREATE TABLE IF NOT EXISTS card (" +
                    "  cardId BIGINT(100) NOT NULL AUTO_INCREMENT," +
                    "  clientId BIGINT(100) NOT NULL," +
                    "  accountId BIGINT(100) NOT NULL," +
                    "  creationDate DATETIME NOT NULL," +
                    "  cvv varchar(255) NOT NULL," +
                    "  creditCardNumber varchar(255) NOT NULL," +
                    "  IBAN varchar(255) NOT NULL," +
                    "  money double NOT NULL," +
                    "  PRIMARY KEY (cardId)," +
                    "  UNIQUE KEY id_UNIQUE (cardId)," +
                    "  FOREIGN KEY (accountId) REFERENCES account(accountId)," +
                    "  FOREIGN KEY (clientId) REFERENCES client(clientId)" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";
            statement.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS transfer (" +
                    "  transferId BIGINT(100) NOT NULL AUTO_INCREMENT," +
                    "  sourceId BIGINT(100) NOT NULL," +
                    "  destinationId BIGINT(100) NOT NULL," +
                    "  creationDate DATETIME NOT NULL," +
                    "  money double NOT NULL," +
                    "  PRIMARY KEY (transferId)," +
                    "  UNIQUE KEY id_UNIQUE (transferId)," +
                    "  FOREIGN KEY (sourceId) REFERENCES card(cardId)," +
                    "  FOREIGN KEY (destinationId) REFERENCES card(cardId)" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";

            statement.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS audit (" +
                    "  auditId BIGINT(100) NOT NULL AUTO_INCREMENT," +
                    "  creationDate DATETIME NOT NULL," +
                    "  username VARCHAR(200) NOT NULL," +
                    "  PRIMARY KEY (auditId)," +
                    "  UNIQUE KEY id_UNIQUE (auditId)," +
                    "  FOREIGN KEY (username) REFERENCES user(username)" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";

            statement.execute(sql);

        }

        public boolean testConnection() throws SQLException {
            return connection.isValid(TIMEOUT);
        }

        public Connection getConnection() {
            return connection;
        }
    }

