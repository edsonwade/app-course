package code.with.vanilson.event;

import code.with.vanilson.exception.VenueNotFoundException;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

/**
 * VenueRepository
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-12-02
 */
@SuppressWarnings("unused")
public class VenueRepository {

    private final List<Venue> venues = List.of(
            new Venue(1, "Grand Plaza", "123 Main St", "New York", "USA"),
            new Venue(2, "Tech Hub", "456 Silicon Valley", "San Francisco", "USA"),
            new Venue(3, "Innovation Center", "789 Innovation Rd", "Austin", "USA"),
            new Venue(4, "Global Conference Center", "101 Global Blvd", "London", "UK"),
            new Venue(5, "Expo Arena", "202 Expo Drive", "Berlin", "Germany"),
            new Venue(6, "Creative Space", "303 Creative St", "Paris", "France"),
            new Venue(7, "Summit Hall", "404 Summit Ave", "Tokyo", "Japan")
    );

    public Optional<Venue> findVenueById(int venueId) {
        return Optional.ofNullable(venues
                .stream()
                .filter(venue -> venue.id() == venueId)
                .findAny()
                .orElseThrow(() -> new VenueNotFoundException(
                        MessageFormat.format("Venue with id {0} not found", venueId))));

    }

}