package com.espaciosdeportivos.dto;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CanchaDTO implements Serializable {
    private Long idCancha;

    @NotBlank(message = "El nombre de la cancha es obligatorio")
    private String nombre;

    @NotNull(message = "El costo por hora es obligatorio")
    @Positive(message = "El costo por hora debe ser un valor positivo")
    private Double costoHora;

    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser un valor positivo")
    private Integer capacidad;

   /* @NotBlank(message = "El estado es obligatorio")
    private String estado;*/

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    @NotBlank(message = "El mantenimiento es obligatorio")
    private String mantenimiento;

    @NotNull(message = "La hora de inicio es obligatoria")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria") 
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaFin;

    @NotBlank(message = "El tipo de superficie es obligatorio")
    private String tipoSuperficie;

    @NotBlank(message = "El tamaño es obligatorio")
    private String tamano;

    @NotBlank(message = "La iluminación es obligatoria")
    private String iluminacion;

    @NotBlank(message = "El tipo de cubierta es obligatorio")
    private String cubierta;

    @Size(max = 900, message = "La URL de la imagen no puede exceder 900 caracteres")
    private String urlImagen;

    @NotNull(message = "El id del área deportiva es obligatorio")
    @Positive(message = "El id del área deportiva debe ser un valor positivo")  
    private Long idAreadeportiva;


    //objetos
    private AreaDeportivaDTO areaDeportiva; // objeto front

    private List<EquipamientoDTO> equipamientos;
    //private List<ReservaDTO> reservas;
    private List<DisciplinaDTO> disciplinas;
    private List<ComentarioDTO> comentarios;

    
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
        return nombre != null && !nombre.trim().isEmpty();
    }
}
