package ru.stankin.mikaev.techselect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * RecommendationsHolder.
 *
 * @author Nikita_Mikaev
 */
@NoArgsConstructor
@Getter
public class RecommendationsHolder {
    private final List<RecommendationDto> recommendations = new ArrayList<>();

    public void addRecommendation(Long recommendationId) {
        recommendations.add(new RecommendationDto(recommendationId));
    }
}
