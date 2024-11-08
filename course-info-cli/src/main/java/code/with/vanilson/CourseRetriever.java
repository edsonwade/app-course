package code.with.vanilson;

import code.with.vanilson.repository.CourseRepository;
import code.with.vanilson.service.CourseRecord;
import code.with.vanilson.service.CourseRetrieveService;
import code.with.vanilson.service.CourseStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CourseRetriever {
    public static final Logger log = LoggerFactory.getLogger(CourseRetriever.class);

    public static void main(String... args) {
        log.info("CourseRecord is starting");
        if (args.length == 0) {
            log.info("Please provide an author name as first argument.");
            return;
        }

        try {

            retrieveCourse(args[0]);
            //todo : before was this retrieveCourse(args[0])// it will not be printed use record when the member  of
            // the class are all final, because the record immutable was create o can not set new value.

            /* todo : it was to shwo how powerfull is record
            var course = new CourseRecord(100, "at nam consequatur ea labore ea harum","t distinctio eum naccusamus ratione
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
        CourseRepository courseRepository = CourseRepository.getInstance("./courses.db");
        CourseStorageService courseStorageService = new CourseStorageService(courseRepository);

        List<CourseRecord> courseRecordToStore = service.getCoursesFor(authorId)
                .stream()
                .limit(12)
                .toList();
        log.info("Retrieved the following  {} courses {}", courseRecordToStore.size(), courseRecordToStore);
        courseStorageService.storeCourse(courseRecordToStore);
        log.info("Courses successfully stored");
    }
}