package model;

import java.sql.*;
import java.util.ArrayList;

public class HighScoreManager {

    private final int maxScores;
    private final String dbURL = "jdbc:derby:yogiDB;create=true";
    private final String TABLE_NAME = "HIGHSCORES";

    public HighScoreManager(int maxScores) {
        this.maxScores = maxScores;
        
        try (Connection conn = DriverManager.getConnection(dbURL)) {
            if (!doesTableExist(conn)) {
                createTable(conn);
            }
        } catch (SQLException e) {
            System.err.println("Error upon init: " + e.getMessage());
        }
    }

    public void putHighScore(String name, int score) {
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (NAME, SCORE, TS) VALUES (?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            
            pstmt.setString(1, name);
            pstmt.setInt(2, score);
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Hiba a mentéskor: " + e.getMessage());
        }
    }

    public ArrayList<HighScore> getHighScores() {
        ArrayList<HighScore> highScores = new ArrayList<>();
        
        String query = "SELECT NAME, SCORE FROM " + TABLE_NAME + 
                       " ORDER BY SCORE DESC FETCH FIRST " + maxScores + " ROWS ONLY";

        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement stmt = conn.createStatement();
             ResultSet results = stmt.executeQuery(query)) {

            while (results.next()) {
                String name = results.getString("NAME");
                int score = results.getInt("SCORE");
                highScores.add(new HighScore(name, score));
            }
            
        } catch (SQLException e) {
            System.err.println("Error upon query: " + e.getMessage());
        }
        
        return highScores;
    }

    private boolean doesTableExist(Connection conn) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet res = meta.getTables(null, null, TABLE_NAME, new String[] {"TABLE"});
        return res.next();
    }

    private void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                     "ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                     "NAME VARCHAR(50), " +
                     "SCORE INT, " +
                     "TS TIMESTAMP)"; // Időbélyeg a biztonság kedvéért
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database table created.");
        }
    }
}
