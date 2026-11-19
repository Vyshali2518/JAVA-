import java.sql.*;
import java.util.*;

public class RegistrationDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/campus_events";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Change to your local MySQL password.

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void insert(String regNo, String name, String event) throws SQLException {
        String sql = "INSERT INTO registrations(register_no, student_name, event_name) VALUES(?,?,?)";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, regNo); ps.setString(2, name); ps.setString(3, event);
            ps.executeUpdate();
        }
    }

    public List<Registration> findByEvent(String event, int limit, int offset) throws SQLException {
        String sql = "SELECT id, register_no, student_name, event_name, registered_at " +
                     "FROM registrations WHERE (? = 'All' OR event_name = ?) " +
                     "ORDER BY id DESC LIMIT ? OFFSET ?";
        List<Registration> rows = new ArrayList<>();
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, event); ps.setString(2, event);
            ps.setInt(3, limit); ps.setInt(4, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rows.add(new Registration(rs.getInt("id"), rs.getString("register_no"),
                    rs.getString("student_name"), rs.getString("event_name"), rs.getString("registered_at")));
            }
        }
        return rows;
    }
}
