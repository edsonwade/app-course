package code.with.vanilson.domain;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CourseTest {

    Course course = new Course("12", "test", 123L, "testUrls");
    private DataSource dataSource;

    @BeforeEach
    public void setUp() {
        // Initialize the DataSource
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:file:./courses.db;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1");
        this.dataSource = ds;

        // Print the current working directory
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        // Execute the initialization script
        executeInitScript("C:\\Users\\vamuhong\\Documents\\Vanilson\\Learning\\backend\\Github\\app-course\\db_init.sql"); // Adjust the path if necessary
    }

    private void executeInitScript(String scriptPath) {
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             BufferedReader reader = new BufferedReader(new FileReader(scriptPath))) {
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }
            stmt.execute(sql.toString());
        } catch (SQLException | IOException e) {
            System.out.println("Failed to execute initialization script: " + e);
        }
    }

    @Test
     void testDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Database connection successful!");
            assertNotNull(connection); // Ensure the connection is not null

            // Execute a simple query to check if the table exists
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM COURSES");
                if (rs.next()) {
                    System.out.println("Number of courses: " + rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e);
        }
    }

    @Test
    void validateIfTheCourseIsFilledTest() {
        assertEquals("12", course.id());
        assertEquals("test", course.name());
        assertEquals(123L, course.length());

    }

}