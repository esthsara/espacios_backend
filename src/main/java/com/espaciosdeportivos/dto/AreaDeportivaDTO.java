package com.espaciosdeportivos.dto;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import jakarta.validation.constraints.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AreaDeportivaDTO implements Serializable{
    private Long idAreadeportiva;

    @NotBlank(message = "El nombre del área es obligatorio")
    private String nombreArea;

    @Size(max = 600, message = "La descripción no puede tener más de 600 caracteres")
    private String descripcionArea;

    @Email(message = "El email debe ser válido")
    private String emailArea;

    @Pattern(regexp = "\\d{8}", message = "El teléfono debe tener 8  dígitos")
    private String telefonoArea;

    @NotNull(message = "La hora de inicio es obligatoria")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicioArea;

    @NotNull(message = "La hora de fin es obligatoria")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaFinArea;

    //@NotBlank(message = "El estado del área es obligatorio")
    //private String estadoArea;

    private String urlImagen;
    
    @NotNull(message = "La latitud del área es obligatorio")
    private Double latitud;

    @NotNull(message = "La longitud del área es obligatorio")
    private Double longitud;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    @NotNull(message = "La dirección es obligatoria")
    @Positive(message = "El ID de la zona debe ser un valor positivo")
    private Long idZona;

    @NotNull(message = "El ID del administrador es obligatorio")
    @Positive(message = "El ID del administrador debe ser un valor positivo")   
    private Long id;

    //objeto para front K
    private ZonaDTO zona; 

    // Para RESPUESTA - imágenes ya procesadas
    private List<ImagenDTO> imagenes;
    //private LocalDateTime fechaCreacion;
    //private LocalDateTime fechaActualizacion;
    
    // Para CREACIÓN/ACTUALIZACIÓN - ignorado en JSON
    @JsonIgnore
    private transient List<MultipartFile> archivosImagenes;
    
    // Métodos de utilidad
    public boolean tieneArchivosParaProcesar() {
        return archivosImagenes != null && !archivosImagenes.isEmpty();
    }
    //
    public boolean esValidoParaCreacion() {
        return nombreArea != null && !nombreArea.trim().isEmpty();
    }
}
