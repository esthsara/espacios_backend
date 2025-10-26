package com.espaciosdeportivos.dto;

import java.io.Serializable;
import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZonaDTO implements Serializable{
    private Long idZona;

    @NotBlank(message = "El nombre de la zona es obligatorio")
    private String nombre;
    
    @Size(max = 600, message = "La descripción no puede tener más de 600 caracteres")   
    private String descripcion;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    @NotNull(message = "El id del macrodistrito es obligatorio")
    private Long idMacrodistrito;

    //objeto
    private MacrodistritoDTO macrodistrito;
}
