# Java Library Management System

Console-based Java project using Core Java, OOP, JDBC, and MySQL.

## Features
- Book CRUD: add, display, search, update, delete
- Member CRUD: add, display, search, update, delete
- Issue and return books with transaction handling
- Available, issued, and returned book reports
- PreparedStatement queries and try-with-resources

## Requirements
- JDK 17 or newer (the code uses modern switch expressions)
- MySQL 8+
- MySQL Connector/J JAR
- VS Code with Extension Pack for Java

## Setup
1. Start MySQL.
2. Run `database/library_management.sql` in MySQL Workbench or the MySQL command line.
3. Download MySQL Connector/J from the official MySQL website and place the JAR in `lib/`.
4. Configure connection settings using environment variables:
   - `LIBRARY_DB_URL` (default: `jdbc:mysql://localhost:3306/library_management`)
   - `LIBRARY_DB_USER` (default: `root`)
   - `LIBRARY_DB_PASSWORD` (default: empty)
5. In VS Code, add `lib/mysql-connector-j.jar` to the Java project referenced libraries.
6. Run `src/org/example/Main.java`.

## Compile from terminal (Windows example)
Replace `mysql-connector-j.jar` with the actual JAR filename:

```powershell
javac -cp "lib\mysql-connector-j.jar" -d out src\org\example\connection\MySQLConnection.java src\org\example\model\*.java src\org\example\operations\*.java src\org\example\Main.java
java -cp "out;lib\mysql-connector-j.jar" org.example.Main
```

For Linux/macOS, use `:` instead of `;` in the Java runtime classpath.

## Important
- Set your actual MySQL password in `LIBRARY_DB_PASSWORD`; do not commit passwords to Git.
- The application is a learning project, not a production-ready library system.
