package com.espaciosdeportivos.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {
    private String username;
    private String password;
    private String email;
    private String rolSolicitado;

    // Datos de Persona
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String urlImagen;
}
