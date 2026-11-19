# Q2 – User-Defined Exceptions and Propagation

Compile: `javac StorageDemo.java`  
Run: `java StorageDemo`

Demonstrates `ExamException`, `SubmissionStorageException`, checked propagation through a storage layer, and exception chaining with the original `IOException` preserved as the cause.

**Student A:** prepare the exception class diagram and storage → service/controller propagation path.  
**Student B:** test each portal rejection rule, print the chained stack trace and compare with a rethrow that loses the cause.
