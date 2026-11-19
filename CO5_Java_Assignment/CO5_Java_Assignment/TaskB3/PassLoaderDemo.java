import java.sql.*;
import java.util.*;

public class PassLoaderDemo {
    public static void main(String[] args) throws Exception {
        String url = System.getenv().getOrDefault("CO5_DB_URL", "jdbc:mysql://localhost:3306/co5_assignment");
        String user = System.getenv().getOrDefault("CO5_DB_USER", "root");
        String pass = System.getenv().getOrDefault("CO5_DB_PASSWORD", "");
        List<String[]> rows = new ArrayList<>();
        for (int i = 1; i <= 2500; i++)
            rows.add(new String[]{"P" + i, "REG" + i, "Student" + i, String.valueOf(i % 20 + 1), "4500.0"});
        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            System.out.println("Rows loaded: " + PassLoader.loadAll(con, rows));
        }
    }
}
