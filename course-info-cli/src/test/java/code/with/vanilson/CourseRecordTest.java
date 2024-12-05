package code.with.vanilson;

import code.with.vanilson.service.CourseRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseRecordTest {
    @Test
    void durationInMinutes() {
        var course = new CourseRecord("1", "Test course", "Test body", "00:12:37");
        assertEquals(12, course.durationInMinutes());
    }

    @Test
    void durationInMinutesOverHour() {
        var course = new CourseRecord("1", "Test course", "Test body", "01:08:54.9613330");
        assertEquals(68, course.durationInMinutes());
    }

    @Test
    void durationInMinutesZero() {
        var course = new CourseRecord("1", "Test course", "Test body", "00:00:00");
        assertEquals(0, course.durationInMinutes());
    }

    //Using ParameterizedTest

    /**
     * Tests the {@link CourseRecord#durationInMinutes()} method using various input durations.
     * <p>
     * This parameterized test uses {@link CsvSource} to provide multiple sets of input values
     * and expected results. Each input represents a duration in the format of a time string,
     * and the expected result is the corresponding duration in minutes.
     *
     * <p>Example inputs and expected outputs:</p>
     * <ul>
     *     <li>Input: "01:08:54.9613330" -> Expected: 68 minutes</li>
     *     <li>Input: "00:05:37" -> Expected: 5 minutes</li>
     *     <li>Input: "00:00:00.0" -> Expected: 0 minutes</li>
     * </ul>
     *
     * @param input    the duration string to be converted to minutes
     * @param expected the expected duration in minutes
     */
    @ParameterizedTest
    @CsvSource(textBlock = """
                 01:08:54.9613330, 68
                 00:05:37, 5
                 00:00:00.0, 0
            """)
    void durationInMinuteWithParameterized(String input, long expected) {
        var course = new CourseRecord("1", "Test course", "Test body", input);
        assertEquals(expected, course.durationInMinutes());
    }

    /*
     * Using parameterized tests, such as the one demonstrated in your example, enhances the efficiency and effectiveness of your testing strategy.
     * It allows you to cover a variety of scenarios with minimal code,
     * making your tests easier to read, maintain, and extend.
     */
}