package code.with.vanilson.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Duration;
import java.time.LocalTime;

/**
 * CourseRecord
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-11-07
 */
@JsonIgnoreProperties(ignoreUnknown = true) //ignora outras propriedades
public record CourseRecord(String id, String title, String body, String duration) {

    public long durationInMinutes() {
        return Duration.between(
                LocalTime.MIN,
                LocalTime.parse(duration)
        ).toMinutes();
    }
}