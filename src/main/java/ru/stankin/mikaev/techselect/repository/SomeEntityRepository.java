package ru.stankin.mikaev.techselect.repository;

import org.springframework.data.repository.CrudRepository;
import ru.stankin.mikaev.techselect.model.SomeEntity;

/**
 * SomeEntityRepository.
 *
 * @author Nikita_Mikaev
 */
public interface SomeEntityRepository extends CrudRepository<SomeEntity, Long> {
}
