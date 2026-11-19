# CO5 – Data Persistence Engineering: Files and Databases

**Course:** Programming in Java (CSA09)  
**Assignment:** 5 – CO5 | **Total:** 50 marks | **Nature:** Individual

## Folder structure
- `Assignment_Report.md` – Part A (A1–A5) design, pseudocode, analysis and Part B interpretation guide.
- `TaskA1` – bus-pass record formats, file operations and damage handling.
- `TaskA2` – unbuffered and buffered line counter.
- `TaskA3` – generic DAO pattern and Student DAO example.
- `TaskA4` – serialization object graph and transient fields.
- `TaskA5` – JDBC transaction and safe login example.
- `TaskB1` – text pass store, completed TODO, damaged-line handling and benchmark tools.
- `TaskB2` – unsafe versus PreparedStatement login demonstration and malformed file cases.
- `TaskB3` – completed batch loader (TODO 1–3), SQL schema and insert benchmark scaffold.
- `TaskB4` – file-to-database migration and DatabaseMetaData inspection.
- `sql/` – MySQL schema and test data.

## Requirements
- JDK 8 or later. Some convenience methods in the supplied `PassStore` use Java 11; use JDK 11+ for the complete project.
- MySQL Server and MySQL Connector/J for JDBC tasks.
- Create the database using `sql/co5_schema.sql`.
- Configure connection details using environment variables `CO5_DB_URL`, `CO5_DB_USER`, `CO5_DB_PASSWORD`; defaults are documented in each JDBC class.

## Compile and run
For file-only tasks, open the relevant folder:
```bash
javac *.java
java ClassName
```
For JDBC tasks, include MySQL Connector/J in the classpath. Windows:
```bat
javac -cp ".;mysql-connector-j-8.x.x.jar" *.java
java -cp ".;mysql-connector-j-8.x.x.jar" ClassName
```
Linux/macOS: replace `;` with `:`.

## Important evidence checklist
This ZIP contains source code, report answers, test inputs and sample/expected outputs where appropriate. It does **not** contain fabricated screenshots or claimed benchmark results.
1. Run each program on your machine and add genuine screenshots in the matching task folder.
2. Measure B1 line counting for 1 MB, 10 MB and 50 MB; record your actual times.
3. Measure pass lookup for 1,000 through 100,000 records.
4. Run B3 at 1,000, 10,000 and 100,000 rows and record actual times for Statement, PreparedStatement and batch.
5. Run the forced transaction-failure test and capture table contents before/after.
6. Run B2 adversarial inputs against your local test database only.
7. Create GitHub repository `CSA09-<RegisterNumber>-Assignment5`, commit progressively, and add the repository URL to the report.
8. Fill in your name, register number, Java version and actual measurement tables in the report.

Do not use real student credentials or production data for the injection demonstrations.
