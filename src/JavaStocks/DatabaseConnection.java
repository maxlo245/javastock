package JavaStocks;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/javastocks";
    private static final String USER = "admin";
    private static final String PASSWORD = "root";
    private static Connection connection = null;
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
                DbLogger.info("Tentative de connexion à " + URL + " (user=" + USER + ")");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                DbLogger.connection("Connexion établie avec succès à " + URL);
            } catch (ClassNotFoundException e) {
                DbLogger.error("DatabaseConnection", "getConnection", e);
                throw new SQLException("Driver PostgreSQL non trouvé.", e);
            } catch (SQLException e) {
                DbLogger.error("DatabaseConnection", "getConnection", e);
                throw e;
            }
        }
        return connection;
    }
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                DbLogger.disconnection("Connexion fermée.");
            } catch (SQLException e) {
                DbLogger.error("DatabaseConnection", "closeConnection", e);
            }
        }
    }
}
