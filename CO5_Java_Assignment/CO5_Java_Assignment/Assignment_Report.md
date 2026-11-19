# PROGRAMMING IN JAVA – ASSIGNMENT 5 (CO5)
## DATA PERSISTENCE ENGINEERING – FILES AND DATABASES

**Name:** ____________________  
**Register Number:** ____________________  
**Java Version:** ____________________  
**GitHub Repository:** ____________________

---

# PART A – PERSISTENCE DESIGN & PSEUDOCODE

## A1 – Design a File Format

### A1.1 Record layouts

**Delimited text record (one pass per line):**

`passId,regNo,name,routeNo,boardingStop,validityDate,amountPaid`

Example:
`P1001,192311001,Arun,12,Central Stop,2026-12-31,4500.00`

Use UTF-8 text and a delimiter-safe encoding. A robust CSV writer must quote a field containing comma or quotation marks and double embedded quotes. Do not split a CSV row using a plain comma if quoted commas are allowed. The simple five-field implementation in Task B1 intentionally follows the supplied assignment TODO format; the full record design above includes all seven required fields.

**Binary DataOutputStream record:**
- `writeUTF(passId)`
- `writeUTF(regNo)`
- `writeUTF(name)`
- `writeInt(routeNo)`
- `writeUTF(boardingStop)`
- `writeUTF(validityDate)` (ISO `YYYY-MM-DD`)
- `writeDouble(amountPaid)`

`writeUTF` stores a length prefix and modified UTF-8 bytes; integer and double values have fixed-width binary representations. A binary file must be read in exactly the same field order and types.

### A1.2 Pseudocode – text-file operations

**ADD PASS**
```
BEGIN
  VALIDATE that passId is not blank
  VALIDATE that routeNo is positive and amountPaid is non-negative
  OPEN file using FileWriter in append mode
  WRAP FileWriter with BufferedWriter
  ENCODE every field using CSV escaping
  WRITE encoded record
  WRITE newline
  FLUSH and CLOSE writer
END
```

**FIND PASS BY ID**
```
BEGIN
  IF file does not exist THEN
      REPORT "No pass records found"
      RETURN NOT_FOUND
  ENDIF
  OPEN FileReader
  WRAP with BufferedReader
  WHILE a line is available
      PARSE line using CSV-aware parser
      IF field count or field types are invalid THEN
          RECORD damaged line and CONTINUE
      ENDIF
      IF parsed passId equals requested passId THEN
          RETURN parsed pass
      ENDIF
  ENDWHILE
  RETURN NOT_FOUND
  CLOSE reader
END
```

**LIST PASSES BY ROUTE**
```
BEGIN
  OPEN FileReader wrapped in BufferedReader
  WHILE a line is available
      PARSE and VALIDATE the record
      IF record is valid AND routeNo equals requestedRoute THEN
          DISPLAY record
      ENDIF
  ENDWHILE
  CLOSE reader
END
```

`FileReader`/`FileWriter` provide character access; `BufferedReader`/`BufferedWriter` reduce repeated low-level reads/writes and provide line operations. For raw binary records use `FileInputStream`/`FileOutputStream` wrapped in buffered streams and `DataInputStream`/`DataOutputStream`.

### A1.3 Damaged data
- **Damaged line:** validate the field count, numeric fields, date format and required fields. Log line number and reason, skip the record and continue; do not silently treat it as valid.
- **Missing file:** distinguish a missing file from an empty file. For a first run, create it when adding; for find/list, return a clear “file not found/no records” result.
- **Incomplete write/power failure:** write a complete new file to a temporary file, flush/close it, then atomically replace the original where supported. For append-based systems, include a record terminator/checksum and ignore a final incomplete record during recovery; keep backups.

### Alternative: RandomAccessFile
With fixed-size records, record `i` begins at `i × recordSize`; `seek()` can reach that record in O(1). However, finding by pass ID still needs O(n) scanning unless a separate index maps pass ID to record number. Fixed-size name/stop fields waste space when actual strings are short and require a maximum length/truncation policy. Text scan is O(n) and more portable/readable.

## A2 – Don’t Read What You Don’t Need

### A2.1 Predict the cost
A 50 MB file contains approximately 50 × 1024 × 1024 = 52,428,800 bytes (if MB is interpreted as MiB). A one-byte-at-a-time loop performs about 52.4 million `read()` calls. The repeated method-call and I/O overhead makes this slower than reading blocks.

