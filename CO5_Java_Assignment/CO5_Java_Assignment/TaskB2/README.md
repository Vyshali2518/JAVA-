# B2 – Adversarial Inputs
Compile/run with MySQL Connector/J on the classpath. Set `CO5_DB_URL`, `CO5_DB_USER`, `CO5_DB_PASSWORD`. Create the test schema first.

This program deliberately contrasts unsafe concatenated SQL with PreparedStatement. Run only against your own local test database, never a production account database. For the unsafe test, use the payload embedded in the program; verify the exact result on your MySQL version. The program also validates malformed pass rows.
