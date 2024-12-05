package com.with.vanilson.server;

import code.with.vanilson.repository.CourseRepository;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import java.util.logging.LogManager;

/**
 * CourseServer
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-11-09
 */

/**
 * The CourseServer class is responsible for starting the HTTP server
 * that serves course-related resources.
 * <p>
 * This server loads its configuration from a properties file and initializes
 * the course repository for handling course data.
 * </p>
 */
public class CourseServer {
    /*
      This code executes first when the JVM is running.
      It resets the logging configuration and installs the SLF4J bridge handler.
     */
    static {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();
    }

    private static final Logger log = LoggerFactory.getLogger(CourseServer.class);
    private static final String BASE_URI = "http://localhost:8081/";

    /**
     * The main method serves as the entry point for the Course Server application.
     * It loads the database filename from the properties file, initializes the
     * course repository, and starts the HTTP server.
     *
     * @param args Command-line arguments (not used).
     */

    public static void main(String... args) {
        String databaseFilename = loadDatabaseFilename();
        log.info("Starting Course Server with database {}", databaseFilename);
        CourseRepository courseRepository = CourseRepository.getInstance(databaseFilename);
        ResourceConfig resourceConfig = new ResourceConfig().register(new CourseResource(courseRepository));

        GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), resourceConfig);
    }

    /**
     * Loads the database filename from the server properties file.
     *
     * @return The database filename as a String.
     * @throws IllegalStateException if the properties file cannot be loaded,
     *                               or if the database filename is not found in the properties.
     */
    private static String loadDatabaseFilename() {
        try (var propertiesStream = CourseServer.class.getResourceAsStream("/server.properties")) {
            var properties = new Properties();
            properties.load(propertiesStream);
            return properties.getProperty("course-info.database");
        } catch (IOException e) {
            log.error("Could not load from file ", e);
            throw new IllegalStateException("Could not load database filename");
        }
    }
}