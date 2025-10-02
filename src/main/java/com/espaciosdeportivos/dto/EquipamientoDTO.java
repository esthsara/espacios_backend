package com.espaciosdeportivos.dto;

import lombok.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipamientoDTO implements Serializable {
     private Long idEquipamiento;

    @NotBlank(message = "El nombre del equipamiento es obligatorio")
    private String nombreEquipamiento;

    @NotBlank(message = "El tipo de equipamiento es obligatorio")
    private String tipoEquipamiento;

    @Size(max = 400, message = "La descripción no puede tener más de 400 caracteres")
    private String descripcion;

   /* @NotBlank(message = "El estado del equipamiento es obligatorio")
    private String estado;*/

    private String urlImagen;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}
