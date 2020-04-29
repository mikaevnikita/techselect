package ru.stankin.mikaev.techselect.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.stankin.mikaev.techselect.model.Question;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * QuestionRepository.
 *
 * @author Nikita_Mikaev
 */
public interface QuestionRepository extends CrudRepository<Question, Long> {

    @Query(value = "SELECT * FROM question WHERE session_id = :session_id ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Optional<Question> findLastQuestion(@Param("session_id") UUID sessionId);

    Optional<Question> findBySessionIdAndMetaId(UUID sessionId, Long metaId);

    @Query(value = "SELECT count(id) FROM question WHERE session_id = :session_id", nativeQuery = true)
    Long getQuestionsCountByUser(@Param("session_id") UUID sessionId);

    List<Question> findAllBySessionId(@Param("session_id") UUID sessionId);
}
