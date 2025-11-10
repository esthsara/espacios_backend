package com.espaciosdeportivos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SepracticaId implements Serializable {

    @Column(name = "id_cancha")
    private Long idCancha;

    @Column(name = "id_disciplina")
    private Long idDisciplina;
}