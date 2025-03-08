import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class DataGenerator {
    private static final String URL = "jdbc:mysql://localhost:3306/muzi";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "D#ruv@1702"; // Use your actual password
    private static final Random random = new Random();

    // Sample data arrays
    private static final String[] GENRES = {
        "Rock", "Pop", "Hip Hop", "Jazz", "Classical", "Electronic", "R&B", "Country",
        "Blues", "Metal", "Folk", "Reggae", "Soul", "Funk", "Disco"
    };

    private static final String[] ARTIST_NAMES = {
        "The Midnight Dreams", "Electric Storm", "Sonic Wave", "Crystal Voice",
        "Dark Matter", "Neon Lights", "Urban Echo", "Desert Wind", "Ocean Sound",
        "Mountain Peak", "Valley Deep", "Forest Echo", "City Pulse", "Star Dust",
        "Moon Shadow", "Sun Ray", "Cloud Nine", "Thunder Strike", "Rainbow Arc",
        "Silver Sound"
    };

    private static final String[] COUNTRIES = {
        "USA", "UK", "Canada", "Australia", "Germany", "France", "Japan", "Brazil",
        "Sweden", "Norway", "Italy", "Spain", "Netherlands", "Denmark", "Ireland"
    };

    private static final String[] ALBUM_WORDS = {
        "Dreams", "Journey", "Stories", "Echoes", "Visions", "Memories", "Horizons",
        "Reflections", "Moments", "Destiny", "Paradise", "Infinity", "Evolution",
        "Legacy", "Symphony", "Harmony", "Rhythm", "Melody", "Beat", "Pulse"
    };

    private static final String[] SONG_WORDS = {
        "Love", "Night", "Day", "Heart", "Soul", "Mind", "Dream", "Life", "Time",
        "Sky", "Ocean", "Fire", "Earth", "Wind", "Star", "Moon", "Sun", "Rain",
        "Light", "Shadow", "Dance", "Song", "Music", "Voice", "Sound"
    };

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            // Generate and insert genres
            List<Integer> genreIds = insertGenres(conn);
            
            // Generate and insert artists
            List<Integer> artistIds = insertArtists(conn);
            
            // Generate and insert albums
            List<Integer> albumIds = insertAlbums(conn, artistIds, genreIds);
            
            // Generate and insert songs
            insertSongs(conn, albumIds, genreIds);
            
            System.out.println("Data generation completed successfully!");
            
        } catch (SQLException e) {
            System.err.println("Error generating data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<Integer> insertGenres(Connection conn) throws SQLException {
        List<Integer> genreIds = new ArrayList<>();
        String sql = "INSERT INTO genres (name) VALUES (?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (String genre : GENRES) {
                pstmt.setString(1, genre);
                pstmt.executeUpdate();
                genreIds.add(getGeneratedId(pstmt));
            }
        }
        return genreIds;
    }

    private static List<Integer> insertArtists(Connection conn) throws SQLException {
        List<Integer> artistIds = new ArrayList<>();
        String sql = "INSERT INTO artists (name, country, formed_year, bio) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (String artistName : ARTIST_NAMES) {
                pstmt.setString(1, artistName);
                pstmt.setString(2, COUNTRIES[random.nextInt(COUNTRIES.length)]);
                pstmt.setInt(3, 1960 + random.nextInt(63)); // Random year between 1960 and 2023
                pstmt.setString(4, "A " + GENRES[random.nextInt(GENRES.length)].toLowerCase() + 
                               " band formed in " + COUNTRIES[random.nextInt(COUNTRIES.length)]);
                pstmt.executeUpdate();
                artistIds.add(getGeneratedId(pstmt));
            }
        }
        return artistIds;
    }

    private static List<Integer> insertAlbums(Connection conn, List<Integer> artistIds, List<Integer> genreIds) throws SQLException {
        List<Integer> albumIds = new ArrayList<>();
        String sql = "INSERT INTO albums (title, artist_id, release_year, genre_id) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (Integer artistId : artistIds) {
                int numAlbums = 1 + random.nextInt(5); // 1-5 albums per artist
                for (int i = 0; i < numAlbums; i++) {
                    String albumTitle = ALBUM_WORDS[random.nextInt(ALBUM_WORDS.length)] + " of " +
                                     ALBUM_WORDS[random.nextInt(ALBUM_WORDS.length)];
                    pstmt.setString(1, albumTitle);
                    pstmt.setInt(2, artistId);
                    pstmt.setInt(3, 1970 + random.nextInt(53)); // Random year between 1970 and 2023
                    pstmt.setInt(4, genreIds.get(random.nextInt(genreIds.size())));
                    pstmt.executeUpdate();
                    albumIds.add(getGeneratedId(pstmt));
                }
            }
        }
        return albumIds;
    }

    private static void insertSongs(Connection conn, List<Integer> albumIds, List<Integer> genreIds) throws SQLException {
        String sql = "INSERT INTO songs (title, album_id, track_number, duration, genre_id) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Integer albumId : albumIds) {
                int numSongs = 8 + random.nextInt(8); // 8-15 songs per album
                for (int trackNum = 1; trackNum <= numSongs; trackNum++) {
                    String songTitle = SONG_WORDS[random.nextInt(SONG_WORDS.length)] + " " +
                                    SONG_WORDS[random.nextInt(SONG_WORDS.length)];
                    pstmt.setString(1, songTitle);
                    pstmt.setInt(2, albumId);
                    pstmt.setInt(3, trackNum);
                    pstmt.setInt(4, 120 + random.nextInt(361)); // Duration between 2-8 minutes in seconds
                    pstmt.setInt(5, genreIds.get(random.nextInt(genreIds.size())));
                    pstmt.executeUpdate();
                }
            }
        }
    }

    private static int getGeneratedId(PreparedStatement pstmt) throws SQLException {
        try (var rs = pstmt.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Failed to get generated ID");
        }
    }
} 