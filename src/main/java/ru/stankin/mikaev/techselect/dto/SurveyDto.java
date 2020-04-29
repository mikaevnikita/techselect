package ru.stankin.mikaev.techselect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import java.util.List;

/**
 * SurveyDto.
 *
 * @author Nikita_Mikaev
 */
@Data
@RequiredArgsConstructor
@Builder
public class SurveyDto {

    private final List<QuestionDto> questions;
}
