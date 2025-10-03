package com.espaciosdeportivos.dto;

import java.io.Serializable;
import java.time.LocalTime;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CanchaDTO implements Serializable {
    private Long idCancha;

    @NotBlank(message = "El nombre de la cancha es obligatorio")
    private String nombre;

    @NotNull(message = "El costo por hora es obligatorio")
    @Positive(message = "El costo por hora debe ser un valor positivo")
    private Double costoHora;

    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser un valor positivo")
    private Integer capacidad;

   /* @NotBlank(message = "El estado es obligatorio")
    private String estado;*/

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    @NotBlank(message = "El mantenimiento es obligatorio")
    private String mantenimiento;

    @NotBlank(message = "La hora de inicio es obligatoria")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @NotBlank(message = "La hora de fin es obligatoria") 
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaFin;

    @NotBlank(message = "El tipo de superficie es obligatorio")
    private String tipoSuperficie;

    @NotBlank(message = "El tamaño es obligatorio")
    private String tamano;

    @NotBlank(message = "La iluminación es obligatoria")
    private String iluminacion;

    @NotBlank(message = "El tipo de cubierta es obligatorio")
    private String cubierta;

    @Size(max = 900, message = "La URL de la imagen no puede exceder 900 caracteres")
    private String urlImagen;

    @NotNull(message = "El id del área deportiva es obligatorio")
    @Positive(message = "El id del área deportiva debe ser un valor positivo")  
    private Long idAreadeportiva;
    
    
}
