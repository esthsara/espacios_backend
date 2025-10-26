package com.espaciosdeportivos.dto;

import lombok.*;
import java.time.LocalDate;
import jakarta.validation.constraints.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvitadoDTO implements Serializable {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
    private String nombre;

    @Size(max = 100, message = "El apellido paterno no puede exceder los 100 caracteres.")
    private String apellidoPaterno;   // cambiado nombre más claro

    @NotBlank(message = "El apellido materno es obligatorio.")
    @Size(max = 100, message = "El apellido materno no puede exceder los 100 caracteres.")
    private String apellidoMaterno;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada.")
    private LocalDate fechaNacimiento;

    @Pattern(regexp = "^[0-9]{8}$", message = "El teléfono debe tener exactamente 8 dígitos.")
    private String telefono;

    @Email(message = "El email debe ser válido.")
    @Size(max = 150, message = "El email no puede exceder los 150 caracteres.")
    private String email;

    //@NotBlank(message = "La URL de la imagen es obligatoria.")
    private String urlImagen;

    @NotNull(message = "El estado es obligatorio.")
    private Boolean estado;

    @NotNull(message = "El estado de verificación es obligatorio.")
    private Boolean verificado;

    //objeto
    private QrDTO qr;
}
