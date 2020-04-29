package ru.stankin.mikaev.techselect.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.stankin.mikaev.techselect.model.Answer;

/**
 * AnswerRepository.
 *
 * @author Nikita_Mikaev
 */
public interface AnswerRepository extends CrudRepository<Answer, Long> {
    Answer findBySessionIdAndQuestionMetaId(Long sessionId, Long questionMetaId);
}
