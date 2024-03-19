
// from lecs
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {

    static Connection connection = null;

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/Assignment3";
        String user = "postgres";
        String password = "3348";

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                System.out.println("connected");
            } else {
                System.out.println("not connected");
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

        }
        // getAllStudents();

        addStudent("Jeffrey", "Ho", "jh@example.com", Date.valueOf("2024-03-7"));
        getAllStudents();

        // updateStudentEmail(4, "jeffreyhoNEW@example.com");
        // getAllStudents();

        // deleteStudent(4);
        // getAllStudents();
    }

    // method getAllStudents, does SELECT * FROM students
    public static void getAllStudents() {
        String query = "SELECT * FROM students";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // format
            System.out.println("student_id | first_name | last_name | email | enrollment_date");
            System.out.println("-----------------------------------------------------------------------");

            // assign temp variable
            while (resultSet.next()) {
                int studentId = resultSet.getInt("student_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                Date enrollmentDate = resultSet.getDate("enrollment_date");

                // print formatted information
                System.out.println(
                        studentId + " | " + firstName + " | " + lastName + " | " + email + " | " + enrollmentDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addStudent(String firstName, String lastName, String email, Date enrollmentDate) {
        // query
        String query = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
        // build structure
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setDate(4, enrollmentDate);

            // add student and return if successful
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new student was added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateStudentEmail(int studentId, String newEmail) {
        String query = "UPDATE students SET email = ? WHERE student_id = ?";
        try {
            // build update info
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newEmail);
            preparedStatement.setInt(2, studentId);
            // execute, return if successful
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Email address updated successfully for student with ID: " + studentId);
            } else {
                System.out.println("No student found with ID: " + studentId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteStudent(int studentId) {
        String query = "DELETE FROM students WHERE student_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, studentId);

            // run
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Student with ID " + studentId + " has been deleted.");
            } else {
                System.out.println("No student found with ID: " + studentId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
