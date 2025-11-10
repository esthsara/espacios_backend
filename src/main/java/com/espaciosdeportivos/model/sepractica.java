package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sepractica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sepractica {

    @EmbeddedId
    private SepracticaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idCancha")
    @JoinColumn(name = "id_cancha", nullable = false)
    private Cancha cancha;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idDisciplina")
    @JoinColumn(name = "id_disciplina", nullable = false)
    private Disciplina disciplina;

    @Column(name = "nivel_dificultad", length = 50)
    private String nivelDificultad; // "PRINCIPIANTE", "INTERMEDIO", "AVANZADO"

    @Column(name = "recomendaciones", length = 500)
    private String recomendaciones;
}