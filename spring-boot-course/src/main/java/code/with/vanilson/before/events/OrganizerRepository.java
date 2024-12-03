package code.with.vanilson.before.events;

import java.util.List;

/**
 * Organizer
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-12-02
 */
@SuppressWarnings("unused")
//@Repository
public class OrganizerRepository {
    private final List<Organizer> organizers = List.of(
            new Organizer(101, "Globomatics", "Globomatics Technology Corporation"),
            new Organizer(102, "TechWorld", "TechWorld Innovations Ltd."),
            new Organizer(103, "MediTech", "MediTech Health Solutions"),
            new Organizer(104, "EcoSolutions", "EcoSolutions Green Technologies"),
            new Organizer(105, "InnoVate", "InnoVate Research & Development"),
            new Organizer(106, "FinServe", "FinServe Financial Services Inc."),
            new Organizer(107, "AgriTech", "AgriTech Agriculture Solutions"),
            new Organizer(108, "SmartEdu", "SmartEdu Educational Technologies"));

    public List<Organizer> getOrganizers() {
        return organizers
                .stream()
                .toList();
    }
}