### A2.2 Buffered redesign
Use `BufferedInputStream` or a byte array buffer (for example 8 KiB or 64 KiB). Read a block, scan the bytes in memory and increment the line count when the byte is `\n`. The number of bulk reads is approximately fileSize / bufferSize, rather than one read per byte. For text with platform-independent character handling, use `BufferedReader.readLine()`.

### A2.3 When buffering gives little benefit
If the data is already in memory, the source is a very small file, or the underlying API already returns large blocks, another buffer may add copying without meaningful I/O savings.

**Alternative – Files.readAllLines():** time is O(file size), but memory is also O(file size) plus per-line object overhead. For a 50 MB file, the in-memory strings and list can consume substantially more than 50 MB. A buffered streaming reader uses O(buffer size) additional memory and is preferred for large logs; readAllLines is convenient for small files that comfortably fit in memory.

## A3 – Discover the Data-Access Design Pattern

### A3.1 Repeated and varying steps
Repeated: obtain connection, create statement, bind parameters, execute SQL, map ResultSet rows to objects, handle SQL exceptions and close JDBC resources. Varying: table name, columns, key type, SQL statements and row-to-object mapping.

### A3.2 Generic DAO
```
INTERFACE Dao<T, K>
  insert(T entity)
  findById(K id) RETURNS Optional<T>
  findAll() RETURNS List<T>
  update(T entity)
  delete(K id)
END INTERFACE
```
`StudentDao implements Dao<Student, Integer>` and owns student-specific SQL and ResultSet mapping. A generic interface defines the contract; a reusable abstract JDBC helper can centralize connection/resource handling.

### A3.3 Student object-to-table mapping

| Java field | SQL column | Example type | Role |
|---|---|---|---|
| id | student_id | INT | Primary key |
| name | student_name | VARCHAR(100) | Student name |
| email | email | VARCHAR(150) | Unique email |
| department | department | VARCHAR(80) | Department |

If SQL is embedded in GUI/service classes, a column rename may require edits in many locations. DAO centralizes persistence code. Both designs have broadly the same database-operation time complexity and similar row/result memory; DAO improves maintainability, not database query speed.

## A4 – What Should Be Serialized?

### A4.1 Persisted fields
Mark the live `Connection` transient because it is an open external resource and is not a serializable snapshot. Mark password transient as well; credentials should not be stored in a serialized object graph. Persist the Hostel → Room list → Resident list data needed to reconstruct the domain state. After deserialization, transient fields receive default values (null for object references), so reconnect/reinitialize services explicitly.

Serialization traverses reachable non-transient serializable objects. Every object in the reachable graph must implement `Serializable`, or serialization fails with `NotSerializableException`.

### A4.2 Schema change and serialVersionUID
When a new phone field is added, an old stream can generally be read if the class remains serialization-compatible and the `serialVersionUID` matches. The new field receives its default value (null for String) if it was absent in the old stream. An incompatible UID causes `InvalidClassException`. Declare an explicit stable UID and implement validation/migration for newly introduced fields.

**Alternative CSV/JSON:** text formats are inspectable and easier to migrate across languages; serialization is convenient for Java object graphs but is Java-specific and tightly coupled to class structure. Save/load is O(n) in residents for either approach. Binary serialization can be compact and direct; CSV/JSON has textual overhead but handles added optional fields more transparently when parsers use defaults. Actual size/time depends on fields, encoding and library.

## A5 – Design for Correctness and Safety

### A5.1 JDBC transaction pseudocode
```
BEGIN
  GET connection
  SET autoCommit = FALSE
  TRY
      UPDATE hostel_wallet SET balance = balance - 500 WHERE wallet_id = source
      VERIFY exactly one row updated and source has sufficient balance
      UPDATE canteen_wallet SET balance = balance + 500 WHERE wallet_id = destination
      VERIFY exactly one row updated
      COMMIT
  CATCH any exception
      ROLLBACK
      REPORT transfer failed
  FINALLY
      RESTORE autoCommit if connection is reused
END
```
Both updates commit together or neither does. Add database constraints and row locking/conditional updates to protect against concurrent transfers.

### A5.2 SQL injection
String concatenation allows user input to become part of SQL syntax. For example, a crafted ID such as `' OR '1'='1' -- ` may alter the WHERE condition in a vulnerable query. Use:
```
String sql = "SELECT * FROM users WHERE id=? AND pwd=?";
PreparedStatement ps = con.prepareStatement(sql);
ps.setString(1, id);
ps.setString(2, pwd);
ResultSet rs = ps.executeQuery();
```
Passwords should be stored as salted password hashes and verified with a password-hashing function; do not store plaintext passwords.

