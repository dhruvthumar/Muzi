import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionTest {
    public static void main(String[] args) {
        // JDBC URL for MySQL
        String url = "jdbc:mysql://localhost:3306/muzi";
        String username = "root";
        String password = "D#ruv@1702";

        try {
            // Establish connection
            Connection connection = DriverManager.getConnection(url, username, password);
            
            // Connection successful
            System.out.println("Connected to database successfully!");
            
            System.out.println("\nThe selected database is muzi");

            // Remember to close the connection when done
            connection.close();
        } catch (SQLException e) {
            // Connection failed
            System.err.println("Connection failed. Error message: " + e.getMessage());
        }
    }
}
