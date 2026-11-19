# CO4 – Event-Driven GUI Design (Java)

**Course:** Programming in Java (CSA09)  
**Assignment:** 4 – CO4  
**Coverage:** AWT/Swing, event delegation, listeners, adapters, inner/anonymous classes, lambdas, layout managers, dialogs and Swing–JDBC.

## Contents
- `Assignment_Report.md` – answers, event tables, layouts, pseudocode, comparisons and design notes for Tasks 1–6.
- `Task01` – repaired fee-payment design and service separation.
- `Task02` – appointment desk event model and handler pseudocode.
- `Task03` – resizable library search screen.
- `Task04` – persistent drawing pad and completed TODO program.
- `Task05` – four converter event-handling styles and completed lambda program.
- `Task06` – layered campus event registration application and MySQL script.

## Requirements
- JDK 8 or later (Task 6 uses SwingWorker and JDBC).
- MySQL Server for Task 6.
- MySQL Connector/J JAR for running Task 6.

## Compile and run
From a task folder:
```bash
javac FileName.java
java ClassName
```
For Task 6, add the MySQL Connector/J JAR to the classpath. Example on Windows:
```bat
javac -cp ".;mysql-connector-j-8.x.x.jar" *.java
java -cp ".;mysql-connector-j-8.x.x.jar" EventRegistrationApp
```
On Linux/macOS replace `;` with `:`.

## Important submission checklist
1. Replace the student name, register number and GitHub link placeholders in your report.
2. Execute every task on your computer and capture genuine screenshots of the required interactions.
3. For Task 3, capture three window sizes. For Task 4, capture before and after minimizing/restoring. For Task 5, capture valid and invalid conversions. For Task 6, capture normal, invalid and duplicate registration tests.
4. Add your screenshots to the corresponding task folders. Screenshots are not fabricated or included in this ZIP.
5. Create the required GitHub repository `CSA09-<RegisterNumber>-Assignment4` and commit progressively, at least one commit per task.
