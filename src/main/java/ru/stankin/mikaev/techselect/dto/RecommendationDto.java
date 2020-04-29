package ru.stankin.mikaev.techselect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RecommendationDto.
 *
 * @author Nikita_Mikaev
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationDto {
    private Long recommendationId;
}
