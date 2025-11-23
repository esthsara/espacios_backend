package com.espaciosdeportivos.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // <--- IMPORTANTE
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cancha")
public class Cancha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cancha")
    private Long idCancha;

    @Column(name = "nombre_cancha", nullable = false, length = 100)
    private String nombre;

    @Column(name = "costo_hora", nullable = false)
    private Double costoHora;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Column(name = "mantenimiento", nullable = false, length = 100)
    private String mantenimiento;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fin")
    private LocalTime horaFin;

    @Column(name = "tipo_superficie", nullable = false, length = 100)
    private String tipoSuperficie;

    @Column(name = "tamano", nullable = false, length = 100)
    private String tamano;

    @Column(name = "iluminacion", nullable = false, length = 100)
    private String iluminacion;

    @Column(name = "cubierta", nullable = false, length = 100)
    private String cubierta;

    @Column(name = "url_imagen", length = 800)
    private String urlImagen;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_areadeportiva")
    // @JsonIgnore <--- Opcional: Si quieres evitar cargar el área completa al ver
    // la cancha
    private AreaDeportiva areaDeportiva;

    @OneToMany(mappedBy = "cancha", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // <--- NECESARIO
    private List<dispone> equipamiento;

    @OneToMany(mappedBy = "cancha", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // <--- NECESARIO
    private List<Comentario> comentario;

    // ESTE ES EL CULPABLE DEL BUCLE INFINITO "RESERVA <-> CANCHA"
    @OneToMany(mappedBy = "cancha", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // <--- ¡CRÍTICO! ESTO SOLUCIONA EL CRASH
    private List<incluye> incluidos;

    @OneToMany(mappedBy = "cancha", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // <--- NECESARIO
    private List<sepractica> sePractica;
}