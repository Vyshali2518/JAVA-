import java.io.Serializable;
import java.sql.Connection;

public class Resident implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String name, registerNumber;
    private final String phoneNumber;
    private transient String password;
    private transient Connection connection;
    public Resident(String name, String registerNumber, String phoneNumber, String password) {
        this.name = name; this.registerNumber = registerNumber;
        this.phoneNumber = phoneNumber; this.password = password;
    }
    public String getName() { return name; }
    public String getRegisterNumber() { return registerNumber; }
    public String getPhoneNumber() { return phoneNumber; }
    public void attachConnection(Connection c) { connection = c; }
}
