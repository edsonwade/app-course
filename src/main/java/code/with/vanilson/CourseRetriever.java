package code.with.vanilson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CourseRetriever {
    public static final Logger log = LoggerFactory.getLogger(CourseRetriever.class);

    public static void main(String... args) {
        log.info("Course is starting");
        if (args.length == 0) {
            log.info("Please provide an author name as first argument.");
            return;
        }

        try {

            retrieveCourse(args[0]);
            //todo : before was this retrieveCourse(args[0])// it will not be printed use record when the member  of
            // the class are all final, because the record immutable was create o can not set new value.

            /* todo : it was to shwo how powerfull is record
            var course = new Course(100, "at nam consequatur ea labore ea harum","t distinctio eum naccusamus ratione
             error aut"); // will not pe printed
            log.info("course: {}", course); ; // will not pe printed
            */

        } catch (Exception e) {
            log.error("Unexpected error {}", e.getMessage());
        }
    }

    private static void retrieveCourse(String authorId) {
        log.info("Retrieving courses for author {}", authorId);
        var service = new CourseRetrieveService();
        //filtering course
        List<Course> courseToStore = service.getCoursesFor(authorId)
                .stream()
                .limit(12)
                .filter(course -> course.id() % 2 == 0)
                .toList();
        log.info("Retrieved the following  {} courses {}", courseToStore.size(), courseToStore);
    }
}