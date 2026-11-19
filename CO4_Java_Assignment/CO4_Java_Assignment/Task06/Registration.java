public class Registration {
    private final int id;
    private final String registerNo, studentName, eventName, registeredAt;
    public Registration(int id, String registerNo, String studentName, String eventName, String registeredAt) {
        this.id = id; this.registerNo = registerNo; this.studentName = studentName;
        this.eventName = eventName; this.registeredAt = registeredAt;
    }
    public int getId() { return id; }
    public String getRegisterNo() { return registerNo; }
    public String getStudentName() { return studentName; }
    public String getEventName() { return eventName; }
    public String getRegisteredAt() { return registeredAt; }
}
