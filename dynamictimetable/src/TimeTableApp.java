import java.sql.*;
import java.util.Scanner;

public class TimeTableApp {
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/timetable";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Priyanka@2000";

    // Database table and column names
    private static final String TABLE_SUBJECTS = "subjects";
    private static final String COLUMN_SUBJECT_ID = "subject_id";
    private static final String COLUMN_SUBJECT_NAME = "subject_name";

    private static final String TABLE_TIMETABLE = "timetable";
    private static final String COLUMN_TIMETABLE_ID = "timetable_id";
    private static final String COLUMN_DAY = "day";
    private static final String COLUMN_PERIOD = "period";
    private static final String COLUMN_SUBJECT_ID_FK = "subject_id_fk";

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role"; // New column for role
    private static final String ROLE_ADMIN = "admin";
    private static final String ROLE_STUDENT = "student";

    // Main menu choices
    private static final int CHOICE_VIEW_TIMETABLE = 1;
    private static final int CHOICE_UPDATE_TIMETABLE = 2;
    private static final int CHOICE_ADD_SUBJECT = 3;
    private static final int CHOICE_ADD_TIMETABLE = 4;
    private static final int CHOICE_EXIT = 5;

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        try {
            // Connect to the database
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Create tables if they don't exist
            createTables(conn);

            boolean loggedIn = false;
            while (!loggedIn) {
                int role = getRoleChoice();

                switch (role) {
                    case 1:
                        // Student Login
                        if (validateUserLogin(conn, ROLE_STUDENT)) {
                            displayStudentMenu(conn);
                        }
                        break;
                    case 2:
                        // Admin Login
                        if (validateUserLogin(conn, ROLE_ADMIN)) {
                            displayAdminMenu(conn);
                        }
                        break;
                    case 3:
                        loggedIn = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the database connection and statement
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();

        String createSubjectsTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_SUBJECTS + "(" +
                COLUMN_SUBJECT_ID + " INT AUTO_INCREMENT PRIMARY KEY," +
                COLUMN_SUBJECT_NAME + " VARCHAR(255) NOT NULL)";
        stmt.executeUpdate(createSubjectsTableQuery);

        String createTimetableTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_TIMETABLE + "(" +
                COLUMN_TIMETABLE_ID + " INT AUTO_INCREMENT PRIMARY KEY," +
                COLUMN_DAY + " VARCHAR(255) NOT NULL," +
                COLUMN_PERIOD + " INT NOT NULL," +
                COLUMN_SUBJECT_ID_FK + " INT NOT NULL," +
                "FOREIGN KEY (" + COLUMN_SUBJECT_ID_FK + ") REFERENCES " + TABLE_SUBJECTS + "(" + COLUMN_SUBJECT_ID
                + "))";
        stmt.executeUpdate(createTimetableTableQuery);

        String createUserTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "(" +
                COLUMN_USERNAME + " VARCHAR(255) NOT NULL PRIMARY KEY," +
                COLUMN_PASSWORD + " VARCHAR(255) NOT NULL," +
                COLUMN_ROLE + " VARCHAR(255) NOT NULL)";
        stmt.executeUpdate(createUserTableQuery);

        stmt.close();
    }

    private static int getRoleChoice() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("-------------------------------------");
        System.out.println("Select your role:");
        System.out.println("-------------------------------------");
        System.out.println("|1. Student");
        System.out.println("|2. Admin");
        System.out.println("|3. Exit");
        System.out.println("-------------------------------------");

        return getIntInput("Enter your choice: ");

    }

    private static boolean validateUserLogin(Connection conn, String role) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("-------------------------------------");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COLUMN_USERNAME + " = ? AND " +
                COLUMN_PASSWORD + " = ? AND " +
                COLUMN_ROLE + " = ?";

        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.setString(3, role);
        ResultSet rs = pstmt.executeQuery();

        boolean loginSuccessful = rs.next();
        rs.close();
        pstmt.close();

        if (!loginSuccessful) {
            System.out.println("Invalid username or password.");
        }

        return loginSuccessful;
    }

