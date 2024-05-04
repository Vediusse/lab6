package viancis.lab6.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBataDase {

    private static ConnectionBataDase instance;
    private Connection connection;

    private static final String LOGIN = "postgres";
    private static final String PASSWORD = "147";
    private static final String HOST = "localhost";
    private static final String NAME = "postgres";
    private static final String DB_URL = "jdbc:postgresql://" + HOST + ":5432/" + NAME;

    public ConnectionBataDase() throws SQLException {
        this.connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
    }

    public static synchronized ConnectionBataDase getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnectionBataDase();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

}
