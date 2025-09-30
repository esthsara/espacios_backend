package com.espaciosdeportivos.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonaDTO {

    private Long idPersona;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
    private String nombre;

    @Size(max = 100, message = "El apellido paterno no puede exceder los 100 caracteres.")
    private String aPaterno;

    @Size(max = 100, message = "El apellido materno no puede exceder los 100 caracteres.")
    private String aMaterno;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada.")
    private LocalDate fechaNacimiento;

    @Pattern(regexp = "^\\+591(7|6|4|3)\\d{7}$", message = "El teléfono debe ser un número válido de Bolivia.")
    private String telefono;

    @Email(message = "El email debe ser válido.")
    @Size(max = 150, message = "El email no puede exceder los 150 caracteres.")
    private String email;
    
    @NotBlank(message = "La URL de la imagen es obligatoria.")
    private String urlImagen;

    /*@NotBlank(message = "El número de cédula de identidad (CI) es obligatorio.")
    @Pattern(regexp = "^[0-9]{6,10}$", message = "El CI debe tener entre 6 y 10 dígitos numéricos.")
    private String ci;*/

}
