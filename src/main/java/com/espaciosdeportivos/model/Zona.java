package com.espaciosdeportivos.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore; // <--- IMPORTAR ESTO

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "zona")
public class Zona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zona")
    private Long idZona;

    @Column(name = "nombre_zona", nullable = false, length = 200)
    private String nombre;

    @Column(name = "descripcion", length = 600)
    private String descripcion;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @ManyToOne
    @JoinColumn(name = "id_macrodistrito")
    private Macrodistrito macrodistrito;

    // --- AQUÍ ESTÁ LA SOLUCIÓN ---
    // Agregamos @JsonIgnore para que al pedir una Zona,
    // NO intente traernos todas las áreas deportivas asociadas y rompa el bucle.
    @OneToMany(mappedBy = "zona", /* cascade = CascadeType.ALL , */ orphanRemoval = false)
    @JsonIgnore // <--- ¡ESTA LÍNEA ES OBLIGATORIA!
    private List<AreaDeportiva> areaDeportiva;
}