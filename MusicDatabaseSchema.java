import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MusicDatabaseSchema {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "D#ruv@1702"; // Use your actual password

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            // Create database if not exists
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS muzi");
                stmt.executeUpdate("USE muzi");
                
                // Create Genres table
                String createGenresTable = "CREATE TABLE IF NOT EXISTS genres (" +
                    "genre_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(50) NOT NULL UNIQUE" +
                    ")";
                
                // Create Artists table
                String createArtistsTable = "CREATE TABLE IF NOT EXISTS artists (" +
                    "artist_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL," +
                    "country VARCHAR(50)," +
                    "formed_year INT," +
                    "bio TEXT" +
                    ")";
                
                // Create Albums table
                String createAlbumsTable = "CREATE TABLE IF NOT EXISTS albums (" +
                    "album_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "title VARCHAR(200) NOT NULL," +
                    "artist_id INT," +
                    "release_year INT," +
                    "genre_id INT," +
                    "FOREIGN KEY (artist_id) REFERENCES artists(artist_id)," +
                    "FOREIGN KEY (genre_id) REFERENCES genres(genre_id)" +
                    ")";
                
                // Create Songs table
                String createSongsTable = "CREATE TABLE IF NOT EXISTS songs (" +
                    "song_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "title VARCHAR(200) NOT NULL," +
                    "album_id INT," +
                    "track_number INT," +
                    "duration INT," + // duration in seconds
                    "genre_id INT," +
                    "FOREIGN KEY (album_id) REFERENCES albums(album_id)," +
                    "FOREIGN KEY (genre_id) REFERENCES genres(genre_id)" +
                    ")";

                // Execute all create table statements
                stmt.executeUpdate(createGenresTable);
                stmt.executeUpdate(createArtistsTable);
                stmt.executeUpdate(createAlbumsTable);
                stmt.executeUpdate(createSongsTable);
                
                System.out.println("Database schema created successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Error creating database schema: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 