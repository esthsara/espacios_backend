package com.espaciosdeportivos.dto;

import jakarta.validation.constraints.*;
import lombok.*;

//lo puse en vez de @data

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonaPublicaDTO {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @Size(max = 100, message = "El apellido paterno no puede exceder los 100 caracteres")
    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno es obligatorio")
    @Size(max = 100, message = "El apellido materno no puede exceder los 100 caracteres")
    private String apellidoMaterno;

    @Size(max = 800, message = "La URL de la imagen no puede exceder los 800 caracteres")
    private String urlImagen;
}
