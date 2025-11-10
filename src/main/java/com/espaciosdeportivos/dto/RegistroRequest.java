package com.espaciosdeportivos.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroRequest {
    private String username;
    private String password;
    private String email;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String urlImagen;
    private String ci;
    private String rolSolicitado;
}
