import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateSchema {
    public static void main(String[] args) {
        // JDBC URL for MySQL
        String url = "jdbc:mysql://localhost:3306/muzi";
        String username = "root";
        String password = "1702_Dhr";

        // SQL commands to create tables
        String createSampleTableSQL = "CREATE TABLE IF NOT EXISTS SAMPLE (" +
                "artist_id INT PRIMARY KEY," +
                "artist_name VARCHAR(100) NOT NULL," +
                "bio TEXT," +
                "genre VARCHAR(50)" +
                ")";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            // Create Artist table
            statement.executeUpdate(createSampleTableSQL);
            System.out.println("Artist table created successfully.");
            
        } catch (SQLException e) {
            System.err.println("Error creating database schema: " + e.getMessage());
        }
    }
}
