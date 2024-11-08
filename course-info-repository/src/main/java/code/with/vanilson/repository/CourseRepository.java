package code.with.vanilson.repository;

import code.with.vanilson.domain.Course;

import java.util.List;

/**
 * CourseRepository
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-11-08
 */
public interface CourseRepository {
    void saveCourse(Course course);

    Course findCourseById(String id);

    List<Course> findAllCourses();

    static CourseRepository getInstance(String databaseFileName) {
        return new CourseJdbcRepository(databaseFileName);

    }

}
