package com.with.vanilson.server;

import code.with.vanilson.domain.Course;
import code.with.vanilson.exception.CourseNotFoundException;
import code.with.vanilson.repository.CourseRepository;
import code.with.vanilson.repository.RepositoryException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

/**
 * CourseResource
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-11-09
 */
@Path("/courses")
public class CourseResource {
    private static final Logger log = LoggerFactory.getLogger(CourseResource.class);
    private final CourseRepository courseRepository;

    public CourseResource(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Course> getCourse() {
        try {
            return courseRepository
                    .findAllCourses()
                    .stream()
                    .sorted(Comparator.comparing(Course::id))
                    .toList();
        } catch (RepositoryException e) {
            log.error("Could not retrieve courses from the database {}", e.getMessage());
            throw new NotFoundException();
        }

    }

    @GET
    @Path("/{id}") // Path for getting a course by ID
    @Produces(MediaType.APPLICATION_JSON)
    public Course getCourseById(@PathParam("id") String id) {
        try {
            return courseRepository
                    .findAllCourses()
                    .stream()
                    .filter(c -> id.equals(c.id()))
                    .findAny()
                    .orElseThrow(() -> new CourseNotFoundException(
                            "Course not found with id: " + id)); // Throw custom exception

        } catch (RepositoryException e) {
            log.error("Could not retrieve course from the database by id: {} {}", id, e.getMessage());
            throw new CourseNotFoundException("Could not retrieve course with id: " + id); // Throw custom exception
        }
    }

}