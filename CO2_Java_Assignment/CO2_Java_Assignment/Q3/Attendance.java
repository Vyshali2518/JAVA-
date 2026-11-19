import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

class Student {
    private final String regNo;
    private final String name;
    Student(String regNo, String name) { this.regNo = regNo; this.name = name; }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student other = (Student) o;
        return regNo.equals(other.regNo);
    }
    @Override public int hashCode() { return Objects.hash(regNo); }
    @Override public String toString() { return regNo + " " + name; }
}
public class Attendance {
    public static void main(String[] args) {
        Set<Student> present = new HashSet<>();
        present.add(new Student("192311001", "Arun"));
        present.add(new Student("192311002", "Divya"));
        present.add(new Student("192311001", "Arun"));
        System.out.println("Unique students: " + present.size());
        for (Student s : present) System.out.println(s);
    }
}
