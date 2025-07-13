import java.sql.*;
import java.util.Scanner;

public class AdminAddQuiz {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (Connection conn = Dbconnection.connect()) {

            // Step 1: Create a new quiz
            System.out.print("Enter quiz title: ");
            String title = sc.nextLine();

            String insertQuiz = "INSERT INTO quizzes (title) VALUES (?)";
            PreparedStatement quizStmt = conn.prepareStatement(insertQuiz, Statement.RETURN_GENERATED_KEYS);
            quizStmt.setString(1, title);
            quizStmt.executeUpdate();

            // Get quiz_id of newly created quiz
            ResultSet rs = quizStmt.getGeneratedKeys();
            int quizId = -1;
            if (rs.next()) {
                quizId = rs.getInt(1);
                System.out.println("✅ Quiz created with ID: " + quizId);
            }

            // Step 2: Add questions to quiz
            String insertQ = "INSERT INTO questions (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement questionStmt = conn.prepareStatement(insertQ);

            while (true) {
                System.out.println("\nEnter question (or type 'exit' to finish): ");
                String question = sc.nextLine();
                if (question.equalsIgnoreCase("exit")) break;

                System.out.print("Option A: ");
                String a = sc.nextLine();
                System.out.print("Option B: ");
                String b = sc.nextLine();
                System.out.print("Option C: ");
                String c = sc.nextLine();
                System.out.print("Option D: ");
                String d = sc.nextLine();
                System.out.print("Correct option (A/B/C/D): ");
                String correct = sc.nextLine().toUpperCase();

                questionStmt.setInt(1, quizId);
                questionStmt.setString(2, question);
                questionStmt.setString(3, a);
                questionStmt.setString(4, b);
                questionStmt.setString(5, c);
                questionStmt.setString(6, d);
                questionStmt.setString(7, correct);

                questionStmt.executeUpdate();
                System.out.println("✅ Question added!");
            }

            System.out.println("✅ All questions saved successfully!");

        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }

        sc.close();
    }
}
