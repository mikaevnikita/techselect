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
 * AnswerDto.
 *
 * @author Nikita_Mikaev
 */
@Entity
@Table(name = "answer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @NotNull
    @Column(name = "answer_meta_id", nullable = false)
    private Long answerMetaId;

    @NotNull
    @Column(name = "question_meta_id", nullable = false)
    private Long questionMetaId;

    @NotNull
    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @NotEmpty
    @Column(name = "text", nullable = false)
    private String text;
}