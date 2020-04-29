package ru.stankin.mikaev.techselect.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.stankin.mikaev.techselect.model.QuestionMeta;
import java.util.Optional;

/**
 * QuestionMetaRepository.
 *
 * @author Nikita_Mikaev
 */
public interface QuestionMetaRepository extends CrudRepository<QuestionMeta, Long> {
    Optional<QuestionMeta> findById(Long id);

    @Query(value = "SELECT * FROM techselect.question_meta ORDER BY id ASC LIMIT 1",
            nativeQuery = true)
    QuestionMeta findFirstQuestion();

    @Query(value = "SELECT count(id) FROM techselect.question_meta", nativeQuery = true)
    Long getQuestionsCount();
}
