import java.sql.*;
import java.util.Scanner;

public class Register {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = sc.nextLine();

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        try (Connection conn = Dbconnection.connect()) {
            String sql = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password); // Later you can hash this

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ User registered successfully!");
            } else {
                System.out.println("❌ Failed to register user.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }

        sc.close();
    }
}
