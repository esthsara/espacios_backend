package com.espaciosdeportivos.dto;

//import com.espaciosdeportivos.model.Reserva;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaDTO implements Serializable {

    private Long idReserva;

    @NotNull(message = "La fecha de creación es obligatoria")
    @PastOrPresent(message = "La fecha de creación no puede ser futura")
    private LocalDate fechaCreacion;

    @NotNull(message = "La fecha de la reserva es obligatoria")
    @FutureOrPresent(message = "La fecha de la reserva no puede ser en el pasado")
    private LocalDate fechaReserva;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    @NotBlank(message = "El estado de la reserva es obligatorio")
    @Pattern(regexp = "PENDIENTE|CONFIRMADA|EN_CURSO|COMPLETADA|CANCELADA|NO_SHOW", 
             message = "Estado de reserva no válido")
    private String estadoReserva;

    @NotNull(message = "El monto total es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private Double montoTotal;

    @Size(max = 500, message = "Las observaciones no pueden superar los 500 caracteres")
    private String observaciones;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId; // id_persona del cliente

    // Campos opcionales
    private Integer duracionMinutos;
    private String codigoReserva;

    // Campos de solo lectura (respuesta)
    private String nombreCliente;
    private String emailCliente;
    private String telefonoCliente;
    private String categoriaCliente;
    private LocalDate fechaActualizacion;
    
    // Información de pagos
    private Double totalPagado;
    private Double saldoPendiente;
    private Boolean pagadaCompleta;
}