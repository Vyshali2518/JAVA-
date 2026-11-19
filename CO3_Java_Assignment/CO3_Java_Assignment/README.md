# Programming in Java – Assignment 3 (CO3)

**Coverage:** Exception handling and multithreading (Sessions 27–38).  
**Mode:** Pair work – 2 students per group. **Total:** 50 marks.

Both students must execute every question in their own repository. Do not divide the assignment question-wise. Use the Student A / Student B responsibility notes in each question README, and prepare evidence for both roles.

## Folder structure
- `Q1` – ATM exception handling and failure paths
- `Q2` – User-defined exceptions, propagation and chaining
- `Q3` – Resource safety: manual close vs try-with-resources
- `Q4` – Thread creation and thread life cycle
- `Q5` – Race conditions and synchronization
- `Q6` – Deadlock prevention and producer–consumer communication
- `Q7` – Concurrent fault-tolerant result-file processor

## Compile and run
Open a terminal in the required question folder:
```bash
javac *.java
java MainClassName
```
Use JDK 11 or later (the examples use `Path.of()` and `Files.readString()` / `Files.writeString()`). Each folder contains its own README with the main class name.

## Submission checklist
1. Create `CSA09-<RegisterNumber>-Assignment3` for each student and share it with faculty.
2. Include name, register number, Java version, run steps and result notes.
3. Keep source code, test inputs, output screenshots and logs in each question folder.
4. Commit progressively (at least one commit per question).
5. Submit one joint report with Student A and Student B sections clearly labelled, plus both GitHub links and the individual contribution record.

**Important:** Timing, thread completion order and race-condition observations vary by machine and run. The supplied programs are executable starting points; collect your own repeated measurements and screenshots. Do not submit the included sample run output as experimental evidence.
