# Task 6 – Campus Event Registration

## Setup
1. Install MySQL and run `schema.sql` in MySQL Workbench or phpMyAdmin.
2. Add MySQL Connector/J to the classpath.
3. Open `RegistrationDAO.java` and set the correct MySQL username/password.
4. Compile:

```bash
javac -cp ".;mysql-connector-j-8.x.x.jar" *.java
```

5. Run:

```bash
java -cp ".;mysql-connector-j-8.x.x.jar" EventRegistrationApp
```

On Linux/macOS, use `:` instead of `;` in the classpath. The sample uses `LIMIT`/`OFFSET` pagination and `SwingWorker` for JDBC work. Export saves the currently displayed page. Capture real screenshots for successful, invalid and duplicate registration, filtering and CSV export.
