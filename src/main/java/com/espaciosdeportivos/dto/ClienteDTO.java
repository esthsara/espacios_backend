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
public class ClienteDTO implements Serializable {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio.")
    @Size(max = 100, message = "El apellido paterno no puede exceder los 100 caracteres.")
    private String aPaterno;


    @NotBlank(message = "El apellido materno es obligatorio.")
    @Size(max = 100, message = "El apellido materno no puede exceder los 100 caracteres.")
    private String aMaterno;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada.")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El teléfono es obligatorio.")
    @Pattern(regexp = "^[0-9]{8}$", message = "El teléfono debe tener exactamente 8 dígitos.")
    private String telefono;

    @Email(message = "El email debe ser válido.")
    @NotBlank(message = "El email es obligatorio.")
    @Size(max = 150, message = "El email no puede exceder los 150 caracteres.")
    private String email;

    @NotBlank(message = "La URL de la imagen es obligatoria.")
    private String urlImagen;

    @NotNull(message = "El estado es obligatorio.")
    private Boolean estado;

    @NotBlank(message = "La categoria es muy necesaria")
    @Size(max = 50, message = "La categoria del cliente no puede exceder los 50 caracteres.")
    private String categoria;
}
