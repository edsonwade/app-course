package code.with.vanilson.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * CourseRetrieveService
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-11-07
 */
public class CourseRetrieveService {
    private static final String PS_URI = "https://jsonplaceholder.typicode.com/posts";
    private static final Logger log = LoggerFactory.getLogger(CourseRetrieveService.class);

    /**
     * 1- also why of implementing.
     * private static final HttpClient CLIENT = HttpClient.newHttpClient();
     * HttpRequest -> don't need use GET method because by default is GET
     */

    //2- improving the http request
    private static final HttpClient CLIENT = HttpClient
            .newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public List<CourseRecord> getCoursesFor(String author) {
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(PS_URI.formatted(author)))
                .GET()
                .header("Content-Type", "application/json")
                .timeout(Duration.ofMinutes(2))
                .build();
        try {
            HttpResponse<String> httpResponse = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Response {}", httpResponse);

            return switch (httpResponse.statusCode()) {
                case 200 -> toGetCourses(httpResponse);
                case 404 -> {
                    log.warn("No courses found for author {}", author);
                    yield new ArrayList<>(9);
                }
                case 500 -> {
                    log.error("Internal server error while retrieving courses for author {}", author);
                    yield List.of();
                }
                default -> throw new RequestException("Request failed with status code: " + httpResponse.statusCode());
            };

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error sending request: {}", e.getMessage(), e);
            throw new RequestException("Error sending request: " + e.getMessage());
        }
    }


    private static List<CourseRecord> toGetCourses(HttpResponse<String> httpResponse) throws JsonProcessingException {
        var returnType = OBJECT_MAPPER.getTypeFactory()
                .constructCollectionType(List.class, CourseRecord.class);
        return OBJECT_MAPPER.readValue(httpResponse.body(), returnType);
    }
}