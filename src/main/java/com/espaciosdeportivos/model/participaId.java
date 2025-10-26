package com.espaciosdeportivos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipaId implements Serializable {

    @Column(name = "id_invitado")
    private Long idInvitado;

    @Column(name = "id_reserva")
    private Long idReserva;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipaId that = (ParticipaId) o;
        return Objects.equals(idInvitado, that.idInvitado) && 
               Objects.equals(idReserva, that.idReserva);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInvitado, idReserva);
    }

    @Override
    public String toString() {
        return "ParticipaId{" +
                "idInvitado=" + idInvitado +
                ", idReserva=" + idReserva +
                '}';
    }
}