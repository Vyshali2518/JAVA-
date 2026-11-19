# Q7 – Concurrent, Fault-Tolerant Result Processor

Compile: `javac ResultProcessor.java`  
Run: `java ResultProcessor [pool-size]`

Processes generated score files with `ExecutorService`, `Callable` and `Future`. Missing and corrupt files are represented as per-file failures; successful results and failure messages are gathered without aborting the entire batch. The default pool size is 4; pass 1, 2, 4 or 8 to compare.

**Student A:** document class diagram, task decomposition, concurrent result collection and pseudocode.  
**Student B:** test missing/corrupt files, verify counts against a hand-checked set, and measure pool sizes 1, 2, 4 and 8. The program creates a temporary sample dataset; use a larger generated set to approach the assignment's 10,000-file workload.
