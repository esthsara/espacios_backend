package com.espaciosdeportivos.dto;

import java.io.Serializable;
import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MacrodistritoDTO implements Serializable{
    private Long idMacrodistrito;

    @NotBlank(message = "El nombre del macrodistrito es obligatorio")
    private String nombre;

    @Size(max = 600, message = "La descripción no puede tener más de 600 caracteres")   
    private String descripcion;

    @NotBlank(message = "El estado es obligatorio")
    private Boolean estado;
}
