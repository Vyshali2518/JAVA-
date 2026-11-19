import java.io.*;

public class SerializationDemo {
    public static void main(String[] args) throws Exception {
        Hostel hostel = new Hostel();
        Room room = new Room(101);
        room.addResident(new Resident("Arun", "192311001", "9000000000", "not-saved"));
        hostel.addRoom(room);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hostel.ser"))) {
            out.writeObject(hostel);
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("hostel.ser"))) {
            Hostel restored = (Hostel) in.readObject();
            System.out.println("Hostel object restored. Rooms: " + restored.getRooms().size());
            System.out.println("Transient password and connection are not restored.");
        }
    }
}
