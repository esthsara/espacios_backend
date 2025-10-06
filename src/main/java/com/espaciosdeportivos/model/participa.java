package com.espaciosdeportivos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "participa")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // para seguridad extra
public class participa {
//J
    @EmbeddedId
    private participaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idInvitado")
    @JoinColumn(name = "id_invitado", referencedColumnName = "id_persona")
    private Invitado invitado;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idReserva")
    @JoinColumn(name = "id_reserva", referencedColumnName = "id_reserva")
    private Reserva reserva;
}
