# Q7 – Diagnose, Redesign and Validate

Compile: `javac StringRedesign.java`  
Run: `java StringRedesign`

Selected bottleneck: repeated immutable String concatenation while building a ticket. The redesigned implementation uses StringBuilder. Run the same workload repeatedly and compare measured times. The report should discuss the additional mutable buffer, capacity growth, and when concatenation is acceptable for a small fixed number of values.
