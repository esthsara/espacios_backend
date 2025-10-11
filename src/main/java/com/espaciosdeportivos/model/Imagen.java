package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "imagen", indexes = {
    @Index(name = "idx_imagen_estado", columnList = "estado"),
    @Index(name = "idx_imagen_fecha_creacion", columnList = "fecha_creacion")
})
@Data
public class Imagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagen")
    private Long idImagen;
    
    @Column(name = "nombre_archivo", nullable = false, length = 255)
    @NotBlank(message = "El nombre del archivo es obligatorio")
    private String nombreArchivo;
    
    @Column(name = "ruta_almacenamiento", nullable = false, length = 500)
    @NotBlank(message = "La ruta de almacenamiento es obligatoria")
    private String rutaAlmacenamiento;
    
    @Column(name = "tipo_mime", length = 100)
    private String tipoMime;
    
    @Column(name = "tamanio_bytes")
    @Positive(message = "El tamaño del archivo debe ser positivo")
    private Long tamanioBytes;
    
    @Column(name = "estado")
    private Boolean estado = true;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Constructor sin argumentos (Lombok lo genera, pero explícito para claridad)
    public Imagen() {
        this.estado = true;
    }
    //
    // Constructor útil para testing
    public Imagen(String nombreArchivo, String rutaAlmacenamiento, String tipoMime, Long tamanioBytes) {
        this.nombreArchivo = nombreArchivo;
        this.rutaAlmacenamiento = rutaAlmacenamiento;
        this.tipoMime = tipoMime;
        this.tamanioBytes = tamanioBytes;
        this.estado = true;
    }
}