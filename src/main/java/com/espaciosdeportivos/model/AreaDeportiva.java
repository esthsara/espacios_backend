package com.espaciosdeportivos.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore; // <--- Importar esto
//solucionado 

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "areadeportiva")
public class AreaDeportiva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_areadeportiva")
    private Long idAreaDeportiva;

    @Column(name = "nombre_area", nullable = false, length = 100)
    private String nombreArea;

    @Column(name = "descripcion_area", length = 600)
    private String descripcionArea;

    @Column(name = "email_area", length = 100)
    private String emailArea;

    @Column(name = "telefono_area", length = 8)
    private String telefonoArea;

    @Column(name = "hora_inicio_area")
    private LocalTime horaInicioArea;

    @Column(name = "hora_fin_area")
    private LocalTime horaFinArea;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @ManyToOne
    @JoinColumn(name = "id_zona")
    private Zona zona;

    @OneToOne
    @JoinColumn(name = "id_persona", nullable = false, unique = true)
    private Administrador administrador;

    // --- AQUÍ ESTABA EL PROBLEMA ---
    // Al agregar @JsonIgnore, evitamos que intente serializar las canchas
    // al crear o listar el área, rompiendo el bucle infinito.
    @OneToMany(mappedBy = "areaDeportiva", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // <--- ¡ESTA LÍNEA ES LA SOLUCIÓN!
    private List<Cancha> cancha;
}