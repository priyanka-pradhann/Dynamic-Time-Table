import java.sql.*;
import java.util.*;
public class MYSQLEx {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/bbsr_usbm";
        String username = "root";
        String password = "Priyanka@2000";
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Enter the verification name");
            // Prompt the user for input
            String userInput = sc.nextLine(); // Replace with actual user input

            // Create the SQL statement
            String sql = "SELECT * FROM MCA1 WHERE name = ?";

            // Create the connection
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                // Set the parameter value
                statement.setString(1, userInput);

                // Execute the query
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        System.out.println("User input exists in the database.");
                    } else {
                        System.out.println("User input does not exist in the database.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}