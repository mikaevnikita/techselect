package ru.stankin.mikaev.techselect.repository;

import org.springframework.data.repository.CrudRepository;
import ru.stankin.mikaev.techselect.model.AnswerMeta;
import java.util.List;

/**
 * AnswerMetaRepository.
 *
 * @author Nikita_Mikaev
 */
public interface AnswerMetaRepository extends CrudRepository<AnswerMeta, Long> {

    List<AnswerMeta> findAllByQuestionMetaId(Long questionMetaId);
}
