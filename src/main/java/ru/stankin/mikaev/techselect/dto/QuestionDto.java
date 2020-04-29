package ru.stankin.mikaev.techselect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Optional;

/**
 * QuestionDto.
 *
 * @author Nikita_Mikaev
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDto {
    private Long id;
    private String text;
    private List<AnswerDto> answers;

    public void setSelectedAnswer(Long answerId) {
        answers
                .stream()
                .filter(a -> a.getId().equals(answerId))
                .findFirst()
                .ifPresent(a -> a.setSelected(true));
    }

    public Optional<AnswerDto> getSelectedAnswer() {
        return answers.stream().filter(AnswerDto::isSelected).findFirst();
    }
}
