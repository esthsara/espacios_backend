package com.espaciosdeportivos.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class supervisaId implements Serializable {

    private Long idUsControl;
    private Long idCancha;

}
