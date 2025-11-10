package com.espaciosdeportivos.model;

import lombok.*;
import jakarta.persistence.*;


import java.time.LocalTime;
import java.util.List;


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
    
    //comentario : aqui cambie el tipo de dato de String a LocalTime
    @Column(name = "hora_inicio_area")
    private LocalTime horaInicioArea;

    @Column(name = "hora_fin_area")
    private LocalTime horaFinArea;

    //@Column(name = "estado_area", nullable = false, length = 100)
    //private String estadoArea;

    @Column(name = "url_imagen", length = 800)
    private String urlImagen;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    /*
    @CreationTimestamp
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
     */
    //aqui me falta lo que SON LA ELIINACION DE UNA ZONA NO DEBERIA ELIMINAR LAS AREAS DEPORTIVAS ASOCIADAS A ELLA POR ESO SE DEB REASIGNNAR (PENSARRLO BIEN)
    //AQUI SI ELIMINO UNA ZONA QUE TIENE AREAS DEPORTIVAS ASOCIADAS NO DEJA ELIMINAR LA ZONA ENTONCES HAY QUE EVALUAR
    @ManyToOne
    @JoinColumn(name = "id_zona")
    private Zona zona;

    @OneToOne
    @JoinColumn(name = "id_persona", nullable = false, unique = true)
    private Administrador administrador;

    //COMENTARIO: AQUI AGREGE EL CASCADE Y ORPHANREMOVAL PARA QUE AL ELIMINAR UN AREA DEPORTIVA SE ELIMINEN LAS CANCHAS ASOCIADAS 
    //LAS CANCHAS SE ELIMINAN  AL ELIMINAR UNA AREA DEPORTIVA
    //CON LAZI SOLO HAGO CONSUÑTAS CUANDO NECESITO
    @OneToMany(mappedBy = "areaDeportiva",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Cancha> cancha;


    
}