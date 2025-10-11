package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.LocalDateTime;

@Entity
@Table(name = "imagen_relacion", indexes = {
    @Index(name = "idx_imagen_relacion_entidad", columnList = "entidad_tipo, entidad_id"),
    @Index(name = "idx_imagen_relacion_estado", columnList = "estado"),
    @Index(name = "idx_imagen_relacion_orden", columnList = "orden")
})
@Data
public class ImagenRelacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagen_relacion") 
    private Long idImagenRelacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_imagen", nullable = false) 
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "La imagen es obligatoria")
    private Imagen imagen;

    @Column(name = "entidad_tipo", nullable = false, length = 50)
    @NotBlank(message = "El tipo de entidad es obligatorio")
    private String entidadTipo; 

    @Column(name = "entidad_id", nullable = false)
    @NotNull(message = "El ID de la entidad es obligatorio")
    @PositiveOrZero(message = "El ID de la entidad debe ser positivo")
    private Long entidadId;

    @Column(name = "orden")
    @PositiveOrZero(message = "El orden debe ser positivo o cero")
    private Integer orden = 0;

    @Column(name = "estado")
    private Boolean estado = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    //
    // Constructor sin argumentos
    public ImagenRelacion() {
        this.estado = true;
        this.orden = 0;
    }

    // Constructor útil
    public ImagenRelacion(Imagen imagen, String entidadTipo, Long entidadId, Integer orden) {
        this.imagen = imagen;
        this.entidadTipo = entidadTipo;
        this.entidadId = entidadId;
        this.orden = orden != null ? orden : 0;
        this.estado = true;
    }
}
