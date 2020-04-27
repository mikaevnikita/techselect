package ru.stankin.mikaev.techselect.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stankin.mikaev.techselect.model.SomeEntity;
import ru.stankin.mikaev.techselect.repository.SomeEntityRepository;
import java.util.List;
import java.util.Optional;

/**
 * TestController.
 *
 * @author Nikita_Mikaev
 */
@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {

    private final SomeEntityRepository repository;

    @PostMapping(value = "/someentity/save")
    public Long save() {
        SomeEntity someEntity = repository.save(SomeEntity.builder().data("testData").build());
        return someEntity.getId();
    }

    @GetMapping(value = "/someentity/{id}")
    public ResponseEntity<SomeEntity> get(@PathVariable(name = "id") Long id) {
        Optional<SomeEntity> byId = repository.findById(id);
        if (byId.isPresent()) {
            return ResponseEntity.ok(byId.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
