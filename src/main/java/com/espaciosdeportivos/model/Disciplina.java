package com.espaciosdeportivos.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "disciplina")
@Data
public class Disciplina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disciplina")
    private Long idDisciplina;
    
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "estado")
    private Boolean estado = true;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    //
    //RELACIONES 
    //J
    //Relación con Cancha (a través de la tabla intermedia se_practica)
    //@OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<sepractica> canchas;
    //J
    // Relación con Reserva (a través de la tabla intermedia incluye)
    //@OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<incluye> reservas;
}