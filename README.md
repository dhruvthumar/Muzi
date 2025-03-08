# Music Database Management System

A Java application for managing a music database with a modern JavaFX user interface. This application allows you to store and query information about artists, albums, songs, and genres.

## Features

- **Database Management**
  - Store information about artists, albums, songs, and genres
  - Automatic random data generation for testing
  - Robust database schema with proper relationships

- **Search Capabilities**
  - Search artists by name, country, or bio
  - Search albums by title or artist
  - Search songs by title, album, or artist
  - Filter artists by genre
  - Filter albums by release year
  - View top artists by album count

- **User Interface**
  - Modern JavaFX interface
  - Easy-to-use search functionality
  - Detailed result display
  - Interactive data visualization

## Prerequisites

1. **Java Development Kit (JDK)**
   - Version 17 or higher recommended
   - Download from: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)

2. **MySQL**
   - Version 8.0 or higher
   - Download from: [MySQL Community Downloads](https://dev.mysql.com/downloads/)

3. **JavaFX SDK**
   - Version 17 or higher
   - Download from: [JavaFX Downloads](https://gluonhq.com/products/javafx/)

4. **MySQL Connector/J**
   - Already included in the project's lib directory
   - Version: 9.2.0

## Setup Instructions

1. **Database Setup**
   ```sql
   CREATE DATABASE muzi;
   CREATE USER 'muzi_user'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON muzi.* TO 'muzi_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

2. **Project Configuration**
   - Update database connection settings in:
     - `MusicDatabaseSchema.java`
     - `DataGenerator.java`
     - `MusicDatabaseApp.java`
   - Set the following variables:
     ```java
     private static final String URL = "jdbc:mysql://localhost:3306/muzi";
     private static final String USERNAME = "your_username";
     private static final String PASSWORD = "your_password";
     ```

3. **Compilation and Running**
   ```bash
   # Compile the project
   javac -cp ".;lib/*" *.java

   # Create database schema
   java -cp ".;lib/*" MusicDatabaseSchema

   # Generate sample data
   java -cp ".;lib/*" DataGenerator

   # Run the application
   java -cp ".;lib/*" MusicDatabaseApp
   ```

## Project Structure

```
music-database/
├── src/
│   ├── MusicDatabaseSchema.java   # Database schema creation
│   ├── DataGenerator.java         # Sample data generation
│   └── MusicDatabaseApp.java      # Main application
├── lib/
│   └── mysql-connector-j-9.2.0.jar # MySQL JDBC driver
└── README.md
```

## Database Schema

```sql
CREATE TABLE genres (
    genre_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE artists (
    artist_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(50),
    formed_year INT,
    bio TEXT
);

CREATE TABLE albums (
    album_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    artist_id INT,
    release_year INT,
    genre_id INT,
    FOREIGN KEY (artist_id) REFERENCES artists(artist_id),
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id)
);

CREATE TABLE songs (
    song_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    album_id INT,
    track_number INT,
    duration INT,
    genre_id INT,
    FOREIGN KEY (album_id) REFERENCES albums(album_id),
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id)
);
```

## Usage

1. **Search Artists**
   - Search by name, country, or biography
   - View detailed artist information

2. **Browse Albums**
   - Search by title or artist
   - Filter by release year or genre
   - View track listings

3. **Explore Songs**
   - Search by title
   - View song details including duration
   - See album and artist information

4. **Analytics**
   - View top artists by album count
   - Analyze genre distribution
   - Track yearly release trends

## Contributing

Feel free to submit issues and enhancement requests! 