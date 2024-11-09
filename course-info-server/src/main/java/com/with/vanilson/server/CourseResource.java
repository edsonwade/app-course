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

/**
 * The CourseResource class provides RESTful endpoints for managing courses.
 * It allows clients to retrieve a list of all courses or a specific course by its ID.
 */
@Path("/courses")
public class CourseResource {
    private static final Logger log = LoggerFactory.getLogger(CourseResource.class);
    private final CourseRepository courseRepository;

    /**
     * Constructs a CourseResource with the specified CourseRepository.
     *
     * @param courseRepository The repository used to access course data.
     */
    public CourseResource(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Retrieves a list of all courses.
     *
     * @return A list of Course objects in JSON format.
     * @throws NotFoundException if there is an error retrieving courses from the database.
     */

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

    /**
     * Retrieves a specific course by its ID.
     *
     * @param id The ID of the course to retrieve.
     * @return The Course object with the specified ID in JSON format.
     * @throws CourseNotFoundException if no course is found with the specified ID.
     * @throws NotFoundException       if there is an error retrieving the course from the database.
     */
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