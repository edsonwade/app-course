package com.with.vanilson.server.exception;

/**
 * CourseNotFoundExceptionMapper
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-11-09
 */
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;

@Provider
public class CourseNotFoundExceptionMapper implements ExceptionMapper<CourseNotFoundException> {
    @Override
    public Response toResponse(CourseNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(exception.getMessage())
                .build();
    }
}
