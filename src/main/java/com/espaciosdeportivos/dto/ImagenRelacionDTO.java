package com.espaciosdeportivos.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ImagenRelacionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idImagenRelacion;
    
    @NotNull(message = "El ID de la imagen es obligatorio")
    @Positive(message = "El ID de la imagen debe ser positivo")
    private Long idImagen;
    
    @NotBlank(message = "El tipo de entidad es obligatorio")
    @Size(max = 50, message = "El tipo de entidad no puede superar los 50 caracteres")
    private String entidadTipo; // DISCIPLINA, CANCHA, MACRODISTRITO, USUARIO, etc.
    
    @NotNull(message = "El ID de la entidad es obligatorio")
    @Positive(message = "El ID de la entidad debe ser positivo")
    private Long entidadId;
    
    @NotNull(message = "El orden no puede ser nulo")
    @PositiveOrZero(message = "El orden debe ser positivo o cero")
    private Integer orden = 0;
    
    @NotNull(message = "El estado no puede ser nulo")
    private Boolean estado = true;
    
    private LocalDateTime fechaCreacion;
    
    // Información de la imagen (opcional - para respuestas completas)
    private ImagenDTO imagen;
    
    // Información de la entidad relacionada (opcional - para respuestas avanzadas)
    private String nombreEntidad;
    private String descripcionEntidad;
    
    // Constructores
    public ImagenRelacionDTO() {
        this.estado = true;
        this.orden = 0;
    }
    
    public ImagenRelacionDTO(Long idImagen, String entidadTipo, Long entidadId, Integer orden) {
        this.idImagen = idImagen;
        this.entidadTipo = entidadTipo;
        this.entidadId = entidadId;
        this.orden = orden != null ? orden : 0;
        this.estado = true;
    }
    
    public ImagenRelacionDTO(Long idImagenRelacion, Long idImagen, String entidadTipo, 
                           Long entidadId, Integer orden, Boolean estado) {
        this.idImagenRelacion = idImagenRelacion;
        this.idImagen = idImagen;
        this.entidadTipo = entidadTipo;
        this.entidadId = entidadId;
        this.orden = orden;
        this.estado = estado;
    }
    
    // Métodos helper
    public boolean esActiva() {
        return Boolean.TRUE.equals(estado);
    }
    
    public String obtenerClaveEntidad() {
        return entidadTipo + ":" + entidadId;
    }
    
    public boolean perteneceAEntidad(String tipoEntidad, Long idEntidad) {
        return entidadTipo.equals(tipoEntidad) && entidadId.equals(idEntidad);
    }
    
    // Para reordenamiento
    public void incrementarOrden() {
        this.orden = this.orden != null ? this.orden + 1 : 0;
    }
    
    public void decrementarOrden() {
        this.orden = this.orden != null && this.orden > 0 ? this.orden - 1 : 0;
    }
    //
}