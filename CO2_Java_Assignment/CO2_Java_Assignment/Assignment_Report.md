# CO2 Assignment Report – Programming in Java

**Student Name:** ____________________  
**Register Number:** ____________________  
**Java Version:** ____________________  
**GitHub Repository:** ____________________

## Q1 – Building Text Under Load
Implemented three ticket-building approaches: repeated `String` concatenation, `StringBuilder`, and `StringBuffer`. Run `StringBenchmark` with the selected workload sizes. Record repeated `System.nanoTime()` measurements and explain the effect of String immutability. The program reports timings; memory and intermediate-object counts should be estimated or measured separately.

## Q2 – ArrayList, LinkedList and ArrayDeque
Implemented a collection benchmark covering append, front insertion/removal, indexed access and traversal. Run `CollectionBenchmark` with at least three sizes and enter the observed timings in the final report. `ArrayDeque` is included for double-ended queue operations; it is not an indexed-list replacement.

## Q3 – hashCode–equals Contract
`Attendance.java` demonstrates the corrected immutable `Student` key. `HashSet` uses both `hashCode()` and `equals()`; equal students must produce the same hash code. Run the test and record the result. To observe the faulty version, remove the `hashCode()` override and run repeated tests, noting that the exact result is not guaranteed.

## Q4 – Choosing a Map
`PharmacyInventory.java` compares insertion, lookup and iteration for `HashMap`, `TreeMap` and `LinkedHashMap`. It prints a code-sorted report and insertion-order report. Run with at least three inventory sizes and record your own measurements.

## Q5 – Generic Leaderboard
`LeaderboardDemo.java` implements a generic leaderboard with a reusable `Comparator`. Ranking order is points descending, goal difference descending, and team name ascending.

## Q6 – Emergency Triage
`Triage.java` uses a `PriorityQueue` ordered by lower severity number first and arrival sequence for ties. Run the program and verify the service order.

## Q7 – Design Challenge
The redesign demonstration replaces repeated immutable String concatenation with `StringBuilder`. Compare both approaches using identical workloads and repeated measurements. Explain trade-offs and state when the original approach is acceptable.

## Experimental Evidence to Add
The assignment requires measurements, predictions, raw data, output screenshots and a defended redesign. The programs in this repository provide executable starting points; machine-dependent timing results are deliberately not invented. Add your own measurements and screenshots after execution.
