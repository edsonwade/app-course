package code.with.vanilson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Course
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-11-07
 */
@JsonIgnoreProperties(ignoreUnknown = true) //ignora outras propriedades
public record Course(int id, String title, String body, String duration) {

    public long durationInMinutes() {
        return Duration.between(
                LocalTime.MIN,
                LocalTime.parse(duration)
        ).toMinutes();
    }
}