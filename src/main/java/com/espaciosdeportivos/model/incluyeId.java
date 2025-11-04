package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class IncluyeId implements Serializable {

    @Column(name = "id_cancha")
    private Long idCancha;

    @Column(name = "id_reserva")
    private Long idReserva;

    @Column(name = "id_disciplina")
    private Long idDisciplina;

   /*  @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IncluyeId incluyeId = (IncluyeId) o;
        return Objects.equals(idCancha, incluyeId.idCancha) &&
               Objects.equals(idReserva, incluyeId.idReserva) &&
               Objects.equals(idDisciplina, incluyeId.idDisciplina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCancha, idReserva, idDisciplina);
    }*/
}