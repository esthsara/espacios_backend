package com.espaciosdeportivos.dto;

import lombok.*;
import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdministradorDTO implements Serializable {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
    private String nombre;

    @Size(max = 100, message = "El apellido paterno no puede exceder los 100 caracteres.")
    private String aPaterno;

    @NotBlank(message = "El apellido materno es obligatorio.")
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

    @NotBlank(message = "El cargo es obligatorio.")
    @Size(max = 100, message = "El cargo no puede exceder los 100 caracteres.")
    private String cargo;

    @NotBlank(message = "La dirección es obligatoria.")
    @Size(max = 255, message = "La dirección no puede exceder los 255 caracteres.")
    private String direccion;
}
