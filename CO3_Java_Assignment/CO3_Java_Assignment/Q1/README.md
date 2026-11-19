# Q1 – Handling Failure in a Bank ATM Simulation

Compile: `javac ATM.java`  
Run: `java ATM`

The program exercises valid withdrawal, non-numeric amount, negative amount, insufficient balance and missing card-reader file paths. It demonstrates checked and unchecked exceptions, ordered catches, multi-catch and `finally`.

**Student A evidence:** draw the relevant `Throwable` hierarchy and make a failure-point/classification table.  
**Student B evidence:** add at least five test cases, record the catch path and finally execution, and capture one deliberately uncaught stack trace. Also demonstrate the compile-time error from placing a broad catch before a narrow catch.
