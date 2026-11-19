# B4 – Migration and Database Metadata
Compile `PassLoader.java` and `PassMigration.java` with MySQL Connector/J on the classpath. Run:
`java PassMigration legacy_passes.csv`

Input is the five-column CSV format used by the assignment implementation: pass_id,reg_no,name,route_no,amount. The migration writes invalid rows and reasons to `migration_rejected.txt`, imports valid rows in batches inside a transaction and prints database/driver/table metadata.
