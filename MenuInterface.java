import java.sql.*;
import java.util.Scanner;

public class MenuInterface {
    public static void main(String[] args) {
        // JDBC URL, username, and password
        String url = "jdbc:mysql://localhost:3306/muzi";
        String user = "root";
        String password = "1702_Dhr";

        try {
            // Establish the database connection
            Connection connection = DriverManager.getConnection(url, user, password);
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                // Display menu options with explanations
                System.out.println("\n===== Menu =====");
                System.out.println("1. View Artists (Displays all artists in the database)");
                System.out.println("2. View Albums (Displays all albums in the database)");
                System.out.println("3. View Events (Displays all events in the database)");
                System.out.println("4. View Users Registered for Events (Displays users registered for events)");
                System.out.println("5. View Artist Revenue (Displays revenue earned by each artist)");
                System.out.println("6. Add User and Register for Event (Allows adding a new user and registering for an event)");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");
                
                // Read user input
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        viewArtists(connection);
                        break;
                    case 2:
                        viewAlbums(connection);
                        break;
                    case 3:
                        viewEvents(connection);
                        break;
                    case 4:
                        viewUsersRegisteredForEvents(connection);
                        break;
                    case 5:
                        viewArtistRevenue(connection);
                        break;
                    case 6:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                }
            }

            // Close the connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Explanation of the interface
    private static void explainInterface() {
        System.out.println("\n===== Interface Explanation =====");
        System.out.println("This application provides the following functionalities:");
        System.out.println("1. View Artists: Displays all artists in the database.");
        System.out.println("2. View Albums: Displays all albums in the database.");
        System.out.println("3. View Events: Displays all events in the database.");
        System.out.println("4. View Users Registered for Events: Displays users registered for events.");
        System.out.println("5. View Artist Revenue: Displays revenue earned by each artist.");
        System.out.println("6. Add User and Register for Event: Allows adding a new user and registering for an event.");
        System.out.println("7. Exit: Terminates the application.");
    }

    // Explanation of the DML statements
    private static void explainDMLStatements() {
        System.out.println("\n===== DML Statements Explanation =====");
        System.out.println("This application uses the following DML statements:");
        System.out.println("1. SELECT: Retrieves data from the database.");
        System.out.println("2. INSERT: Adds new records to the database.");
        System.out.println("3. UPDATE: Modifies existing records in the database.");
        System.out.println("4. DELETE: Removes records from the database.");
    }
    // View artists
    private static void viewArtists(Connection connection) throws SQLException {
        String query = "SELECT * FROM artist";
        try (
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)
        ) {
            System.out.println("\n===== Artists =====");
            while (resultSet.next()) {
                System.out.println("Artist ID: " + resultSet.getInt("artist_id"));
                System.out.println("Name: " + resultSet.getString("artist_name"));
                System.out.println("Bio: " + resultSet.getString("bio"));
                System.out.println("Genre: " + resultSet.getString("genre"));
                System.out.println();
            }
        }
    }

    // View albums
    private static void viewAlbums(Connection connection) throws SQLException {
        String query = "SELECT * FROM album";
        try (
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)
        ) {
            System.out.println("\n===== Albums =====");
            while (resultSet.next()) {
                System.out.println("Album ID: " + resultSet.getInt("album_id"));
                System.out.println("Title: " + resultSet.getString("title"));
                System.out.println("Artist ID: " + resultSet.getInt("artist_id"));
                System.out.println("Release Date: " + resultSet.getString("release_date"));
                System.out.println();
            }
        }
    }

    // View events
    private static void viewEvents(Connection connection) throws SQLException {
        String query = "SELECT * FROM events";
        try (
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)
        ) {
            System.out.println("\n===== Events =====");
            while (resultSet.next()) {
                System.out.println("Event ID: " + resultSet.getInt("event_id"));
                System.out.println("Name: " + resultSet.getString("event_name"));
                System.out.println("Date: " + resultSet.getString("event_date"));
                System.out.println("Venue: " + resultSet.getString("venue"));
                System.out.println("City: " + resultSet.getString("city"));
                System.out.println("Country: " + resultSet.getString("country"));
                System.out.println("Artist ID: " + resultSet.getInt("artist_id"));
                System.out.println();
            }
        }
    }

    // View users registered for events
    private static void viewUsersRegisteredForEvents(Connection connection) throws SQLException {
        String query = "SELECT u.username, e.event_name, e.venue " +
                       "FROM events_registration er " +
                       "JOIN user u ON er.user_id = u.user_id " +
                       "JOIN events e ON er.event_id = e.event_id";
        try (
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)
        ) {
            System.out.println("\n===== Users Registered for Events =====");
            while (resultSet.next()) {
                System.out.println("User Name: " + resultSet.getString("username"));
                System.out.println("Event Name: " + resultSet.getString("event_name"));
                System.out.println("Venue: " + resultSet.getString("venue"));
                System.out.println();
            }
        }
    }

    // View artist revenue
    private static void viewArtistRevenue(Connection connection) throws SQLException {
        String query = "SELECT a.artist_name, r.net_worth " +
                       "FROM artist a " +
                       "JOIN revenue r ON a.artist_id = r.artist_id";
        try (
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)
        ) {
            System.out.println("\n===== Artist Revenue =====");
            while (resultSet.next()) {
                System.out.println("Artist Name: " + resultSet.getString("artist_name"));
                System.out.println("Net Worth: " + resultSet.getDouble("net_worth"));
                System.out.println();
            }
        }
    }
}
