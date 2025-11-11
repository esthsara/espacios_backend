package com.espaciosdeportivos.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class RegistroDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RegistroClienteRequest {
        @NotBlank(message = "El nombre de usuario es obligatorio.")
        @Size(min = 3, max = 20)
        private String username;

        @NotBlank(message = "El email es obligatorio.")
        @Size(max = 50)
        @Email
        private String email;

        @NotBlank(message = "La contraseña es obligatoria.")
        @Size(min = 6, max = 100)
        private String password;

        // Datos Persona
        @NotBlank(message = "El nombre es obligatorio.")
        private String nombre;

        @NotBlank(message = "El apellido paterno es obligatorio.")
        private String apellidoPaterno;

        @NotBlank(message = "El apellido materno es obligatorio.")
        private String apellidoMaterno;

        @NotBlank(message = "El teléfono es obligatorio.")
        @Pattern(regexp = "^[0-9]{8}$", message = "El teléfono debe tener 8 dígitos.")
        private String telefono;

        @NotNull(message = "La fecha de nacimiento es obligatoria.")
        @Past(message = "La fecha de nacimiento debe ser pasada.")
        private LocalDate fechaNacimiento;

        private String urlImagen;

        @NotBlank(message = "La categoría es obligatoria.")
        private String categoria;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RegistroAdministradorRequest {
        @NotBlank(message = "El nombre de usuario es obligatorio.")
        @Size(min = 3, max = 20)
        private String username;

        @NotBlank(message = "El email es obligatorio.")
        @Size(max = 50)
        @Email
        private String email;

        @NotBlank(message = "La contraseña es obligatoria.")
        @Size(min = 6, max = 100)
        private String password;

        @NotBlank(message = "La contraseña de administrador es obligatoria.")
        private String adminPassword;

        // Datos Persona
        @NotBlank(message = "El nombre es obligatorio.")
        private String nombre;

        @NotBlank(message = "El apellido paterno es obligatorio.")
        private String apellidoPaterno;

        @NotBlank(message = "El apellido materno es obligatorio.")
        private String apellidoMaterno;

        @NotBlank(message = "El teléfono es obligatorio.")
        @Pattern(regexp = "^[0-9]{8}$", message = "El teléfono debe tener 8 dígitos.")
        private String telefono;

        @NotNull(message = "La fecha de nacimiento es obligatoria.")
        @Past(message = "La fecha de nacimiento debe ser pasada.")
        private LocalDate fechaNacimiento;

        private String urlImagen;

        @NotBlank(message = "El cargo es obligatorio.")
        private String cargo;

        @NotBlank(message = "La dirección es obligatoria.")
        private String direccion;
    }
}