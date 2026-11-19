# Q3 – hashCode–equals Contract

Compile: `javac Attendance.java`  
Run: `java Attendance`

The Student class is immutable and defines equals/hashCode using the same `regNo` field. To test the faulty design, temporarily remove the hashCode override and compare repeated runs; the outcome can depend on hash bucket placement.
