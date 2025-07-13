import java.sql.*;
import java.util.Scanner;

public class TakeQuiz {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (Connection conn = Dbconnection.connect()) {
            if (conn == null) {
                System.out.println("❌ Could not connect to the database.");
                return;
            }

            // Step 1: Show available quizzes
            String getQuizzes = "SELECT quiz_id, title FROM quizzes";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getQuizzes);

            System.out.println("📚 Available Quizzes:");
            while (rs.next()) {
                System.out.println(rs.getInt("quiz_id") + ": " + rs.getString("title"));
            }

            // Step 2: Ask user to select quiz
            System.out.print("\nEnter quiz ID to take: ");
            String quizIdStr = sc.nextLine().trim();
            int quizId = Integer.parseInt(quizIdStr);

            // Step 3: Fetch questions
            String getQuestions = "SELECT * FROM questions WHERE quiz_id = ?";
            PreparedStatement qstmt = conn.prepareStatement(getQuestions);
            qstmt.setInt(1, quizId);
            ResultSet questions = qstmt.executeQuery();

            int score = 0;
            int total = 0;

            // Step 4: Ask questions
            while (questions.next()) {
                total++;
                String questionText = questions.getString("question_text");
                if (questionText == null || questionText.trim().isEmpty()) {
                    questionText = "[No question text provided]";
                }

                System.out.println("\nQ" + total + ": " + questionText);
                System.out.println("A. " + questions.getString("option_a"));
                System.out.println("B. " + questions.getString("option_b"));
                System.out.println("C. " + questions.getString("option_c"));
                System.out.println("D. " + questions.getString("option_d"));

                System.out.print("Your answer (A/B/C/D): ");
                String answer = sc.nextLine().trim().toUpperCase();

                if (answer.equals(questions.getString("correct_option"))) {
                    System.out.println("✅ Correct!");
                    score++;
                } else {
                    System.out.println("❌ Incorrect! Correct answer: " + questions.getString("correct_option"));
                }
            }

            // Step 5: Display result
            System.out.println("\n🎉 Quiz Completed! Your Score: " + score + "/" + total);

            // Step 6: Save result to DB
            System.out.print("Enter your username to save your result: ");
            String username = sc.nextLine().trim();

            String insertResult = "INSERT INTO quiz_results (username, quiz_id, score, total_questions) VALUES (?, ?, ?, ?)";
            PreparedStatement resultStmt = conn.prepareStatement(insertResult);
            resultStmt.setString(1, username);
            resultStmt.setInt(2, quizId);
            resultStmt.setInt(3, score);
            resultStmt.setInt(4, total);
            resultStmt.executeUpdate();

            System.out.println("📁 Your result has been saved successfully!");

        } catch (SQLException e) {
            System.out.println("❌ SQL Error: " + e.getMessage());
        }

        sc.close();
    }
}
