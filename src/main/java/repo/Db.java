package repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Db {
    private static final String URL = "jdbc:sqlite:db/app.db";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
