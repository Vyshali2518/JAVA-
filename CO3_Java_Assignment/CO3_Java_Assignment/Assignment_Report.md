# CO3 Assignment 3 – Pair Report

**Student A:** ____________________  **Register No.:** ____________________  
**Student B:** ____________________  **Register No.:** ____________________  
**Java Version:** ____________________  
**Student A GitHub:** ____________________  
**Student B GitHub:** ____________________

## Pair-work principle
Both students participated in every question and individually executed all programs. The responsibilities below identify the primary evidence each student prepared; they do not divide the assignment question-wise.

## Q1 – ATM exception handling
- Student A: Throwable hierarchy, failure classification, catch ordering and finally guarantee.
- Student B: at least five test inputs, catch/finally trace, uncaught stack trace and broad-before-narrow catch demonstration.
- Joint: evaluate whether catching `Exception` alone is appropriate.
- Program: `Q1/ATM.java`.

## Q2 – Online examination portal exceptions
- Student A: exception hierarchy, checked/unchecked justification and propagation diagram.
- Student B: failure scenarios, `getCause()` evidence, chained stack trace and test table.
- Joint: explain the value of a meaningful storage-layer exception.
- Program: `Q2/StorageDemo.java`.

## Q3 – Daily sales report resource safety
- Student A: debugger evidence, injected mid-run failure, open-resource count and file-specific logging.
- Student B: resource inventory, close order, try-with-resources and suppressed-exception explanation.
- Joint: identify remaining robustness requirements.
- Program: `Q3/ReportResourceDemo.java`.

## Q4 – Parallel answer-script scanning
- Student A: Thread vs Runnable, life-cycle diagram, `run()` vs `start()` and `join()`.
- Student B: ten finish-order runs, priority and daemon experiments.
- Joint: explain scheduler-dependent ordering.
- Program: `Q4/ScannerStations.java`.

## Q5 – Cinema seat booking
- Student A: reproduce and count races, show interleaving and identify critical section.
- Student B: time 10,000 attempts, compare synchronized method/block, discuss intrinsic and static locks.
- Joint: defend the strategy for a multi-screen cinema.
- Program: `Q5/SeatBooking.java`.

## Q6 – Wallet transfers and mess token counter
- Student A: producer–consumer design, wait loop, notify/notifyAll, capacity trace and sleep vs wait.
- Student B: deadlock reproduction, four conditions, lock-ordering prevention.
- Joint: explain progress guarantees.
- Program: `Q6/ConcurrencyProblems.java`.

## Q7 – Concurrent result-file processor
- Student A: ExecutorService/Callable/Future design, concurrent results, exception strategy, class diagram and pseudocode.
- Student B: implementation, corrupt/missing-file injection, pool-size measurements and hand-checked counts.
- Joint: defend pool size, summary correctness and remaining limitation.
- Program: `Q7/ResultProcessor.java`.

## Individual contribution record

| Question | Student A – Primary responsibility | Student B – Primary responsibility | Joint |
|---|---|---|---|
| Q1 | Exception design | Testing and tracing | Single-catch decision |
| Q2 | Hierarchy and propagation | Scenarios and chaining | Storage exception design |
| Q3 | Debugging and experiments | Resource safety | Robustness decision |
| Q4 | Thread design | Behavioural experiments | Scheduling conclusion |
| Q5 | Race reproduction | Synchronization comparison | Deployment decision |
| Q6 | Producer–consumer design | Deadlock analysis | Progress conclusion |
| Q7 | System design | Implementation and measurement | Engineering defence |

## Evidence to attach
Add actual screenshots, test tables, stack traces, logs and raw measurement data. Run Q4/Q5/Q7 repeatedly and record your own observations. No machine-specific timing or scheduler order is assumed in this report.
