package com.with.vanilson.server.exception;

/**
 * CourseNotFoundException
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-11-09
 */
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class CourseNotFoundException extends WebApplicationException {
    public CourseNotFoundException(String message) {
        super(Response.status(Response.Status.NOT_FOUND).entity(message).build());
    }
}
