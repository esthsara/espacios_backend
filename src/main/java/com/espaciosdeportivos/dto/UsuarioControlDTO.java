package com.espaciosdeportivos.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioControlDTO implements Serializable {

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

    // 
    @NotBlank(message = "El estado operativo es obligatorio.")
    @Size(max = 50, message = "El estado operativo no puede exceder los 50 caracteres.")
    private String estadoOperativo;

    @NotNull(message = "La hora de inicio de turno es obligatoria.")
    private LocalTime horaInicioTurno;

    @NotNull(message = "La hora de fin de turno es obligatoria.")
    private LocalTime horaFinTurno;

    @NotBlank(message = "La dirección es obligatoria.")
    @Size(max = 200, message = "La dirección no puede exceder los 200 caracteres.")
    private String direccion;

    //objeto
    private List<CanchaDTO> canchasAsignadas;   
}
