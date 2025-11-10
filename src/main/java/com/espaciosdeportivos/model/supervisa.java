package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "supervisa")
public class Supervisa {

    @EmbeddedId
    private SupervisaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idUsControl")
    @JoinColumn(name = "id_us_control", referencedColumnName = "id_persona")
    private UsuarioControl usuarioControl;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idCancha")
    @JoinColumn(name = "id_cancha", referencedColumnName = "id_cancha")
    private Cancha cancha;

}
