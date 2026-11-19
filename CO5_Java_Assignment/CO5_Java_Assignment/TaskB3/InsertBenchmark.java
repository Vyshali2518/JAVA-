import java.sql.*;
import java.util.*;

public class InsertBenchmark {
    private static Connection connect() throws SQLException {
        String url = System.getenv().getOrDefault("CO5_DB_URL", "jdbc:mysql://localhost:3306/co5_assignment");
        String user = System.getenv().getOrDefault("CO5_DB_USER", "root");
        String pass = System.getenv().getOrDefault("CO5_DB_PASSWORD", "");
        return DriverManager.getConnection(url, user, pass);
    }
    private static List<String[]> generate(int count) {
        List<String[]> rows = new ArrayList<>(count);
        for (int i = 1; i <= count; i++)
            rows.add(new String[]{"B" + i, "R" + i, "Student" + i, String.valueOf(i % 30 + 1), "2500.00"});
        return rows;
    }
    private static void clear(Connection c) throws SQLException {
        try (Statement s = c.createStatement()) { s.executeUpdate("DELETE FROM bus_pass"); }
    }
    private static long statement(Connection c, List<String[]> rows) throws SQLException {
        long start = System.nanoTime();
        try (Statement s = c.createStatement()) {
            for (String[] p : rows) {
                String sql = "INSERT INTO bus_pass(pass_id,reg_no,name,route_no,amount) VALUES ('"
                    + p[0] + "','" + p[1] + "','" + p[2] + "'," + p[3] + "," + p[4] + ")";
                s.executeUpdate(sql);
            }
        }
        return (System.nanoTime() - start) / 1_000_000;
    }
    private static long prepared(Connection c, List<String[]> rows) throws SQLException {
        long start = System.nanoTime();
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO bus_pass(pass_id,reg_no,name,route_no,amount) VALUES (?,?,?,?,?)")) {
            for (String[] p : rows) {
                ps.setString(1,p[0]); ps.setString(2,p[1]); ps.setString(3,p[2]);
                ps.setInt(4,Integer.parseInt(p[3])); ps.setDouble(5,Double.parseDouble(p[4]));
                ps.executeUpdate();
            }
        }
        return (System.nanoTime() - start) / 1_000_000;
    }
    public static void main(String[] args) throws Exception {
        int[] sizes = {1000, 10000, 100000};
        try (Connection c = connect()) {
            for (int n : sizes) {
                List<String[]> rows = generate(n);
                clear(c); long a = statement(c, rows);
                clear(c); long b = prepared(c, rows);
                clear(c); long start = System.nanoTime(); PassLoader.loadAll(c, rows);
                long batch = (System.nanoTime() - start) / 1_000_000;
                System.out.println(n + " rows | Statement=" + a + " ms | Prepared=" + b
                        + " ms | Batch=" + batch + " ms");
            }
        }
    }
}
