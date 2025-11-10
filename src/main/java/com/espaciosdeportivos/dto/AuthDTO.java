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

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class JwtResponse {
        private String token;
        private String type;
        private Long id;
        private String username;
        private String email;
        private Set<String> roles;

        // nuevo campo para enviar el ID de la persona
        private Long idPersona;

        // 🔧 CAMBIO: nuevo constructor que incluye idPersona
        public JwtResponse(String token, Long id, String username, String email, Set<String> roles, Long idPersona) {
            this.token = token;
            this.type = "Bearer";
            this.id = id;
            this.username = username;
            this.email = email;
            this.roles = roles;
            this.idPersona = idPersona; //agregando K
        }
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class MessageResponse {
        private String message;
    }
}
