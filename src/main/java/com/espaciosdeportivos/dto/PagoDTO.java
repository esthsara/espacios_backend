package com.espaciosdeportivos.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoDTO implements Serializable {

    private Long idPago;

    @NotNull(message = "El monto no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private Double monto;

    @NotNull(message = "La fecha del pago es obligatoria")
    @PastOrPresent(message = "La fecha del pago no puede ser futura")
    private LocalDate fecha;

    @NotBlank(message = "El tipo de pago no puede estar vacío")
    @Pattern(regexp = "PARCIAL|TOTAL|ANTICIPO", message = "Tipo de pago debe ser: PARCIAL, TOTAL o ANTICIPO")
    private String tipoPago;

    @NotBlank(message = "El método de pago no puede estar vacío")
    @Pattern(regexp = "EFECTIVO|TARJETA_CREDITO|TARJETA_DEBITO|TRANSFERENCIA|QR", 
             message = "Método de pago no válido")
    private String metodoPago;

    @NotBlank(message = "El estado del pago no puede estar vacío")
    @Pattern(regexp = "PENDIENTE|CONFIRMADO|ANULADO|RECHAZADO", 
             message = "Estado de pago no válido")
    private String estado;

    @Size(max = 100, message = "El código de transacción no puede exceder los 100 caracteres")
    private String codigoTransaccion;

    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    private String descripcion;

    @NotNull(message = "El ID de la reserva es obligatorio")
    private Long idReserva;

    //objeto
    private ClienteDTO cliente;

    // Campos de solo lectura (respuesta)
    /*private LocalDate fechaCreacion;
    private LocalDate fechaActualizacion;
    private String codigoReserva;
    private LocalDate fechaReserva;
    private Long idCliente;
    private String nombreCliente;
    private String emailCliente;
    private String telefonoCliente;
    private Double montoTotalReserva;
    private Double saldoPendiente;*/
}