**Alternative: auto-commit statements:** if the debit succeeds and the program fails before crediting the destination, money is lost. If the credit executes without a valid debit due to faulty ordering/retry, money can be created. A transaction adds coordination/logging overhead, but protects the all-or-nothing invariant; correctness is more important than the small extra cost.

---

# PART B – IMPLEMENTATION & EXPERIMENTAL INVESTIGATION

## B1 – Instrumented File Store

The Java implementation is in `TaskB1/PassStore.java`. It completes the supplied TODO by reading line-by-line with `BufferedReader`, skipping malformed records and returning the matching five fields. The sample input includes a damaged P1002 line; that line is skipped because it does not contain exactly five fields, so the search continues and returns P1003.

### Required validation table

| Test | Input/action | Expected behaviour |
|---|---|---|
| Normal add/find | Add P1003 then find P1003 | Returns its five fields |
| Missing ID | Find P9999 | Returns null |
| Damaged line | Add a row with wrong field count | Skips damaged row and continues |
| Invalid amount | Non-numeric amount | Rejects/flags record in validation stage |

### Measurement record – fill with actual runs

| File size | Unbuffered time (ms) | Buffered time (ms) |
|---:|---:|---:|
| 1 MB | ______ | ______ |
| 10 MB | ______ | ______ |
| 50 MB | ______ | ______ |

| Pass records | Find target time (ms) |
|---:|---:|
| 1,000 | ______ |
| 10,000 | ______ |
| 50,000 | ______ |
| 100,000 | ______ |

A sequential lookup is O(n). Loading the file once into a `HashMap<String,String[]>` costs O(n) time and O(n) extra memory; q average-time lookups then cost O(q), giving O(n + q) overall rather than O(q × n). This is useful when many searches justify the memory cost.

## B2 – Engineer an Adversarial Input

Use only the local test database. The code in `TaskB2/LoginInjectionDemo.java` runs the same login check using concatenated SQL and PreparedStatement. A classic test payload is:
- ID: `' OR '1'='1' -- `
- Password: any text

The unsafe query may be altered into a condition that is always true (exact behaviour depends on SQL dialect/comment parsing). The parameterized query treats the payload as a literal value and should reject it.

Malformed pass-file cases to test: wrong field count, non-numeric amount and empty line. The B1 store skips records that do not match its exact five-field structure; numeric validation is demonstrated in the B2 validator. Escaping quotes or regex validation is weaker than parameter binding because it is easy to miss SQL syntax/encoding edge cases. PreparedStatement separates SQL structure from data.

## B3 – Statement vs PreparedStatement vs Batch

The completed `TaskB3/PassLoader.java` binds all five values, adds rows to batches of 1,000, executes each batch, commits once, rolls back on SQL/runtime failure and restores the original auto-commit setting.

### Required experiment table – enter actual measurements

| Rows | Statement (ms) | PreparedStatement (ms) | PreparedStatement batch (ms) |
|---:|---:|---:|---:|
| 1,000 | ______ | ______ | ______ |
| 10,000 | ______ | ______ | ______ |
| 100,000 | ______ | ______ | ______ |

Repeat batch test with auto-commit on and one transaction. Force an invalid amount halfway through and verify the transaction leaves no inserted rows. A batch size b bounds queued parameter sets to O(b); total work remains O(n), while fewer network round trips can improve throughput. A single multi-row INSERT can reduce round trips but must respect SQL/packet limits. `LOAD DATA` is often efficient for bulk imports but requires careful file permissions, validation and database configuration. Use a transaction for an all-or-nothing import.

## B4 – Final Engineering Decision

`TaskB4/PassMigration.java` reads the legacy file, validates each record, batches valid records into the database and writes rejected records to a failure report. It also prints database product/driver information and tables using `DatabaseMetaData`.

For 12 campuses, 100× data and concurrent counters, a shared relational database is the strongest choice: centralized constraints, transactions, indexes, concurrent access and SQL search. Text files are suitable for interchange/import and serialization for Java-specific snapshots, but neither is a good shared multi-writer system at this scale. A separate serialized file per campus would require cross-campus aggregation for global searches and careful locking/versioning for simultaneous writes.

---

## Final checklist
- [ ] Execute every Java program and record actual results.
- [ ] Capture genuine screenshots for all required tests.
- [ ] Complete measurement tables using your own machine/database.
- [ ] Add SQL script and test data to GitHub.
- [ ] Add repository link and student details.
- [ ] Commit each task progressively.
