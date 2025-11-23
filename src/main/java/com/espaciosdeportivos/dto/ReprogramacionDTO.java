package com.espaciosdeportivos.dto;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReprogramacionDTO {
    @NotNull(message = "La nueva fecha de reserva es obligatoria")
    @FutureOrPresent(message = "Debe ser una fecha futura o actual")
    private LocalDate nuevaFechaReserva;

    @NotNull(message = "La nueva hora de inicio es obligatoria")
    private LocalTime nuevaHoraInicio;

    @NotNull(message = "La nueva hora de fin es obligatoria")
    private LocalTime nuevaHoraFin;

    private String observaciones;
}
