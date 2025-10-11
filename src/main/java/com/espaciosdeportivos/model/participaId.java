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
public class participaId implements Serializable {

    @Column(name = "id_invitado")
    private Long idInvitado;

    @Column(name = "id_reserva")
    private Long idReserva;
}
