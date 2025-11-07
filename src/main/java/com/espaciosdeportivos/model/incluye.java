package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "incluye")
public class Incluye {

    @EmbeddedId
    private IncluyeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idCancha")
    @JoinColumn(name = "id_cancha", nullable = false)
    private Cancha cancha;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idReserva")
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idDisciplina")
    @JoinColumn(name = "id_disciplina", nullable = false)
    private Disciplina disciplina;

    @Column(name = "monto_total", nullable = false)
    private Double montoTotal;
}
