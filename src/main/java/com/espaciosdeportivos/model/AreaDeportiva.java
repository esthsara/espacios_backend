package com.espaciosdeportivos.model;

import lombok.*;
import jakarta.persistence.*;
//import java.util.List;

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
    private String horaInicioArea;
    @Column(name = "hora_fin_area")
    private String horaFinArea;

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

    @ManyToOne
    @JoinColumn(name = "id_zona")
    private Zona zona;

    @ManyToOne
    @JoinColumn(name = "id_persona",nullable = false)
    private Administrador administrador;

    /*@OneToMany(mappedBy = "areaDeportiva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cancha> cancha;*/

    
}