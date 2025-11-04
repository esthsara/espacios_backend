package com.espaciosdeportivos.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.Set;
import java.time.LocalDate;

public class AuthDTO {

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class LoginRequest {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    // ✅ MANTENIDO: Registro general
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class SignupRequest {
        @NotBlank @Size(min = 3, max = 20)
        private String username;

        @NotBlank @Size(max = 50) @Email
        private String email;

        @NotBlank @Size(min = 6, max = 100)
        private String password;

        // Datos Persona
        private String nombre;
        private String apellidoPaterno;
        private String apellidoMaterno;
        private String telefono;
        private LocalDate fechaNacimiento;
        private String urlImagen;

        private String rolSolicitado; // "CLIENTE", "ADMINISTRADOR", "SUPERUSUARIO"
    }

    // ✅ NUEVO: DTO para registro de Cliente
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ClienteSignupRequest {
        @NotBlank @Size(min = 3, max = 20)
        private String username;

        @NotBlank @Size(max = 50) @Email
        private String email;

        @NotBlank @Size(min = 6, max = 100)
        private String password;

        // Datos Persona para Cliente
        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
        private String nombre;

        @NotBlank(message = "El apellido paterno es obligatorio.")
        @Size(max = 100, message = "El apellido paterno no puede exceder los 100 caracteres.")
        private String apellidoPaterno;

        @NotBlank(message = "El apellido materno es obligatorio.")
        @Size(max = 100, message = "El apellido materno no puede exceder los 100 caracteres.")
        private String apellidoMaterno;

        @NotNull(message = "La fecha de nacimiento es obligatoria.")
        @Past(message = "La fecha de nacimiento debe ser una fecha pasada.")
        private LocalDate fechaNacimiento;

        @NotBlank(message = "El teléfono es obligatorio.")
        @Pattern(regexp = "^[0-9]{8}$", message = "El teléfono debe tener exactamente 8 dígitos.")
        private String telefono;

        @NotBlank(message = "La URL de la imagen es obligatoria.")
        private String urlImagen;

        @NotBlank(message = "La categoria es muy necesaria")
        @Size(max = 50, message = "La categoria del cliente no puede exceder los 50 caracteres.")
        private String categoria;
    }

    // ✅ NUEVO: DTO para solicitud de Administrador
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class AdminSolicitudRequest {
        @NotBlank @Size(min = 3, max = 20)
        private String username;

        @NotBlank @Size(max = 50) @Email
        private String email;

        @NotBlank @Size(min = 6, max = 100)
        private String password;

        @NotBlank
        private String adminPassword; // Contraseña especial para solicitar admin

        // Datos Persona para Administrador
        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
        private String nombre;

        @NotBlank(message = "El apellido paterno es obligatorio.")
        @Size(max = 100, message = "El apellido paterno no puede exceder los 100 caracteres.")
        private String apellidoPaterno;

        @NotBlank(message = "El apellido materno es obligatorio.")
        @Size(max = 100, message = "El apellido materno no puede exceder los 100 caracteres.")
        private String apellidoMaterno;

        @NotNull(message = "La fecha de nacimiento es obligatoria.")
        @Past(message = "La fecha de nacimiento debe ser una fecha pasada.")
        private LocalDate fechaNacimiento;

        @NotBlank(message = "El teléfono es obligatorio.")
        @Pattern(regexp = "^[0-9]{8}$", message = "El teléfono debe tener exactamente 8 dígitos.")
        private String telefono;

        @NotBlank(message = "La URL de la imagen es obligatoria.")
        private String urlImagen;

        @NotBlank(message = "El cargo es obligatorio.")
        @Size(max = 100, message = "El cargo no puede exceder los 100 caracteres.")
        private String cargo;

        @NotBlank(message = "La dirección es obligatoria.")
        @Size(max = 255, message = "La dirección no puede exceder los 255 caracteres.")
        private String direccion;
    }

    // ✅ MANTENIDO: JwtResponse con idPersona
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class JwtResponse {
        private String token;
        private String type;
        private Long id;
        private String username;
        private String email;
        private Set<String> roles;
        private Long idPersona;

        public JwtResponse(String token, Long id, String username, String email, Set<String> roles, Long idPersona) {
            this.token = token;
            this.type = "Bearer";
            this.id = id;
            this.username = username;
            this.email = email;
            this.roles = roles;
            this.idPersona = idPersona;
        }
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class MessageResponse {
        private String message;
    }
}