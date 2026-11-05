# CO1 Java Assignment 1 – Campus Smart-Parking System

**Student Name:** [Enter your name]  
**Register Number:** [Enter your register number]  
**Java Version:** OpenJDK 11  
**Required repository name:** `CSA09-<RegisterNumber>-Assignment1`

See [Assignment_Report.md](Assignment_Report.md) for the answers, design comparisons, pseudocode, UML diagram, and test evidence for Tasks 1–12.

## Compile and run

Open PowerShell in this project folder. Compile and run each program with the commands below. Run the `javac` and `java` lines separately; do not type any HTML such as `<br>` between them.

### Task 3 — jagged-array allocation

```powershell
javac Task03\ParkingFacility.java
java -cp Task03 ParkingFacility
```

**Result:** Allocates and releases slots across unequal floors; prints `null` when the facility is full.

### Task 8 — vehicle hierarchy and polymorphism

```powershell
javac Task08\FeeDemo.java
java -cp Task08 FeeDemo
```

**Result:** Calculates vehicle and EV charging fees using overridden methods and an interface.

### Task 11 — parking-system implementation and tests

```powershell
javac Task11\SmartParkingSystem.java
java -cp Task11 SmartParkingSystem
```

**Result:** Demonstrates tickets, entry-time capture, fees, monthly passes, slot release, and validation tests.

### Task 12 — design challenge

```powershell
javac Task12\DesignChallenge.java
java -cp Task12 DesignChallenge
```

**Result:** Demonstrates e-bike pricing, delivery-van time pricing, one-day visitor passes, and a five-floor jagged array.

## Task result notes

| Task | Result / evidence |
|---|---|
| 1 | Identifies system inputs, outputs, objects, business rules and assumptions; compares objects with parallel arrays. |
| 2 | Justifies data types, jagged-array choice, precision and input options; compares alternatives and complexity. |
| 3 | Provides allocation and fee pseudocode plus a runnable allocation/release example. |
| 4 | Explains classes, objects, constructors, references, heap/stack and alternatives. |
| 5 | Explains static ticket numbering, object references and garbage-collection eligibility. |
| 6 | Provides the vehicle hierarchy, UML diagram, `super` constructor trace and composition comparison. |
| 7 | Implements fee overrides and `equals()`, `hashCode()` and `toString()`; discusses `final`. |
| 8 | Demonstrates overload resolution, runtime dispatch, vehicle fees and EV charging. |
| 9 | Explains the abstract `Vehicle` class and `Chargeable` interface. |
| 10 | Proposes package organization and explains encapsulation, access and wrapper types. |
| 11 | Runs parking, fee-boundary, pass and invalid-input checks; compares active-ticket data structures. |
| 12 | Tests the new vehicle types, time pricing, one-day pass and unequal floor capacities. |

## Submission checklist

- Replace the student name and register number here and in the report.
- Create and share the required GitHub repository; add its URL to the report.
- Run the programs yourself and capture screenshots of the terminal output in the relevant task folders. No screenshots are included because they must be captured on your computer.
- Commit work progressively, with at least one commit per task, as required by the assignment.
- Keep each task folder's README and add any task-specific source, input, and screenshot files required for your submission.
- Check that the repository is accessible to your faculty.
