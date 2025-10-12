package com.espaciosdeportivos.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrDTO implements Serializable {

    private Long idQr;

    @NotBlank(message = "El código QR es obligatorio")
    @Size(max = 200, message = "El código QR no puede exceder los 200 caracteres")
    private String codigoQr;

    @NotNull(message = "La fecha de generación es obligatoria")
    private LocalDateTime fechaGeneracion;

    @NotNull(message = "La fecha de expiración es obligatoria")
    private LocalDateTime fechaExpiracion;

    @NotNull(message = "El estado del QR es obligatorio")
    private Boolean estado;

    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    private String descripcion;

    @NotNull(message = "El ID del usuario de control es obligatorio")
    private Long idUsuarioControl;

    @NotNull(message = "El ID de la reserva es obligatorio")
    private Long idReserva;

    @NotNull(message = "El ID del invitado es obligatorio")
    private Long idInvitado;

    //  Nuevos campos para visualizació
}
