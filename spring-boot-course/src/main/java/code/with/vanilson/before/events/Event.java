package code.with.vanilson.before.events;

import java.time.LocalDate;

/**
 * Organizer
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-12-02
 */
@SuppressWarnings("unused")
public record Event(
        int id,
        String name,
        Organizer organizer,
        Venue venue,
        LocalDate startDate,
        LocalDate endDate) {
}
