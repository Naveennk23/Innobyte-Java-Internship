import java.sql.*;

public class Dbconnection {
    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ Replace "your_db_name" with your actual database name (e.g., quiz_app)
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/quiz_app?useSSL=false&serverTimezone=UTC",
                "root",
                "navin@181818"
            );

        } catch (Exception e) {
            System.out.println("❌ Connection Error: " + e.getMessage());
            return null;
        }
    }
}
