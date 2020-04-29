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
 * QuestionMeta.
 *
 * @author Nikita_Mikaev
 */
@Entity
@Table(name = "question_meta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotEmpty
    @Column(name = "text", nullable = false)
    private String text;
}
