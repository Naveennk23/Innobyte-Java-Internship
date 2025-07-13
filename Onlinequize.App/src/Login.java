import java.sql.*;
import java.util.Scanner;

public class Login {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        try (Connection conn = Dbconnection.connect()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("✅ Login successful! Welcome, " + rs.getString("username"));
                System.out.println("Role: " + rs.getString("role"));
            } else {
                System.out.println("❌ Invalid username or password.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }

        sc.close();
    }
}
