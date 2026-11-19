import java.sql.*;

public class TransactionAndLogin {
    public static void transfer(Connection con, int fromId, int toId, double amount) throws SQLException {
        boolean old = con.getAutoCommit();
        con.setAutoCommit(false);
        try {
            try (PreparedStatement debit = con.prepareStatement(
                    "UPDATE wallet SET balance=balance-? WHERE wallet_id=? AND balance>=?");
                 PreparedStatement credit = con.prepareStatement(
                    "UPDATE wallet SET balance=balance+? WHERE wallet_id=?")) {
                debit.setDouble(1, amount); debit.setInt(2, fromId); debit.setDouble(3, amount);
                if (debit.executeUpdate() != 1) throw new SQLException("Insufficient funds or source wallet missing");
                credit.setDouble(1, amount); credit.setInt(2, toId);
                if (credit.executeUpdate() != 1) throw new SQLException("Destination wallet missing");
            }
            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(old);
        }
    }

    public static boolean safeLogin(Connection con, String id, String passwordHash) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE id=? AND password_hash=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id); ps.setString(2, passwordHash);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }
}
