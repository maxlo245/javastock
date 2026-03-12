package JavaStocks;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Simple headless server for testing the database connection
 */
public class AppServer {
    public static void main(String[] args) {
        System.out.println("JavaStock Application Server Starting...");
        
        try {
            // Test database connection
            Connection conn = DatabaseConnection.getConnection();
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Database connection successful");
                
                // Execute a simple test query
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM COUREUR");
                
                if (rs.next()) {
                    System.out.println("✓ Coureur table count: " + rs.getInt(1));
                }
                
                stmt.close();
                conn.close();
                System.out.println("✓ Application running successfully");
                System.out.println("Press Ctrl+C to stop");
                
                // Keep application running
                Thread.currentThread().join();
            } else {
                System.err.println("✗ Failed to connect to database");
                System.exit(1);
            }
            
        } catch (Exception e) {
            System.err.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
