package com.espaciosdeportivos.model;

import lombok.*;

import java.time.LocalTime;
//import java.util.ArrayList;

import jakarta.persistence.*;

import java.util.ArrayList;
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

    /*@Column(name = "estado_cancha", nullable = false, length = 100)
    private String estado;*/

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

    /*
    @CreationTimestamp
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @UpdateTimestampx
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_areadeportiva")
    private AreaDeportiva areaDeportiva;

    //revisar por ejmplo si comparten equipamiento enonces no


    @OneToMany(mappedBy = "cancha", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Dispone> equipamiento;

    @OneToMany(mappedBy = "cancha", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comentario> comentario;

    @OneToMany(mappedBy = "cancha", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Incluye> incluidos;

    @OneToMany(mappedBy = "cancha", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Sepractica> sePractica;



}
