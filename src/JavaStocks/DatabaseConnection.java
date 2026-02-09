package JavaStocks;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/javastock";
    private static final String USER = "admin";
    private static final String PASSWORD = "root";
    private static Connection connection = null;
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion etablie avec succes!");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver PostgreSQL non trouve.", e);
            }
        }
        return connection;
    }
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion fermee.");
            } catch (SQLException e) {
                System.err.println("Erreur fermeture: " + e.getMessage());
            }
        }
    }
}
