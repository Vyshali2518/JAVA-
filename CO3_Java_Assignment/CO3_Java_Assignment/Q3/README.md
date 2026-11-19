# Q3 – Resource Safety, Debugging and Robust Code

Compile: `javac ReportResourceDemo.java`  
Run: `java ReportResourceDemo`

Creates sample store files and processes them twice: once with manual close in a `finally` block and once with try-with-resources. The second run includes a deliberately invalid input file and logs the file that caused the failure.

**Student A:** set a breakpoint in the manual loop, inspect the open reader on the failing path, and record the resource leak.  
**Student B:** identify each opened resource, explain close order and suppressed exceptions. Note that the demonstration closes the current reader in `finally`; compare that safe variant with a deliberately faulty close-after-processing-only version during debugging.
