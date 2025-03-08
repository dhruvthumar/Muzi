import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ArtistStats {
    public static void main(String[] args) {
        // JDBC URL, username, and password
        String url = "jdbc:mysql://localhost:3306/muzi";
        String user = "root";
        String password = "D#ruv@1702";

        // SQL query
        String sqlQuery = "SELECT a.artist_name, l.monthly_listeners, r.net_worth " +
                          "FROM artist a " +
                          "JOIN listeners l ON a.artist_id = l.artist_id " +
                          "JOIN revenue r ON a.artist_id = r.artist_id " +
                          "ORDER BY r.net_worth DESC";

        try (
            // Establish the database connection
            Connection connection = DriverManager.getConnection(url, user, password);
            // Create a statement
            Statement statement = connection.createStatement();
            // Execute the query
            ResultSet resultSet = statement.executeQuery(sqlQuery)
        ) {
            // Print the header
            System.out.format("%-20s %-20s %-20s%n", "Artist Name", "Monthly Listeners", "Net Worth");
            // Print separator
            System.out.println("-----------------------------------------------------------------------------");

            // Print the results
            while (resultSet.next()) {
                String artistName = resultSet.getString("artist_name");
                int monthlyListeners = resultSet.getInt("monthly_listeners");
                double netWorth = resultSet.getDouble("net_worth");

                // Print each row
                System.out.format("%-20s %-20d %-20.2f%n", artistName, monthlyListeners, netWorth);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
