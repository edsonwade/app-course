package code.with.vanilson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CourseRetriever {
    public static final Logger log = LoggerFactory.getLogger(CourseRetriever.class);

    public static void main(String... args) {
        log.info("Course is starting");
        if (args.length == 0) {
            log.info("Please provide an author name as first argument.");
            return;
        }

        try {
            if (args[0] != null && !args[0].isEmpty()) {
                retrieveCourse(args[0]);
            }
        } catch (Exception e) {
            log.error("Unexpected error {}", e.getMessage());
        }
    }

    private static void retrieveCourse(String arg) {
        log.info("Retrieving courses for author {}", arg);
    }
}