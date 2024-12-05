package code.with.vanilson.before.events;

import java.math.BigDecimal;

/**
 * Organizer
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-12-02
 */
@SuppressWarnings("unused")
public record Product(int id, int eventId, String name, String description, BigDecimal price) {
}