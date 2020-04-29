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

    public QuestionDto getQuestionById(Long id){
        return questions.stream().filter(q -> q.getId().equals(id)).findFirst().get();
    }

    public AnswerDto getAnswerById(Long id) {
        return questions.stream()
                .flatMap(q -> q.getAnswers().stream())
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .get();
    }

    public boolean isSelectedAnswer(Long id) {
        return getAnswerById(id).isSelected();
    }
}
