import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SampleQuery {
    public static void main(String[] args) {
        // JDBC URL, username, and password
        String url = "jdbc:mysql://localhost:3306/muzi";
        String username = "root";
        String password = "D#ruv@1702";

        // Database connection
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected to the database!");

            // Execute sample queries
            executeSampleQueries(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void executeSampleQueries(Connection connection) throws SQLException {
        // Sample queries
        String query1 = "SELECT * FROM Artist";

        // Execute query 1: Retrieve all artists
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query1)) {
            System.out.println("Artists:");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("artist_id") + "\t" + resultSet.getString("artist_name"));
            }
        }

    }
}
