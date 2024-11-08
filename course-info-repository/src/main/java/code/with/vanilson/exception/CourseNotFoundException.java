package code.with.vanilson.exception;

/**
 * CourseNotFoundException
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-11-08
 */
public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String message) {
        super(message);
    }
}