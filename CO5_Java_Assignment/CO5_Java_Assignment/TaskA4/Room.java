import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int roomNumber;
    private List<Resident> residents = new ArrayList<>();
    public Room(int roomNumber) { this.roomNumber = roomNumber; }
    public void addResident(Resident r) { residents.add(r); }
    public List<Resident> getResidents() { return residents; }
}
