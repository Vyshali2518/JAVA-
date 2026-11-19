# A2 – Buffered I/O
Compile: `javac LineCounter.java`  
Run: `java LineCounter attendance.log`

Counts LF newline bytes in a file. Run on 1 MB, 10 MB and 50 MB files and record the actual timings in the report. For Windows CRLF files, counting `\n` still counts one line ending per line. A final line without a newline is not counted by this byte-based definition.
