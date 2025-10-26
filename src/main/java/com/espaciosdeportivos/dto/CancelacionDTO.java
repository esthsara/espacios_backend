package com.espaciosdeportivos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelacionDTO implements Serializable {

    private Long idCancelacion;

    @NotBlank(message = "El motivo de cancelación es obligatorio")
    @Size(min = 5, max = 255, message = "El motivo debe tener entre 5 y 255 caracteres")
    private String motivo;

    @NotNull(message = "La fecha de cancelación es obligatoria")
    private LocalDate fechaCancelacion;

    @NotNull(message = "La hora de cancelación es obligatoria")
    private LocalTime horaCancelacion;

    @NotNull(message = "El estado de la cancelación es obligatorio")
    private Boolean estado;

    @NotNull(message = "El ID del cliente es obligatorio")
    @Positive(message = "El ID de la cliente debe ser un valor positivo")
    private Long idCliente;

    @NotNull(message = "El ID de la reserva es obligatorio")
    @Positive(message = "El ID de la reserva debe ser un valor positivo")
    private Long idReserva;
}