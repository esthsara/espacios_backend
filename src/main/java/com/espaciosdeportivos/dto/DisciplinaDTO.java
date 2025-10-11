package com.espaciosdeportivos.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisciplinaDTO implements Serializable {
    
    private Long idDisciplina;
    
    @NotBlank(message = "El nombre de la disciplina no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;

    private Boolean estado;
    
    // Para RESPUESTA - imágenes ya procesadas
    private List<ImagenDTO> imagenes;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
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