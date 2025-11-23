package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class incluyeId implements Serializable {

    @Column(name = "id_cancha")
    private Long idCancha;

    @Column(name = "id_reserva")
    private Long idReserva;

    @Column(name = "id_disciplina")
    private Long idDisciplina;
}
