package com.espaciosdeportivos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncluyeDTO implements Serializable {

    @NotNull(message = "El ID de la cancha es obligatorio")
    @Positive(message = "El ID de la cancha debe ser positivo")
    private Long idCancha;

    @NotNull(message = "El ID de la reserva es obligatorio")
    @Positive(message = "El ID de la reserva debe ser positivo")
    private Long idReserva;

    @NotNull(message = "El ID de la disciplina es obligatorio")
    @Positive(message = "El ID de la disciplina debe ser positivo")
    private Long idDisciplina;
}