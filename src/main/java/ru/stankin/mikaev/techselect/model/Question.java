package ru.stankin.mikaev.techselect.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * QuestionDto.
 *
 * @author Nikita_Mikaev
 */
@Entity
@Table(name = "question")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @NotNull
    @Column(name = "meta_id", nullable = false)
    private Long metaId;

    @NotEmpty
    @Column(name = "text", nullable = false)
    private String text;
}

