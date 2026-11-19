# B3 – Statement vs PreparedStatement vs Batch
`PassLoader.java` completes TODO 1–3: binds five fields, executes every 1,000 rows, commits once, rolls back on failure and restores auto-commit.

The `InsertBenchmark.java` harness measures Statement, PreparedStatement and transactional batch insertion at 1,000, 10,000 and 100,000 generated rows. Run on a disposable local test database; the benchmark clears the `bus_pass` table before each strategy.

Compile with MySQL Connector/J:
```bat
javac -cp ".;mysql-connector-j-8.x.x.jar" *.java
java -cp ".;mysql-connector-j-8.x.x.jar" InsertBenchmark
```
For rollback evidence, modify a generated record to contain an invalid numeric route/amount halfway through, call `PassLoader.loadAll`, catch the error and verify that the table has no newly inserted rows.
