package com.espaciosdeportivos.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComentarioDTO implements Serializable {

    private Long idComentario;

    @NotBlank(message = "El contenido no puede estar vacío")
    @Size(max = 500, message = "El contenido no puede exceder los 500 caracteres")
    private String contenido;

    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer calificacion;

    @NotNull(message = "La fecha del comentario es obligatoria")
    private LocalDateTime fecha;

    @NotNull(message = "El ID de la persona es obligatorio")
    private Long idPersona;

    @NotNull(message = "El ID de la cancha es obligatorio")
    private Long idCancha;

    @NotNull(message = "El estado del comentario es obligatorio")
    private Boolean estado;

    private PersonaPublicaDTO persona; 

}