import java.sql.SQLException;
import java.util.List;

public class RegistrationService {
    private final RegistrationDAO dao = new RegistrationDAO();
    public void register(String regNo, String name, String event) throws SQLException {
        if (blank(regNo) || blank(name) || blank(event))
            throw new IllegalArgumentException("Register number, student name and event are required.");
        dao.insert(regNo.trim(), name.trim(), event.trim());
    }
    public List<Registration> findByEvent(String event, int limit, int offset) throws SQLException {
        return dao.findByEvent(event, limit, offset);
    }
    private boolean blank(String s) { return s == null || s.trim().isEmpty(); }
}
