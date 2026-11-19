import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class PassMigration {
    private static Connection connection() throws SQLException {
        String url = System.getenv().getOrDefault("CO5_DB_URL", "jdbc:mysql://localhost:3306/co5_assignment");
        String user = System.getenv().getOrDefault("CO5_DB_USER", "root");
        String pass = System.getenv().getOrDefault("CO5_DB_PASSWORD", "");
        return DriverManager.getConnection(url, user, pass);
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java PassMigration <pass-file.csv>");
            return;
        }
        Path source = Paths.get(args[0]);
        List<String[]> valid = new ArrayList<>();
        List<String> rejected = new ArrayList<>();
        int lineNo = 0;
        try (BufferedReader r = Files.newBufferedReader(source)) {
            String line;
            while ((line = r.readLine()) != null) {
                lineNo++;
                String[] f = line.split(",", -1);
                try {
                    if (f.length != 5) throw new IllegalArgumentException("expected 5 fields");
                    Integer.parseInt(f[3]);
                    Double.parseDouble(f[4]);
                    valid.add(f);
                } catch (RuntimeException ex) {
                    rejected.add("Line " + lineNo + ": " + ex.getMessage() + " | " + line);
                }
            }
        }
        try (Connection con = connection()) {
            int loaded = PassLoader.loadAll(con, valid);
            System.out.println("Valid rows loaded: " + loaded);
            DatabaseMetaData md = con.getMetaData();
            System.out.println("Database: " + md.getDatabaseProductName() + " " + md.getDatabaseProductVersion());
            System.out.println("Driver: " + md.getDriverName() + " " + md.getDriverVersion());
            try (ResultSet tables = md.getTables(con.getCatalog(), null, "%", new String[]{"TABLE"})) {
                System.out.println("Tables:");
                while (tables.next()) System.out.println(" - " + tables.getString("TABLE_NAME"));
            }
        }
        Path rejectedFile = Paths.get("migration_rejected.txt");
        Files.write(rejectedFile, rejected);
        System.out.println("Rejected rows: " + rejected.size() + " (see " + rejectedFile + ")");
    }
}
