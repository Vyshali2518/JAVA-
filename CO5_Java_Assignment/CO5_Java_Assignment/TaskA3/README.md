# A3 – Generic DAO Pattern
Compile: `javac *.java`

`Dao<T,K>` defines the generic persistence contract. `StudentDao` maps `Student` fields to the `student` table and uses try-with-resources and PreparedStatement. A live JDBC connection is injected into the DAO.
