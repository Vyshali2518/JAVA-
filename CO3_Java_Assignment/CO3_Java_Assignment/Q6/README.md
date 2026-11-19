# Q6 – Deadlock and Inter-Thread Communication

Compile: `javac ConcurrencyProblems.java`  
Run: `java ConcurrencyProblems`

Part A transfers funds between wallets while acquiring locks in a consistent identity order to avoid circular wait. Part B completes the bounded five-token producer–consumer buffer using `wait()` and `notifyAll()`.

**Student A:** explain why waits are in `while` loops inside synchronized methods, record buffer bounds, compare `wait()` and `sleep()`.  
**Student B:** implement/run a deliberately deadlocking transfer variant, identify the four Coffman conditions, then explain the lock-ordering fix.