    private static void displayStudentMenu(Connection conn) throws SQLException {
        // Code for student menu options
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        System.out.println("-------------------------------------");
        System.out.println("\nTime Table Management System");
        System.out.println("-------------------------------------");
        System.out.println("|1. View Time Table");
        System.out.println("|5. Exit");
        System.out.println("-------------------------------------");
        int choice = getIntInput(scanner, "Enter your choice: ");
        switch (choice) {
            case CHOICE_VIEW_TIMETABLE:
                viewTimeTable(conn);
                break;
            case CHOICE_EXIT:
                exit = true;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void displayAdminMenu(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nADMIN MENU");
            System.out.println("-------------------------------------");
            System.out.println("|1. View Timetable");
            System.out.println("|2. Update Timetable");
            System.out.println("|3. Add Subject");
            System.out.println("|4. Add Timetable");
            System.out.println("|5. Exit");

            System.out.println("-------------------------------------");
            int choice = getIntInput(scanner, "Enter your choice: ");
            System.out.println("-------------------------------------");

            switch (choice) {
                case CHOICE_VIEW_TIMETABLE:
                    viewTimeTable(conn);
                    break;
                case CHOICE_UPDATE_TIMETABLE:
                    updateTimeTable(conn);
                    break;
                case CHOICE_ADD_SUBJECT:
                    addSubject(conn);
                    break;
                case CHOICE_ADD_TIMETABLE:
                    addTimeTable(conn);
                    break;
                case CHOICE_EXIT:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewTimeTable(Connection conn) throws SQLException {
        // Code to view the timetable
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TABLE_TIMETABLE);
        ResultSet rs = stmt.executeQuery();

        System.out.println("-------------------------------------");
        System.out.println("\nTime Table:");

        while (rs.next()) {
            int timeTableId = rs.getInt(COLUMN_TIMETABLE_ID);
            String day = rs.getString(COLUMN_DAY);
            int period = rs.getInt(COLUMN_PERIOD);
            int subjectId = rs.getInt(COLUMN_SUBJECT_ID_FK);

            String subjectName = getSubjectName(conn, subjectId);

            System.out.println("-------------------------------------");
            System.out.println("|Time Table ID: " + timeTableId);
            System.out.println("|Day: " + day);
            System.out.println("|Period: " + period);
            System.out.println("|Subject: " + subjectName);
            System.out.println("-------------------------------------");
            System.out.println();
        }

        stmt.close();
    }

    private static void updateTimeTable(Connection conn) throws SQLException {
        // Code to update the timetable
        int timeTableId = getIntInput("Enter Time Table ID: ");
        String day = getStringInput("Enter Day: ");
        int period = getIntInput("Enter Period: ");
        int subjectId = getIntInput("Enter Subject ID: ");

        PreparedStatement stmt = conn.prepareStatement("UPDATE " + TABLE_TIMETABLE + " SET " +
                COLUMN_DAY + " = ?, " +
                COLUMN_PERIOD + " = ?, " +
                COLUMN_SUBJECT_ID_FK + " = ? WHERE " +
                COLUMN_TIMETABLE_ID + " = ?");
        stmt.setString(1, day);
        stmt.setInt(2, period);
        stmt.setInt(3, subjectId);
        stmt.setInt(4, timeTableId);

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("-------------------------------------");
            System.out.println("Time Table updated successfully!");
            System.out.println("-------------------------------------");
        } else {
            System.out.println("Failed to update Time Table.");
        }

        stmt.close();
    }

    private static void addSubject(Connection conn) throws SQLException {
        // Code to add a subject
        int subjectId = getIntInput("Enter Subject ID: ");
        String subjectName = getStringInput("Enter subject name: ");

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + TABLE_SUBJECTS +
                " (" + COLUMN_SUBJECT_ID + ", " + COLUMN_SUBJECT_NAME + ") VALUES (?, ?)");

        stmt.setInt(1, subjectId);
        stmt.setString(2, subjectName);

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("-------------------------------------");
            System.out.println("Subject added successfully!");
            System.out.println("-------------------------------------");
        } else {
            System.out.println("-------------------------------------");
            System.out.println("Failed to add subject.");
            System.out.println("-------------------------------------");
        }

        stmt.close();
    }

    private static void addTimeTable(Connection conn) throws SQLException {
        // Code to add an entry to the timetable
        int timeTableId = getIntInput("Enter Time Table ID: ");
        String day = getStringInput("Enter Day: ");
        int period = getIntInput("Enter Period: ");
        int subjectId = getIntInput("Enter Subject ID: ");

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + TABLE_TIMETABLE +
                " (" + COLUMN_DAY + ", " + COLUMN_PERIOD + " , " + COLUMN_SUBJECT_ID_FK + " , " + COLUMN_TIMETABLE_ID
                + ") VALUES (?, ?, ?, ?)");
        stmt.setString(1, day);
        stmt.setInt(2, period);
        stmt.setInt(3, subjectId);
        stmt.setInt(4, timeTableId);

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("-------------------------------------");
            System.out.println("Time Table updated successfully!");
            System.out.println("-------------------------------------");
        } else {
            System.out.println("-------------------------------------");
            System.out.println("Failed to update Time Table.");
            System.out.println("-------------------------------------");
        }

        stmt.close();
    }

    private static String getSubjectName(Connection conn, int subjectId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT " + COLUMN_SUBJECT_NAME + " FROM " + TABLE_SUBJECTS +
                " WHERE " + COLUMN_SUBJECT_ID + " = ?");
        stmt.setInt(1, subjectId);

        ResultSet rs = stmt.executeQuery();
        String subjectName = "";

        if (rs.next()) {
            subjectName = rs.getString(COLUMN_SUBJECT_NAME);
        }

        stmt.close();

        return subjectName;
    }

    // Utility method to get integer input from the user
    private static int getIntInput(Scanner scanner, String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter an integer: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static int getIntInput(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        return scanner.nextInt();
    }

    private static String getStringInput(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        return scanner.nextLine();
    }

}
