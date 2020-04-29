package ru.stankin.mikaev.techselect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * AnswerDto.
 *
 * @author Nikita_Mikaev
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class AnswerDto {
    private Long id;
    private String text;
    private boolean selected;
}
