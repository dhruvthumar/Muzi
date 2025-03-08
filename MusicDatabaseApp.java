import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Priority;

public class MusicDatabaseApp extends Application {
    private static final String URL = "jdbc:mysql://localhost:3306/muzi";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "D#ruv@1702"; // Use your actual password

    private Connection connection;
    private TextArea resultArea;
    private ComboBox<String> queryTypeComboBox;
    private TextField searchField;

    @Override
    public void start(Stage primaryStage) {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
            VBox root = new VBox(10);
            root.setPadding(new Insets(10));

            // Query type selection
            queryTypeComboBox = new ComboBox<>();
            queryTypeComboBox.setItems(FXCollections.observableArrayList(
                "Search Artists",
                "Search Albums",
                "Search Songs",
                "Artists by Genre",
                "Albums by Year",
                "Top Artists by Album Count"
            ));
            queryTypeComboBox.setValue("Search Artists");

            // Search field
            searchField = new TextField();
            searchField.setPromptText("Enter search term...");
            
            // Search button
            Button searchButton = new Button("Search");
            searchButton.setOnAction(e -> performSearch());

            // Results area
            resultArea = new TextArea();
            resultArea.setEditable(false);
            resultArea.setPrefRowCount(20);
            
            // Layout
            HBox searchBox = new HBox(10);
            searchBox.getChildren().addAll(queryTypeComboBox, searchField, searchButton);
            
            root.getChildren().addAll(searchBox, resultArea);
            VBox.setVgrow(resultArea, Priority.ALWAYS);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Music Database Explorer");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (SQLException e) {
            showError("Database connection error: " + e.getMessage());
        }
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        String queryType = queryTypeComboBox.getValue();
        
        try {
            String query = switch (queryType) {
                case "Search Artists" -> 
                    "SELECT name, country, formed_year, bio FROM artists " +
                    "WHERE name LIKE ? OR country LIKE ? OR bio LIKE ?";
                case "Search Albums" ->
                    "SELECT a.title, art.name as artist, a.release_year, g.name as genre " +
                    "FROM albums a " +
                    "JOIN artists art ON a.artist_id = art.artist_id " +
                    "JOIN genres g ON a.genre_id = g.genre_id " +
                    "WHERE a.title LIKE ? OR art.name LIKE ?";
                case "Search Songs" ->
                    "SELECT s.title, a.title as album, art.name as artist, s.duration " +
                    "FROM songs s " +
                    "JOIN albums a ON s.album_id = a.album_id " +
                    "JOIN artists art ON a.artist_id = art.artist_id " +
                    "WHERE s.title LIKE ? OR a.title LIKE ? OR art.name LIKE ?";
                case "Artists by Genre" ->
                    "SELECT DISTINCT art.name, art.country, g.name as genre " +
                    "FROM artists art " +
                    "JOIN albums a ON art.artist_id = a.artist_id " +
                    "JOIN genres g ON a.genre_id = g.genre_id " +
                    "WHERE g.name LIKE ?";
                case "Albums by Year" ->
                    "SELECT a.title, art.name as artist, a.release_year " +
                    "FROM albums a " +
                    "JOIN artists art ON a.artist_id = art.artist_id " +
                    "WHERE a.release_year = ?";
                case "Top Artists by Album Count" ->
                    "SELECT art.name, COUNT(a.album_id) as album_count " +
                    "FROM artists art " +
                    "LEFT JOIN albums a ON art.artist_id = a.artist_id " +
                    "GROUP BY art.artist_id, art.name " +
                    "ORDER BY album_count DESC " +
                    "LIMIT 10";
                default -> throw new IllegalStateException("Unknown query type: " + queryType);
            };

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                // Set parameters based on query type
                switch (queryType) {
                    case "Search Artists" -> {
                        String param = "%" + searchTerm + "%";
                        pstmt.setString(1, param);
                        pstmt.setString(2, param);
                        pstmt.setString(3, param);
                    }
                    case "Search Albums" -> {
                        String param = "%" + searchTerm + "%";
                        pstmt.setString(1, param);
                        pstmt.setString(2, param);
                    }
                    case "Search Songs" -> {
                        String param = "%" + searchTerm + "%";
                        pstmt.setString(1, param);
                        pstmt.setString(2, param);
                        pstmt.setString(3, param);
                    }
                    case "Artists by Genre" -> 
                        pstmt.setString(1, "%" + searchTerm + "%");
                    case "Albums by Year" -> {
                        try {
                            pstmt.setInt(1, Integer.parseInt(searchTerm));
                        } catch (NumberFormatException e) {
                            showError("Please enter a valid year");
                            return;
                        }
                    }
                }

                StringBuilder result = new StringBuilder();
                try (ResultSet rs = pstmt.executeQuery()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    
                    // Add header
                    for (int i = 1; i <= columnCount; i++) {
                        result.append(String.format("%-25s", metaData.getColumnLabel(i)));
                    }
                    result.append("\n");
                    result.append("-".repeat(columnCount * 25)).append("\n");
                    
                    // Add data
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            String value = rs.getString(i);
                            if (value == null) value = "N/A";
                            if (metaData.getColumnName(i).equals("duration")) {
                                int duration = rs.getInt(i);
                                value = String.format("%d:%02d", duration / 60, duration % 60);
                            }
                            result.append(String.format("%-25s", value));
                        }
                        result.append("\n");
                    }
                }
                
                resultArea.setText(result.toString());
            }
        } catch (SQLException e) {
            showError("Error executing query: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 