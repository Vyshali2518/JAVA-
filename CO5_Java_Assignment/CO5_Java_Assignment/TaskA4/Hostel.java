import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Hostel implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Room> rooms = new ArrayList<>();
    public List<Room> getRooms() { return rooms; }
    public void addRoom(Room room) { rooms.add(room); }
}
