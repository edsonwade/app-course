package code.with.vanilson.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * VenueNotFoundException
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-12-02
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class VenueNotFoundException extends RuntimeException {
    public VenueNotFoundException(String message) {
        super(message);
    }
}