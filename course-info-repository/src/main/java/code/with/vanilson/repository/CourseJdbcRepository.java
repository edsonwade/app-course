package code.with.vanilson.repository;

import code.with.vanilson.domain.Course;
import code.with.vanilson.exception.CourseNotFoundException;
import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CourseJdbcRepository
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-11-08
 */
@SuppressWarnings("unused")
class CourseJdbcRepository implements CourseRepository {
    private static final Logger log = LoggerFactory.getLogger(CourseJdbcRepository.class);

    // DATABASE FOR CONNECTIVITY
    private final DataSource dataSource;

    private static final String H2_DATABASE_URL =
            "jdbc:h2:file:%s;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1"; // Removed INIT parameter

    private static final String INSERT_COURSE = """
            MERGE INTO COURSES (id, name, length, url) VALUES (?, ?, ?, ?)
            """;

    public CourseJdbcRepository(String databaseFile) {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL(H2_DATABASE_URL.formatted(databaseFile));
        this.dataSource = ds;

        // Execute the initialization script after setting up the DataSource
        executeInitScript("db_init.sql"); // Adjust the path as needed
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
            log.error("Failed to execute initialization script: ", e);
        }
    }

    @Override
    public void saveCourse(Course course) {
        try (Connection con = this.dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(INSERT_COURSE)) {
            statement.setString(1, course.id());
            statement.setString(2, course.name());
            statement.setLong(3, course.length());
            statement.setString(4, course.url());
            statement.execute();
        } catch (SQLException e) {
            throw new RepositoryException("Failed to save " + course, e);
        }
    }

    @Override
    public Course findCourseById(String id) {
        String query = "SELECT course.id, course.name, course.length, course.url FROM COURSES course WHERE course.id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Course(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getLong("length"),
                        resultSet.getString("url"));
            } else {
                throw new CourseNotFoundException("No course found with id " + id);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Failed to find course with id " + id, e);
        }
    }

    @Override
    public List<Course> findAllCourses() {
        try (Connection connection = dataSource.getConnection();
             Statement st = connection.createStatement()) {
            String query = "SELECT course.id, course.name, course.length, course.url FROM COURSES course";
            ResultSet resultSet = st.executeQuery(query);
            List<Course> courses = new ArrayList<>();
            while (resultSet.next()) {
                Course course = new Course(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getLong(3),
                        resultSet.getString(4));
                courses.add(course);
            }
            return Collections.unmodifiableList(courses);
        } catch (SQLException e) {
            throw new RepositoryException("Failed to retrieve courses", e);
        }
    }
}
