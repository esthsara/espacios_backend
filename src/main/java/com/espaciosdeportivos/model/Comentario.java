package com.espaciosdeportivos.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comentario")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private Long idComentario;

    @Column(name = "contenido", nullable = false, length = 500)
    private String contenido;

    @Column(name = "calificacion", nullable = false)
    private Integer calificacion;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @ManyToOne
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
    private Persona persona;

    @ManyToOne
    @JoinColumn(name = "id_cancha", referencedColumnName = "id_cancha")
    private Cancha cancha;

    
}