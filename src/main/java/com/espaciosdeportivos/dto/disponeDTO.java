package com.espaciosdeportivos.dto;

import lombok.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisponeDTO implements Serializable{

    @NotNull(message = "El id_equipamiento es obligatorio")
    @Positive(message = "El id_equipamiento debe ser un valor positivo")
    private Long idEquipamiento;

    @NotNull(message = "El id_cancha es obligatorio")
    @Positive(message = "El id_cancha debe ser un valor positivo")
    private Long idCancha;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser un valor positivo")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

}

 