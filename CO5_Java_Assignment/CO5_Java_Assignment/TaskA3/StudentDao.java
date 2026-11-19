import java.sql.*;
import java.util.*;

public class StudentDao implements Dao<Student, Integer> {
    private final Connection connection;
    public StudentDao(Connection connection) { this.connection = connection; }

    private Student map(ResultSet rs) throws SQLException {
        return new Student(rs.getInt("student_id"), rs.getString("student_name"),
                rs.getString("email"), rs.getString("department"));
    }
    public void insert(Student s) throws SQLException {
        String sql = "INSERT INTO student(student_id, student_name, email, department) VALUES(?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1,s.getId()); ps.setString(2,s.getName());
            ps.setString(3,s.getEmail()); ps.setString(4,s.getDepartment()); ps.executeUpdate();
        }
    }
    public Optional<Student> findById(Integer id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM student WHERE student_id=?")) {
            ps.setInt(1,id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? Optional.of(map(rs)) : Optional.empty(); }
        }
    }
    public List<Student> findAll() throws SQLException {
        List<Student> list = new ArrayList<>();
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM student")) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }
    public void update(Student s) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE student SET student_name=?, email=?, department=? WHERE student_id=?")) {
            ps.setString(1,s.getName()); ps.setString(2,s.getEmail());
            ps.setString(3,s.getDepartment()); ps.setInt(4,s.getId()); ps.executeUpdate();
        }
    }
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM student WHERE student_id=?")) {
            ps.setInt(1,id); ps.executeUpdate();
        }
    }
}
