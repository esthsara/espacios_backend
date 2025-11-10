package com.espaciosdeportivos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.io.Serializable;
//import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipaDTO implements Serializable {
    
    @NotNull(message = "El id_invitado es obligatorio")
    @Positive(message = "El id_invitado debe ser un valor positivo")
    private Long idInvitado;

    @NotNull(message = "El id_reserva es obligatorio")
    @Positive(message = "El id_reserva debe ser un valor positivo")
    private Long idReserva;


    private Boolean asistio;
    private Boolean confirmado;
    private Boolean notificado;
    
    @NotBlank(message = "Las observaciones son obligatorias")   
    private String observaciones;

    //objeto
    private ReservaDTO reserva;
    private InvitadoDTO invitado;


    // Campos de solo lectura (respuesta)
    /*private String nombreInvitado;
    private String emailInvitado;
    private String telefonoInvitado;
    private Boolean verificadoInvitado;
    private LocalDateTime fechaInvitacion;
    private String codigoReserva;
    private String estadoReserva;*/
}