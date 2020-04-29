package ru.stankin.mikaev.techselect.repository;

import org.springframework.data.repository.CrudRepository;
import ru.stankin.mikaev.techselect.model.Recommendation;

/**
 * RecommendationRepository.
 *
 * @author Nikita_Mikaev
 */
public interface RecommendationRepository extends CrudRepository<Recommendation, Long> {
}
