package code.with.vanilson.service;

import code.with.vanilson.domain.Course;
import code.with.vanilson.repository.CourseRepository;

import java.util.List;

/**
 * CourseStorageService
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-11-08
 */
public class CourseStorageService {
    private final CourseRepository courseRepository;

    public CourseStorageService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void storeCourse(List<CourseRecord> psCourses) {
        for (CourseRecord psCourse : psCourses) {
            Course course1 = new Course(psCourse.id(), psCourse.title(), psCourse.durationInMinutes(), psCourse.body());
            courseRepository.saveCourse(course1);

        }
    }
}