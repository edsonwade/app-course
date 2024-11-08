package code.with.vanilson.service;

/**
 * RequestException
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-11-07
 */
public class RequestException  extends RuntimeException{
    public RequestException(String message) {
        super(message);
    }
}