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
public class SupervisaId implements Serializable {
    @Column(name = "id_us_control")
    private Long idUsControl;

    @Column(name = "id_cancha")
    private Long idCancha;

}
