import java.sql.*;

public class LoginInjectionDemo {
    private static Connection connection() throws SQLException {
        String url = System.getenv().getOrDefault("CO5_DB_URL", "jdbc:mysql://localhost:3306/co5_assignment");
        String user = System.getenv().getOrDefault("CO5_DB_USER", "root");
        String pass = System.getenv().getOrDefault("CO5_DB_PASSWORD", "");
        return DriverManager.getConnection(url, user, pass);
    }

    static boolean unsafeLogin(Connection con, String id, String pwd) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE id='" + id + "' AND pwd='" + pwd + "'";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return rs.next();
        }
    }

    static boolean safeLogin(Connection con, String id, String pwd) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE id=? AND pwd=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id); ps.setString(2, pwd);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    static boolean validPassRow(String line) {
        String[] f = line.split(",", -1);
        if (f.length != 5 || f[0].isEmpty() || f[1].isEmpty() || f[2].isEmpty()) return false;
        try { Integer.parseInt(f[3]); Double.parseDouble(f[4]); return true; }
        catch (NumberFormatException ex) { return false; }
    }

    public static void main(String[] args) {
        try (Connection con = connection()) {
            String payload = "' OR '1'='1' -- ";
            System.out.println("Unsafe login accepted: " + unsafeLogin(con, payload, "wrong"));
            System.out.println("Prepared login accepted: " + safeLogin(con, payload, "wrong"));
        } catch (SQLException ex) {
            System.err.println("Database error: " + ex.getMessage());
        }
        String[] damaged = {"P1,R1,Name,2,100.0", "P2,R2,Name,2,not-money", "", "P3,R3,Name"};
        for (String row : damaged)
            System.out.println("Valid row? " + validPassRow(row) + " | " + row);
    }
}
