package code.with.vanilson.before.registration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Organizer
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-12-02
 */
public record Registration(
        Integer id,
        @NotNull(message = "Product id is required")
        Integer productId,
        String ticketCode,
        @NotBlank(message = "Attendee name is required")
        String attendeeName) {
}