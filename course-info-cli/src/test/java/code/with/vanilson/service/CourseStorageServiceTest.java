package code.with.vanilson.service;

import code.with.vanilson.domain.Course;
import code.with.vanilson.exception.CourseNotFoundException;
import code.with.vanilson.repository.CourseRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseStorageServiceTest {

    @Test
    void storeCourses() {
        CourseRepository courseRepository = new InMemoryCourseStorageService();
        CourseStorageService courseStorageService = new CourseStorageService(courseRepository);
        var ps1 = new CourseRecord("1", "Title 1", "https://app.pluralsight.com", "01:09:00.123");
        courseStorageService.storeCourse(List.of(ps1));

        Course expected = new Course("1", "Title 1", 69, "https://app.pluralsight.com");
        assertEquals(List.of(expected), courseRepository.findAllCourses());
    }

    static class InMemoryCourseStorageService implements CourseRepository {

        private final List<Course> courses = new ArrayList<>();

        @Override
        public void saveCourse(Course course) {
            courses.add(course);

        }

        @Override
        public Course findCourseById(String id) {
            for (Course course : courses) {
                if (course.id().equals(id)) {
                    return course;
                }
            }
            throw new CourseNotFoundException("Course with id " + id + " not found");
        }

        @Override
        public List<Course> findAllCourses() {
            return courses;
        }
    }
}