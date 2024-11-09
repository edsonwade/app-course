package com.with.vanilson.server;

import code.with.vanilson.repository.CourseRepository;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * CourseServer
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-11-09
 */
public class CourseServer {
    private static final Logger log = LoggerFactory.getLogger(CourseServer.class);
    private static final String BASE_URI = "http://localhost:8081/";

    public static void main(String... args) {
        log.info("Starting Course Server");
        CourseRepository courseRepository = CourseRepository.getInstance("./courses.db");
        ResourceConfig resourceConfig = new ResourceConfig().register(new CourseResource(courseRepository));

        GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), resourceConfig);
    }
}