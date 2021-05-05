package database;

import entities.Post;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PostgreSQLManager {
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Connection openConnection() {
        String driver = "org.postgresql.Driver";
        String connectionString = "jdbc:postgresql://localhost:5432/service";

        String connectionUsername = "postgres";
        String connectionPassword = "1234";
        try {
            Class.forName(driver);
            return DriverManager.getConnection(
                    connectionString,
                    connectionUsername,
                    connectionPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertIntoPostsCertainDate(String name, String text, String date) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = openConnection();
            conn.setAutoCommit(false);

            stmt = conn.createStatement();

            String sql = "INSERT INTO Posts(publish_date, " + "username, post_text) "
                    + "VALUES ('" + date + "', '" + name + "', '" + text + "');";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }

    public List<Post> selectPosts(int pageNumber,
                                  int postsPerPage)
            throws SQLException {
        Connection conn = null;
        try {
            conn = openConnection();
            conn.setAutoCommit(false);
            List<Post> notes = new ArrayList<Post>();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * " + "FROM posts "
                    + "ORDER BY post_id DESC LIMIT " + postsPerPage + " OFFSET "
                    + (pageNumber - 1) * postsPerPage + ";");
            while (rs.next()) {
                Post note = new Post();
                note.setDate(rs.getString("publish_date"));
                note.setUsername(rs.getString("username"));
                note.setText(rs.getString("post_text"));
                notes.add(note);
            }
            rs.close();
            stmt.close();
            return notes;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
        return null;
    }

    public int countPosts() throws SQLException {
        int postNumber = 0;
        Connection conn = null;
        try {
            conn = openConnection();
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*)"
                    + " AS number FROM posts;");
            postNumber = rs.next() ? rs.getInt("number") : 0;
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
        return postNumber;
    }

    public void insertIntoPosts(String name, String text) throws SQLException {
        String date = dateFormat.format(new Date());
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = openConnection();
            conn.setAutoCommit(false);

            stmt = conn.createStatement();

            String sql = "INSERT INTO Posts(publish_date, " + "username, post_text) "
                    + "VALUES ('" + date + "', '" + name + "', '" + text + "');";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }
}
