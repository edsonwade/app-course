package code.with.vanilson.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Course
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-11-08
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Course(String id, String name, long length, String url) {

    public Course {
        filled(id);
        filled(name);
        filled(url);
    }

   private static void filled(String s) {
        if (null == s || s.isBlank()) {
            throw new IllegalArgumentException("No value present");
        }
    }
}